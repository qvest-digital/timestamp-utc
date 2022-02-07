package org.evolvis.tartools.tsutc;

/*-
 * tsutc (TimestampWithoutTimezone) is Copyright
 *  © 2016, 2018 mirabilos (t.glaser@tarent.de)
 * Licensor is tarent solutions GmbH, http://www.tarent.de/
 *
 * TimestampWithoutTimezoneTypeDescriptor is a derivative work of
 * Hibernate, Relational Persistence for Idiomatic Java, 5.1.0.FINAL,
 * Copyright © 2010, 2011, 2012, 2015, 2016 Red Hat, Inc. (under CLA)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License (as file COPYING in the META-INF directory) along with
 * this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

import org.evolvis.tartools.tsutc.entities.LogRow;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for tsutc with H2 in-memory database.
 *
 * @author mirabilos (t.glaser@tarent.de)
 */
public class IntegrationTest {

private static final Logger LOG =
    Logger.getLogger(TimestampWithoutTimezoneTest.class.getName());
private static final SimpleDateFormat df =
    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ROOT);

private static final String HELLO = "Hello, World!";
private static final String MEOW = "meow";

private static final String RECORD = "INSERT INTO logs (stamp, message) " +
    "VALUES ('1995-05-25 19:57:43', '4.4BSD-Lite2/COPYRIGHT');";
private static final String DUMP = "SCRIPT SIMPLE NOSETTINGS";
private static final String SELBSD = "from LogRow WHERE message LIKE '%BSD%'";
private static final String CMPBSD = "4.4BSD-Lite2/COPYRIGHT";

private SessionFactory sessionFactory;
private Session session;
private Transaction transaction;

@Before
public void
before()
{
	sessionFactory = createSessionFactory();
	session = sessionFactory.openSession();
	BEGIN();
}

private void
BEGIN()
{
	transaction = session.beginTransaction();
}

private void
COMMIT()
{
	transaction.commit();
}

private SessionFactory
createSessionFactory()
{
	Configuration cfg = new Configuration();
	cfg.addAnnotatedClass(LogRow.class);
	cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
	cfg.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
	/* cf. https://stackoverflow.com/a/70906928/2171120 */
	cfg.setProperty("hibernate.connection.url", "jdbc:h2:mem:testdb;MODE=LEGACY");
	cfg.setProperty("hibernate.hbm2ddl.auto", "create");
	return cfg.buildSessionFactory();
}

/**
 * Test we can store and retrieve values.
 */
@Test
public void
testPos()
{
	final Date teststamp = new Date(System.currentTimeMillis());
	LogRow logentry;

	assertNotNull(sessionFactory);
	assertNotNull(session);

	logentry = new LogRow();
	logentry.setStamp(teststamp);
	logentry.setMessage(HELLO);
	session.save(logentry);

	logentry = new LogRow();
	logentry.setStamp(teststamp);
	logentry.setMessage(MEOW);
	session.save(logentry);

	COMMIT();

	@SuppressWarnings("unchecked")
	List<LogRow> db = session.createQuery("from LogRow").list();

	boolean foundHello = false, foundMeow = false;

	for (LogRow entry : db) {
		LOG.log(Level.INFO, "LogRow({0}, {1}, ''{2}'')", new Object[] {
		    entry.getPk(),
		    df.format(entry.getStamp()),
		    entry.getMessage()
		});
		if (HELLO.equals(entry.getMessage()))
			foundHello = true;
		if (MEOW.equals(entry.getMessage()))
			foundMeow = true;
		assertEquals(teststamp.getTime(), entry.getStamp().getTime());
	}
	assertTrue(foundHello);
	assertTrue(foundMeow);

	BEGIN();
	session.doWork(conn -> conn.createStatement().executeUpdate(RECORD));
	COMMIT();

	LOG.info("BEGIN test database dump {{{");
	session.doWork(conn -> {
		ResultSet rs = conn.prepareStatement(DUMP).executeQuery();
		while (rs.next())
			LOG.info(rs.getString(1));
	});
	LOG.info("END test database dump }}}");

	@SuppressWarnings("unchecked")
	List<LogRow> d2 = session.createQuery(SELBSD).list();
	assertEquals(1, d2.size());
	assertEquals(801431863000L, d2.get(0).getStamp().getTime());
	assertEquals(CMPBSD, d2.get(0).getMessage());
}

}

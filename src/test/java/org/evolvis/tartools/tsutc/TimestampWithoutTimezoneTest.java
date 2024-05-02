package org.evolvis.tartools.tsutc;

/*-
 * tsutc (TimestampWithoutTimezone) is Copyright
 *  © 2016, 2018 mirabilos (t.glaser@qvest-digital.com)
 * Licensor: Qvest Digital AG, Bonn, Germany
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
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;

/**
 * Test class for tsutc, mostly so the classes are not marked as unused.
 *
 * @author mirabilos (t.glaser@qvest-digital.com)
 */
public class TimestampWithoutTimezoneTest {

private static final Logger LOG =
    Logger.getLogger(TimestampWithoutTimezoneTest.class.getName());
private static final SimpleDateFormat df =
    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ROOT);

/**
 * Pretty stupid test, basically just sees that this does not crash.
 */
@Test
public void
canInstantiateEntity()
{
	final Date teststamp = new Date(System.currentTimeMillis());
	LogRow logentry = new LogRow();

	logentry.setPk(0L);
	logentry.setStamp(teststamp);
	logentry.setMessage("Hello, World!");
	LOG.log(Level.INFO, "LogRow({0}, {1}, ''{2}'')", new Object[] {
	    logentry.getPk(),
	    df.format(logentry.getStamp()),
	    logentry.getMessage()
	});
	/* for IDE fooling */
	assertNotNull(LogRow.myType);
	assertNotNull(LogRow.anotherType);
}

}

package org.evolvis.tartools.tsutc;

/*-
 * tsutc (TimestampWithoutTimezone) is Copyright
 *  © 2016, 2018 mirabilos (t.glaser@tarent.de)
 * TimestampWithoutTimezoneTypeDescriptor is a derivative work of
 * Hibernate, Relational Persistence for Idiomatic Java, 5.1.0.FINAL,
 * Copyright © 2010, 2011, 2012, 2015, 2016 Red Hat, Inc. (under CLA)
 * Licensor is tarent solutions GmbH, http://www.tarent.de/
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

import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.BasicBinder;
import org.hibernate.type.descriptor.sql.BasicExtractor;
import org.hibernate.type.descriptor.sql.TimestampTypeDescriptor;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Descriptor for {@link Types#TIMESTAMP TIMESTAMP} handling.
 * Adapted for TIMESTAMP WITHOUT TIME ZONE as UTC by mirabilos.
 *
 * @author Steve Ebersole
 */
final class TimestampWithoutTimezoneTypeDescriptor extends TimestampTypeDescriptor {

private static final long serialVersionUID = -1447781774266391738L;

private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

/**
 * Singleton instance of {@link TimestampWithoutTimezoneTypeDescriptor}
 */
static final TimestampWithoutTimezoneTypeDescriptor INSTANCE =
    new TimestampWithoutTimezoneTypeDescriptor();

private class ReturnTuple {
	Timestamp timestamp;
	Calendar calendar;
}

/**
 * {@inheritDoc}
 */
@Override
public <X> ValueBinder<X>
getBinder(final JavaTypeDescriptor<X> javaTypeDescriptor)
{
	return (new BasicBinder<X>(javaTypeDescriptor, this) {
		private ReturnTuple
		internalBind(final X value, final WrapperOptions options)
		{
			ReturnTuple rv = new ReturnTuple();
			rv.timestamp = javaTypeDescriptor.unwrap(value,
			    Timestamp.class, options);
			rv.calendar = (value instanceof Calendar) ?
			    (Calendar)value : Calendar.getInstance(UTC);
			return (rv);
		}

		@Override
		protected void
		doBind(final PreparedStatement st, final X value,
		    final int index, final WrapperOptions options)
		throws SQLException
		{
			final ReturnTuple v = internalBind(value, options);
			st.setTimestamp(index, v.timestamp, v.calendar);
		}

		@Override
		protected void
		doBind(final CallableStatement st, final X value,
		    final String name, final WrapperOptions options)
		throws SQLException
		{
			final ReturnTuple v = internalBind(value, options);
			st.setTimestamp(name, v.timestamp, v.calendar);
		}
	});
}

/**
 * {@inheritDoc}
 */
@Override
public <X> ValueExtractor<X>
getExtractor(final JavaTypeDescriptor<X> javaTypeDescriptor)
{
	return (new BasicExtractor<X>(javaTypeDescriptor, this) {
		@Override
		protected X
		doExtract(final ResultSet rs, final String name,
		    final WrapperOptions options)
		throws SQLException
		{
			return (javaTypeDescriptor.wrap(rs.getTimestamp(name,
			    Calendar.getInstance(UTC)), options));
		}

		@Override
		protected X
		doExtract(final CallableStatement st, final int index,
		    final WrapperOptions options)
		throws SQLException
		{
			return (javaTypeDescriptor.wrap(st.getTimestamp(index,
			    Calendar.getInstance(UTC)), options));
		}

		@Override
		protected X
		doExtract(final CallableStatement st, final String name,
		    final WrapperOptions options)
		throws SQLException
		{
			return (javaTypeDescriptor.wrap(st.getTimestamp(name,
			    Calendar.getInstance(UTC)), options));
		}
	});
}

}

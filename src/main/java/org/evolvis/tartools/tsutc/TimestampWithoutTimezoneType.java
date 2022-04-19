package org.evolvis.tartools.tsutc;

/*-
 * tsutc (TimestampWithoutTimezone) is Copyright
 *  © 2016, 2018 mirabilos (t.glaser@tarent.de)
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

import org.hibernate.type.TimestampType;

/**
 * Hibernate type for persisting a (millis-only) java.util.Date
 * as SQL “TIMESTAMP” (aka “TIMESTAMP WITHOUT TIME ZONE”) in UTC.
 *
 * Sample use in an Entity:
 *
 * <pre><code>
 * {@literal @}Type(type="org.evolvis.tartools.tsutc.TimestampWithoutTimezoneType")
 * {@literal @}Column(name="update_date")
 * private Date updateDate;
 * </code></pre>
 *
 * @author mirabilos (t.glaser@tarent.de)
 */
public final class TimestampWithoutTimezoneType extends TimestampType {

private static final long serialVersionUID = 5886290597946668009L;

/**
 * Singleton instance of {@link TimestampWithoutTimezoneType}
 */
public static final TimestampWithoutTimezoneType INSTANCE =
    new TimestampWithoutTimezoneType();

/**
 * Constructs the class and overrides the {@link TimestampType} parent’s
 * SQL type descriptor to {@link TimestampWithoutTimezoneTypeDescriptor}.
 */
public
TimestampWithoutTimezoneType()
{
	super();
	setSqlTypeDescriptor(TimestampWithoutTimezoneTypeDescriptor.INSTANCE);
}

}

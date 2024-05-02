package org.evolvis.tartools.tsutc.entities;

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

import org.evolvis.tartools.tsutc.TimestampWithoutTimezoneType;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Test entity, deliberately in a separate package.
 *
 * @author mirabilos (t.glaser@qvest-digital.com)
 */
@Entity
@Table(name = "logs")
public class LogRow implements Serializable {

private static final long serialVersionUID = 5259042723540746538L;
/* for IDE fooling */
public static final TimestampWithoutTimezoneType myType =
    TimestampWithoutTimezoneType.INSTANCE;
public static final TimestampWithoutTimezoneType anotherType =
    new TimestampWithoutTimezoneType();

@Id
@Generated(GenerationTime.INSERT)
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "pk")
private Long pk;

@Type(type = "org.evolvis.tartools.tsutc.TimestampWithoutTimezoneType")
@Column
private Date stamp;

@Column
private String message;

public Long
getPk()
{
	return (pk);
}

public void
setPk(final Long newPK)
{
	pk = newPK;
}

public Date
getStamp()
{
	return (new Date(stamp.getTime()));
}

public void
setStamp(final Date timestamp)
{
	stamp = new Date(timestamp.getTime());
}

public String
getMessage()
{
	return (message);
}

public void
setMessage(final String newMessage)
{
	message = newMessage;
}

/* PersistenceObject by Christian Preilowski (c.thiel@tarent.de) */

/**
 * {@inheritDoc}
 */
@Override
public int
hashCode()
{
	if (getPk() == null)
		return (super.hashCode());
	return (getPk().hashCode());
}

/**
 * {@inheritDoc}
 */
@Override
public boolean
equals(final Object obj)
{
	if (this == obj)
		return (true);
	if (obj == null)
		return (false);
	if (!(getClass().equals(obj.getClass())))
		return (false);
	if (getPk() == null)
		return (super.equals(obj));
	return (getPk().equals(((LogRow)obj).getPk()));
}

}

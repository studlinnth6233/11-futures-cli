package de.thro.inf.prg3.a11.openmensa.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Data transfer object for a state of a canteen retrieved from the OpenMensaAPI
 *
 * @author Peter Kurfer
 */

public final class State {

	private String date;
	private boolean closed = true;

	public State() {
		date = "";
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof State)) return false;

		State state = (State) o;

		return new EqualsBuilder()
			.append(isClosed(), state.isClosed())
			.append(getDate(), state.getDate())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(getDate())
			.append(isClosed())
			.toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("date", date)
			.append("closed", closed)
			.toString();
	}
}

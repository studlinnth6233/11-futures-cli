package de.thro.inf.prg3.a11.openmensa.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Data transfer object for a canteen retrieved from the OpenMensaAPI
 *
 * @author Peter Kurfer
 */

public final class Canteen {
	private int id;
	private String name;
	private String city;
	private String address;
	private double[] coordinates;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double[] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(double[] coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public String toString() {
		return String.format("%s\t%s", getId(), getName());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (!(o instanceof Canteen)) return false;

		Canteen canteen = (Canteen) o;

		return new EqualsBuilder()
			.append(getId(), canteen.getId())
			.append(getName(), canteen.getName())
			.append(getCity(), canteen.getCity())
			.append(getAddress(), canteen.getAddress())
			.append(getCoordinates(), canteen.getCoordinates())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(getId())
			.append(getName())
			.append(getCity())
			.append(getAddress())
			.append(getCoordinates())
			.toHashCode();
	}
}

package de.thro.inf.prg3.a11.openmensa.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.LinkedList;
import java.util.List;

/**
 * Data transfer object for a meal retrieved from the OpenMensaAPI
 *
 * @author Peter Kurfer
 */

public final class Meal
{
	private int id;
	private String name;
	private String category;
	private List<String> notes;

	public Meal()
	{
		notes = new LinkedList<>();
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public List<String> getNotes()
	{
		return notes;
	}

	public void setNotes(List<String> notes)
	{
		this.notes = notes;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;

		if (!(o instanceof Meal)) return false;

		Meal meal = (Meal) o;

		return new EqualsBuilder()
			.append(getId(), meal.getId())
			.append(getName(), meal.getName())
			.append(getCategory(), meal.getCategory())
			.append(getNotes(), meal.getNotes())
			.isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder(17, 37)
			.append(getId())
			.append(getName())
			.append(getCategory())
			.append(getNotes())
			.toHashCode();
	}

	@Override
	public String toString()
	{
		return name;
	}
}

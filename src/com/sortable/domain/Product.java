package com.sortable.domain;

public class Product {

	private String name;
	private String manufacturer;
	private String family;
	private String model;
	private String announcedDate;
	
	public String getName() {
		return name;
	}
	
	public String getManufacturer() {
		return manufacturer;
	}
	
	public String getFamily() {
		return family;
	}
	
	public String getModel() {
		return model;
	}
	
	public boolean hasFamily() {
		return getFamily() != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}

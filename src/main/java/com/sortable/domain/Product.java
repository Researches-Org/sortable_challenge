package com.sortable.domain;

public class Product {

	private String product_name;
	private String manufacturer;
	private String family;
	private String model;
	private String announcedDate;

	public Product(String product_name, String manufacturer, String family,
			String model, String announcedDate) {
		this.product_name = product_name;
		this.manufacturer = manufacturer;
		this.family = family;
		this.model = model;
		this.announcedDate = announcedDate;
	}

	public String getName() {
		return product_name;
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

	public String getAnnouncedDate() {
		return announcedDate;
	}

	public boolean hasFamily() {
		return getFamily() != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((product_name == null) ? 0 : product_name.hashCode());
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
		if (product_name == null) {
			if (other.product_name != null)
				return false;
		} else if (!product_name.equals(other.product_name))
			return false;
		return true;
	}

}

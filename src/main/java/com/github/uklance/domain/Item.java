package com.github.uklance.domain;

public class Item {
	private String name;
	private double latitude;
	private double longitude;
	private ItemCategory category; 
	private long itemId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public ItemCategory getCategory() {
		return category;
	}
	public void setCategory(ItemCategory category) {
		this.category = category;
	}
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	public long getItemId() {
		return itemId;
	}
}

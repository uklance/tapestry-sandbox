package com.github.uklance.services;

import com.github.uklance.domain.ItemCategory;

public class ItemFilter {
	private Double latitude;
	private Double longitude;
	private ItemCategory itemCategory;
	private Double kms;
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public ItemCategory getItemCategory() {
		return itemCategory;
	}
	public void setItemCategory(ItemCategory itemCategory) {
		this.itemCategory = itemCategory;
	}
	public Double getKms() {
		return kms;
	}
	public void setKms(Double kms) {
		this.kms = kms;
	}
}

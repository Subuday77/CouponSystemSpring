package com.couponsystem.CouponSystemSpring.beans;

import javax.persistence.Id;


public enum Category {
	
	Food,
	Electricity,
	Restaurant,
	Vacation,
	undefined;
	private long id;
	
	@Id
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	
	@Override
	public String toString() {
		return "Category [id=" + id + "]";
	}
	
}

package com.couponsystem.CouponSystemSpring.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.cfg.annotations.HCANNHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.couponsystem.CouponSystemSpring.Help;

@Entity
@Table(name = "Companies")
@Component
@Scope(value="prototype")
public class Company {

	private long id;
	private String uid=Help.getUUID();
	private String name;
	private String email;
	private String password;
	private Collection<Coupon> coupons = new ArrayList<Coupon>();

	public Company() {

	}

	public Company(long id, String name, String email, String password, Collection<Coupon> coupons, String uid) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.coupons = coupons;
		this.uid = uid;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", updatable = false, nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@OneToMany(cascade = CascadeType.REMOVE)
	public Collection<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(Collection<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Column
	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", coupons="
				+ coupons + ", uid=" + uid + "]\n";
	}

}

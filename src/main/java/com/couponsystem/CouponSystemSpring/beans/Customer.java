package com.couponsystem.CouponSystemSpring.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.couponsystem.CouponSystemSpring.Help;

@Entity
@Table(name = "Customers")
@Component
@Scope(value="prototype")
public class Customer {

	private long id;
	private String uid = Help.getUUID();
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private Collection<Coupon> coupons = new ArrayList<Coupon>();
	public Customer() {
		// TODO Auto-generated constructor stub
	}
	public Customer(int id, String firstName, String lastName, String email, String password,
			List<Coupon> coupons, String uid) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.coupons = coupons;
		this.uid=uid;
	}
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	@Column(name = "ID", updatable = false, nullable = false)
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	@Column
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	@Column
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	@ManyToMany
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
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", password=" + password + ", coupons=" + coupons + "]";
	}
	
	
}

package com.couponsystem.CouponSystemSpring.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.couponsystem.CouponSystemSpring.beans.Category;
import com.couponsystem.CouponSystemSpring.beans.Company;
import com.couponsystem.CouponSystemSpring.beans.Coupon;
import com.couponsystem.CouponSystemSpring.beans.Customer;
import com.couponsystem.CouponSystemSpring.repo.CompanyRepo;
import com.couponsystem.CouponSystemSpring.repo.CouponRepo;
import com.couponsystem.CouponSystemSpring.repo.CustomerRepo;

@Repository
public class SystemDAO {
	@Autowired
	CompanyRepo companyRepo;
	@Autowired
	CouponRepo couponRepo;
	@Autowired
	CustomerRepo customerRepo;

	// Company
	public void addCompany(Company company) {
		companyRepo.save(company);
	}

	public Optional<Company> findCompanyById(long id) {
		return companyRepo.findById(id);
	}


	public Optional<Company> findCompanyByName(String name) {
		return companyRepo.findByName(name);
	}

	
	public Optional<Company> findCompanyByEmailOptional(String email) {
		return companyRepo.findByEmail(email);
	}
	
	public Optional<Company> findCompanyByString(String name, String email, String uid) {
		
		return companyRepo.findByNameOrEmailOrUid(name, email, uid);
	}

	public void deleteCompany(Company company) {
		companyRepo.delete(company);
	}


	public Collection<Company> getAllCompanies() {
		return companyRepo.findAll();
	}

	public void updateCompany(Company company) {
		companyRepo.save(company);
	}

	// Coupon
	public void addCoupon(Coupon coupon) {
		couponRepo.save(coupon);
	}

	public Optional<Coupon> findCouponById(long id) {
		return couponRepo.findById(id);
	}
	
	
	public Optional<Coupon> findCouponByUid(String uid) {
		return couponRepo.findByUid(uid);
	}

	public ArrayList<Coupon> findCouponsByCompanyId(Long id) {
		return (ArrayList<Coupon>) couponRepo.findByCompanyId(id);
	}

	public void deleteCoupon(Coupon coupon) {
		couponRepo.delete(coupon);
	}

	public Collection<Coupon> getAllCoupons() {
		return couponRepo.findAll();
	}
	
	public Collection<Coupon> findCouponsByCategoryAndCompanyId (Category category, long companyId) {
		return couponRepo.findByCategoryAndCompanyId(category, companyId);
	}
	
	public Collection<Coupon> findCouponsByPriceAndCompanyId (double price, long companyId){
		return couponRepo.findByPriceLessThanEqualAndCompanyId(price, companyId);
	}
	
	

	// Customer
	public void addCustomer(Customer customer) {
		customerRepo.save(customer);
	}

	public Optional<Customer> findCustomerById(long id) {
		return customerRepo.findById(id);
	}

	public Optional<Customer> findCustomerByEmailOptional(String email) {
		return customerRepo.findByEmail(email);
	}

	public Optional<Customer> findCustomerByUid(String uid) {
		return customerRepo.findByUid(uid);
	}

	public void deleteCustomer(Customer customer) {
		customerRepo.delete(customer);
	}

	public Collection<Customer> getAllCustomers() {
		return customerRepo.findAll();
	}

	public ArrayList<Customer> findCustomerByString(String firstName, String lastName, String email, String uid) {
		
		return (ArrayList<Customer>) customerRepo.findByFirstNameOrLastNameOrEmailOrUid (firstName, lastName,  email,  uid);
	}

	

	
	

}
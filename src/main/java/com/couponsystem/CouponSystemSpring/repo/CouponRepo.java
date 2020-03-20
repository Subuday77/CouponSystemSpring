package com.couponsystem.CouponSystemSpring.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.couponsystem.CouponSystemSpring.beans.Category;
import com.couponsystem.CouponSystemSpring.beans.Coupon;

@Repository
public interface CouponRepo extends JpaRepository<Coupon, Long>{

	Optional<Coupon> findByUid (String uid);
	List <Coupon> findByCompanyId (long id);
	List <Coupon> findByCategoryAndCompanyId (Category category, long companyId);
	List <Coupon> findByCategory (Category category);
	List <Coupon> findByPriceLessThanEqual (double price);
	List <Coupon> findByPriceLessThanEqualAndCompanyId (double price, long companyId);
	List <Coupon> findByPriceLessThanEqualAndCategoryAndCompanyId (double price, Category category, long companyId);
	List <Coupon> findByPriceLessThanEqualAndCategory (double price, Category category);

	
}


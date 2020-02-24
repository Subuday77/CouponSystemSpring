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
	List <Coupon> findByPriceLessThanEqualAndCompanyId (double price, long companyId);
	
	
//	@Query
//	("DELETE FROM companies_coupons WHERE coupons_id=?1")
//	@Modifying
//	void deleteFromCompanies_coupons (long id);
//	@Query
//	("DELETE FROM customers_coupons WHERE coupons_id=?1")
//	@Modifying
//	void deleteFromCustomers_coupons (long id);
//	@Query
//	("DELETE FROM coupons WHERE id=?1")
//	@Modifying
//	void deleteFromCoupons (long id);
	
}


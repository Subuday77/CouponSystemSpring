package com.couponsystem.CouponSystemSpring.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.couponsystem.CouponSystemSpring.beans.Company;


@Repository
public interface CompanyRepo extends JpaRepository<Company, Long>{
	
	Optional<List<Company>> findByIdOrNameOrEmailOrUid (long id, String name, String email, String uid);

	Optional <Company> findByName (String name);
	Optional<Company> findByEmail (String email);
	
}

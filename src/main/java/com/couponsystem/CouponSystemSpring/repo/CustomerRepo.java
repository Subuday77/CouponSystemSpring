package com.couponsystem.CouponSystemSpring.repo;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.couponsystem.CouponSystemSpring.beans.Customer;


@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {
	
	Optional<Customer> findByUid (String uid);
	Optional <Customer> findByEmail (String email); 
	Collection<Customer> findByIdOrFirstNameOrLastNameOrEmailOrUid (Long id, String firstName, String lastName, String email, String uid);

}

package com.couponsystem.CouponSystemSpring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.couponsystem.CouponSystemSpring.beans.Company;
import com.couponsystem.CouponSystemSpring.beans.Coupon;
import com.couponsystem.CouponSystemSpring.dao.SystemDAO;
import com.couponsystem.CouponSystemSpring.rest.AdminController;

@Component
@EnableAsync
public class ScheduledCleanUp {

	@Autowired
	SystemDAO systemDAO;
	@Autowired
	AdminController adminController;

	@Async
	@Scheduled(fixedRate = 1000*60*2)
	public void cleanUp() throws InterruptedException {
	//	 adminController.deleteOutdatedCoupons();
		
//		List<Company> allCompanies = (List<Company>) systemDAO.getAllCompanies();
//		for (Company company : allCompanies) {
//			List<Coupon> allCompanyCoupons = (List<Coupon>) company.getCoupons();
//			
//			Iterator <Coupon> iterator = allCompanyCoupons.iterator();
//			while(iterator.hasNext()) {
//				if (iterator.next().getId()==66) {
//					System.out.println(iterator);
//					iterator.remove();
//				}
//				
//			}
//					
//				}
			}
		

	}



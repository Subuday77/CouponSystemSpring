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
import com.couponsystem.CouponSystemSpring.beans.Login;
import com.couponsystem.CouponSystemSpring.dao.LoginDAO;
import com.couponsystem.CouponSystemSpring.dao.SystemDAO;
import com.couponsystem.CouponSystemSpring.repo.LoginRepo;
import com.couponsystem.CouponSystemSpring.rest.AdminController;

@Component
@EnableAsync
public class ScheduledCleanUp {

	@Autowired
	SystemDAO systemDAO;
	@Autowired
	LoginDAO loginDAO;
	@Autowired
	AdminController adminController;

	@Async
	@Scheduled(fixedRate = 1000*60)
	public void cleanUp() throws InterruptedException {
		
		for (Login login : loginDAO.getAllLogins()) {
			if (login.getTimestamp()<System.currentTimeMillis()-(1000*60*30)) {
				loginDAO.removeLogin(login);
			}
		}
		
	// adminController.deleteOutdatedCoupons();
		
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



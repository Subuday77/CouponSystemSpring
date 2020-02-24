package com.couponsystem.CouponSystemSpring;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.couponsystem.CouponSystemSpring.beans.Company;
import com.couponsystem.CouponSystemSpring.beans.Coupon;
import com.couponsystem.CouponSystemSpring.dao.SystemDAO;
import com.couponsystem.CouponSystemSpring.rest.CompanyController;

@Service
public class CleanUp {
	@Autowired
	SystemDAO systemDAO;
	@Autowired
	Help help;
	@Autowired
	CompanyController companyController;
	
	int sec = 60;
	int min = 2;

	public Runnable newRunnable() {
		return new Runnable() {

			public void run() {

				while (true) {
					
					List<Coupon> allCoupons = ((ArrayList<Coupon>) systemDAO.getAllCoupons());
					
//					if (allCoupons.size() > 0) {
//						for (Coupon coupon : allCoupons) {
//							if (coupon.getEndDate().before(new Date())) {
//								
//								systemDAO.deleteOutdatedCoupon(coupon.getId());
//																							
//								Optional<Company> company = systemDAO.findCompanyById(coupon.getCompanyId());
//								if (company.isPresent()) {
//									List<Coupon> list =  Collections.synchronizedList(company.get().getCoupons());
//									System.out.println(list.remove(coupon));
//									company.get().setCoupons(list);
//									systemDAO.updateCompany(company.get());
//
//									ArrayList<Customer> customers = (ArrayList<Customer>) systemDAO.getAllCustomers();
//									for (Customer c : customers) {
//										c.getCoupons().remove(coupon);
//										systemDAO.addCustomer(c);
//									}
//
//								}
//								systemDAO.deleteCoupon(coupon);
//							}
//						}
//					}
					try {
						Thread.sleep(1000*sec*min);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}
				}

			}

		};

	}
	
	
		
		//System.out.println(company.get().getCoupons().remove(coupon));
		
	}



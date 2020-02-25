package com.couponsystem.CouponSystemSpring.rest;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.couponsystem.CouponSystemSpring.Help;
import com.couponsystem.CouponSystemSpring.beans.Category;
import com.couponsystem.CouponSystemSpring.beans.Company;
import com.couponsystem.CouponSystemSpring.beans.Coupon;
import com.couponsystem.CouponSystemSpring.beans.Customer;
import com.couponsystem.CouponSystemSpring.dao.SystemDAO;
import com.fasterxml.jackson.core.sym.Name;

@RestController
@RequestMapping("/company")

public class CompanyController {

	@Autowired
	SystemDAO systemDAO;
	@Autowired
	Help help;

	@PostMapping("/addcoupon")
	public ResponseEntity<?> addCoupon(@RequestBody Coupon coupon, @RequestParam (name="token") UUID token) {
		
		Optional<Company> existingCompany = systemDAO.findCompanyById(coupon.getCompanyId());
		if (!help.allowed(token, existingCompany.get().getId())) {
			return new ResponseEntity<String>("Forbidden!", HttpStatus.FORBIDDEN);
		}
		if (existingCompany.isPresent()) {
			if (help.isUnique(coupon)) {
				systemDAO.addCoupon(coupon);
				systemDAO.findCompanyById(coupon.getCompanyId()).get().getCoupons().add(coupon);
				systemDAO.addCompany(systemDAO.findCompanyById(coupon.getCompanyId()).get());
				help.updateTimestamp(token);
				return new ResponseEntity<String>("Coupon with id="+coupon.getId()+" was added", HttpStatus.OK);
			}
			help.updateTimestamp(token);
			return new ResponseEntity<String>("Such coupon already exists", HttpStatus.IM_USED);
		}
		help.updateTimestamp(token);
		return new ResponseEntity<String>("No such company", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/findcouponsbycompanyid")
	public ResponseEntity<?> getCompanyCoupons(@RequestParam(name = "id") long id) {

		return new ResponseEntity<ArrayList<Coupon>>(systemDAO.findCouponsByCompanyId(id), HttpStatus.OK);
	}

	@PutMapping("/updatecoupon")
	public ResponseEntity<?> updateCoupon(@RequestBody Coupon coupon) {
		Optional<Coupon> existingCoupon = systemDAO.findCouponById(coupon.getId());
		if (existingCoupon.isPresent()) {
			if (help.isUnique(coupon)) {
				systemDAO.addCoupon(Help.compareFieldsCoupon(existingCoupon, coupon));
				return new ResponseEntity<String>("Coupon " + existingCoupon.get().getId() + " was updated",
						HttpStatus.OK);
			}
			return new ResponseEntity<String>(
					"Coupon with title *" + existingCoupon.get().getTitle() + "* already exists.", HttpStatus.IM_USED);
		}
		return new ResponseEntity<String>("No such coupon", HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/deletecoupon")
	public ResponseEntity<?> deleteCoupon(@RequestBody Coupon coupon) {
		Optional<Coupon> existingCoupon = systemDAO.findCouponById(coupon.getId());
		long id = coupon.getId();
		if (coupon.getId() > 0 && existingCoupon.isPresent()) {
			Optional<Company> company = systemDAO.findCompanyById(existingCoupon.get().getCompanyId());
			if (company.isPresent()) {
				company.get().getCoupons().remove(existingCoupon.get());
				systemDAO.updateCompany(company.get());
				ArrayList<Customer> customers = (ArrayList<Customer>) systemDAO.getAllCustomers();
				for (Customer c : customers) {
					c.getCoupons().remove(existingCoupon.get());
					systemDAO.addCustomer(c);
				}

			}
			systemDAO.deleteCoupon(coupon);
			existingCoupon = systemDAO.findCouponById(coupon.getId());
			if (existingCoupon.isEmpty()) {
				return new ResponseEntity<String>("Coupon (id=" + id + ") was deleted", HttpStatus.OK);
			}
		}
		return new ResponseEntity<String>("Something went wrong.", HttpStatus.EXPECTATION_FAILED);
	}

	@GetMapping("/findcouponsbycategory")
	public ResponseEntity<?> getCompanyCouponsByCategory(@RequestParam(name = "category") Category category,
			@RequestParam(name = "companyid") long companyId) {
		return new ResponseEntity<ArrayList<Coupon>>(
				(ArrayList<Coupon>) systemDAO.findCouponsByCategoryAndCompanyId(category, companyId), HttpStatus.OK);

	}

	@GetMapping("/findcouponsbyprice")
	public ResponseEntity<?> getCompanyCouponsByPrice(@RequestParam(name = "price") double price,
			@RequestParam(name = "companyid") long companyId) {
		return new ResponseEntity<ArrayList<Coupon>>(
				(ArrayList<Coupon>) systemDAO.findCouponsByPriceAndCompanyId(price, companyId), HttpStatus.OK);

	}

	@GetMapping("/getcompanybyid")
	public ResponseEntity<?> findCompanyById(@RequestParam(name = "id") long id) {
		Optional<Company> existingCompany = systemDAO.findCompanyById(id);
		if (existingCompany.isPresent()) {
			return new ResponseEntity<Company>(systemDAO.findCompanyById(id).get(), HttpStatus.OK);
		}
		return new ResponseEntity<String>("Company with id " + id + " not found.", HttpStatus.BAD_REQUEST);
	}
}

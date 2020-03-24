package com.couponsystem.CouponSystemSpring.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.naming.spi.DirStateFactory.Result;
import javax.print.DocFlavor.STRING;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.couponsystem.CouponSystemSpring.Help;
import com.couponsystem.CouponSystemSpring.beans.Category;
import com.couponsystem.CouponSystemSpring.beans.Coupon;
import com.couponsystem.CouponSystemSpring.beans.Customer;
import com.couponsystem.CouponSystemSpring.dao.SystemDAO;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/customer")

public class CustomerController {

	@Autowired
	SystemDAO systemDAO;
	@Autowired
	Help help;

	@PutMapping("/purchase")
	public ResponseEntity<?> purchaseCoupon(@RequestParam(name = "couponid") long couponId,
			@RequestParam(name = "customerid") long id, @RequestParam(name = "token") UUID token) {
		if (!help.allowed(token, id)) {
			return new ResponseEntity<String>("Forbidden!", HttpStatus.FORBIDDEN);
		}
		if (systemDAO.findCouponById(couponId).isPresent()) {
			if (systemDAO.findCouponById(couponId).get().getAmount() > 0) {
				if (systemDAO.findCouponById(couponId).get().getEndDate().after(new Date())) {
					boolean flag = true;
					for (Coupon c : systemDAO.findCustomerById(id).get().getCoupons()) {
						if (c.getId() == couponId) {
							flag = false;
							help.updateTimestamp(token);
							return new ResponseEntity<String>("You already have this coupon.",
									HttpStatus.I_AM_A_TEAPOT);
						}
					}
					if (flag) {
						Optional<Coupon> coupon = systemDAO.findCouponById(couponId);
						systemDAO.findCustomerById(id).get().getCoupons().add(coupon.get());
						coupon.get().setAmount(coupon.get().getAmount() - 1);

						systemDAO.addCoupon(coupon.get());
						help.updateTimestamp(token);
						return new ResponseEntity<String>("Coupon (" + coupon.get().getId() + ") was purchased",
								HttpStatus.OK);
					}
				}
				help.updateTimestamp(token);
				return new ResponseEntity<String>("The coupon expired.", HttpStatus.EXPECTATION_FAILED);
			}
			help.updateTimestamp(token);
			return new ResponseEntity<String>("You are too late. Nothing left.", HttpStatus.GONE);
		}
		help.updateTimestamp(token);
		return new ResponseEntity<String>("No such coupon", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getcoupons")
	public ResponseEntity<?> findCouponsByCustomerId(@RequestParam(name = "id") long id,
			@RequestParam(name = "token") UUID token) {
		if (!help.allowed(token, id)) {
			return new ResponseEntity<String>("Forbidden!", HttpStatus.FORBIDDEN);
		}
		help.updateTimestamp(token);
		return new ResponseEntity<List<Coupon>>((List<Coupon>) systemDAO.findCustomerById(id).get().getCoupons(),
				HttpStatus.OK);

	}

	@GetMapping("/getcouponsbycategory")
	public ResponseEntity<?> findCouponsByCategory(@RequestParam(name = "id") long id,
			@RequestParam(name = "category") Category category, @RequestParam(name = "token") UUID token) {
		if (!help.allowed(token, id)) {
			return new ResponseEntity<String>("Forbidden!", HttpStatus.FORBIDDEN);
		}
		List<Coupon> coupons = (List<Coupon>) systemDAO.findCustomerById(id).get().getCoupons();
		ArrayList<Coupon> result = new ArrayList<Coupon>();

		for (Coupon coupon : coupons) {
			if (coupon.getCategory().equals(category)) {
				result.add(coupon);
			}
		}
		if (result.size() > 0) {
			help.updateTimestamp(token);
			return new ResponseEntity<ArrayList<Coupon>>(result, HttpStatus.OK);

		}
		help.updateTimestamp(token);
		return new ResponseEntity<String>("There are no purchased coupons in this category", HttpStatus.OK);
	}

	@GetMapping("/getcouponsbyprice")
	public ResponseEntity<?> findCouponsByPrice(@RequestParam(name = "id") long id,
			@RequestParam(name = "price") double price, @RequestParam(name = "token") UUID token) {
		if (!help.allowed(token, id)) {
			return new ResponseEntity<String>("Forbidden!", HttpStatus.FORBIDDEN);
		}
		List<Coupon> coupons = (List<Coupon>) systemDAO.findCustomerById(id).get().getCoupons();
		ArrayList<Coupon> result = new ArrayList<Coupon>();

		for (Coupon coupon : coupons) {
			if (coupon.getPrice() <= price) {
				result.add(coupon);
			}
		}
		if (result.size() > 0) {
			help.updateTimestamp(token);
			return new ResponseEntity<ArrayList<Coupon>>(result, HttpStatus.OK);

		}
		help.updateTimestamp(token);
		return new ResponseEntity<String>("There are no purchased coupons in this bounds", HttpStatus.OK);
	}

	@GetMapping("/findcouponsbypriceandcategory")
	public ResponseEntity<?> getCustomerCouponsByPriceAndCategory(@RequestParam(name = "id") long id,
			@RequestParam(name = "price") double price, @RequestParam(name = "category") Category category,
			@RequestParam(name = "token") UUID token) {
		if (!help.allowed(token, id)) {
			return new ResponseEntity<String>("Forbidden!", HttpStatus.FORBIDDEN);
		}
		if (category == null && price <= 0) {
			help.updateTimestamp(token);
			return new ResponseEntity<List<Coupon>>((List<Coupon>) systemDAO.findCustomerById(id).get().getCoupons(),
					HttpStatus.OK);
		}

		if (category == Category.undefined) {
			List<Coupon> coupons = (List<Coupon>) systemDAO.findCustomerById(id).get().getCoupons();
			ArrayList<Coupon> result = new ArrayList<Coupon>();

			for (Coupon coupon : coupons) {
				if (coupon.getPrice() <= price) {
					result.add(coupon);
				}
			}
			if (result.size() > 0) {
				help.updateTimestamp(token);
				return new ResponseEntity<ArrayList<Coupon>>(result, HttpStatus.OK);

			}
			help.updateTimestamp(token);
			return new ResponseEntity<String>("There are no purchased coupons in this bounds", HttpStatus.OK);

		}
		if (price <= 0) {
			List<Coupon> coupons = (List<Coupon>) systemDAO.findCustomerById(id).get().getCoupons();
			ArrayList<Coupon> result = new ArrayList<Coupon>();

			for (Coupon coupon : coupons) {
				if (coupon.getCategory().equals(category)) {
					result.add(coupon);
				}
			}
			if (result.size() > 0) {
				help.updateTimestamp(token);
				return new ResponseEntity<ArrayList<Coupon>>(result, HttpStatus.OK);

			}
			help.updateTimestamp(token);
			return new ResponseEntity<String>("There are no purchased coupons in this category", HttpStatus.OK);
		}

		ArrayList<Coupon> allCoupons = (ArrayList<Coupon>) systemDAO.findByPriceAndCategory(price, category);
		ArrayList<Coupon> result = new ArrayList<Coupon>();
		List<Coupon> coupons = (List<Coupon>) systemDAO.findCustomerById(id).get().getCoupons();
		for (Coupon coupon : allCoupons) {
			for (Coupon c : coupons) {
				if (coupon.equals(c)) {
					result.add(c);
				}
			}
		}
		help.updateTimestamp(token);
		return new ResponseEntity<ArrayList<Coupon>>(result, HttpStatus.OK);
	}

	@GetMapping("/findallcouponsbypriceandcategory")
	public ResponseEntity<?> getCouponsByPriceAndCategory(@RequestParam(name = "id") long id,
			@RequestParam(name = "price") double price, @RequestParam(name = "category") Category category,
			@RequestParam(name = "token") UUID token) {
		if (!help.allowed(token, id)) {
			return new ResponseEntity<String>("Forbidden!", HttpStatus.FORBIDDEN);
		}
		if (category == Category.undefined && price <= 0) {
			help.updateTimestamp(token);
			return new ResponseEntity<ArrayList<Coupon>>((ArrayList<Coupon>) systemDAO.getAllCoupons(), HttpStatus.OK);
		}
		if (category == Category.undefined) {
			help.updateTimestamp(token);
			return new ResponseEntity<ArrayList<Coupon>>((ArrayList<Coupon>) systemDAO.findByPrice(price),
					HttpStatus.OK);
		}
		if (price <= 0) {
			help.updateTimestamp(token);
			return new ResponseEntity<ArrayList<Coupon>>((ArrayList<Coupon>) systemDAO.findByCategory(category),
					HttpStatus.OK);
		}

		help.updateTimestamp(token);
		return new ResponseEntity<ArrayList<Coupon>>(
				(ArrayList<Coupon>) systemDAO.findByPriceAndCategory(price, category), HttpStatus.OK);
	}

	@GetMapping("/getcustomerbyid")
	public ResponseEntity<?> findCustomerById(@RequestParam(name = "id") long id,
			@RequestParam(name = "token") UUID token) {
		if (!help.allowed(token, id)) {
			return new ResponseEntity<String>("Forbidden!", HttpStatus.FORBIDDEN);
		}
		Optional<Customer> existingCustomer = systemDAO.findCustomerById(id);
		if (existingCustomer.isPresent()) {
			help.updateTimestamp(token);
			return new ResponseEntity<Customer>(systemDAO.findCustomerById(id).get(), HttpStatus.OK);
		}
		help.updateTimestamp(token);
		return new ResponseEntity<String>("Customer with id " + id + " not found.", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getallcoupons")
	public ResponseEntity<?> getAllCouponsEntity(@RequestParam(name = "id") long id,
			@RequestParam(name = "token") UUID token) {
		if (!help.allowed(token, id)) {
			return new ResponseEntity<String>("Forbidden!", HttpStatus.FORBIDDEN);
		}
		help.updateTimestamp(token);
		return new ResponseEntity<ArrayList<Coupon>>((ArrayList<Coupon>) systemDAO.getAllCoupons(), HttpStatus.OK);
	}
	@PutMapping("/updatecustomer")
	public ResponseEntity<?> updateCustomer(@RequestBody Customer customer, @RequestParam(name = "token") UUID token) {
		if (!help.allowed(token, customer.getId())) {
			return new ResponseEntity<String>("Forbidden!", HttpStatus.FORBIDDEN);
		}
		Optional<Customer> existingCustomer = systemDAO.findCustomerById(customer.getId());
		if (existingCustomer.isPresent()) {
			if (help.isUnique(customer)) {
				systemDAO.addCustomer(Help.compareFieldsCustomer(existingCustomer, customer));
				help.updateTimestamp(token);
				return new ResponseEntity<String>("Customer " + existingCustomer.get().getLastName() + " (id="
						+ customer.getId() + ")" + " updated", HttpStatus.OK);
			}
			help.updateTimestamp(token);
			return new ResponseEntity<String>("Email already in use", HttpStatus.IM_USED);
		}
		help.updateTimestamp(token);
		return new ResponseEntity<String>("Customer with id " + customer.getId() + " not found.",
				HttpStatus.BAD_REQUEST);
	}

}

package com.couponsystem.CouponSystemSpring.rest;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import net.bytebuddy.dynamic.loading.PackageDefinitionStrategy.Definition.Undefined;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/company")

public class CompanyController {

	@Autowired
	SystemDAO systemDAO;
	@Autowired
	Help help;

	@PostMapping("/addcoupon")
	public ResponseEntity<?> addCoupon(@RequestBody Coupon coupon, @RequestParam(name = "token") UUID token) {

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
				return new ResponseEntity<String>("Coupon with id=" + coupon.getId() + " was added", HttpStatus.OK);
			}
			help.updateTimestamp(token);
			return new ResponseEntity<String>("Such coupon already exists", HttpStatus.IM_USED);
		}
		help.updateTimestamp(token);
		return new ResponseEntity<String>("No such company", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/findcouponsbycompanyid")
	public ResponseEntity<?> getCompanyCoupons(@RequestParam(name = "id") long id,
			@RequestParam(name = "token") UUID token) {
		if (!help.allowed(token, id)) {
			return new ResponseEntity<String>("Forbidden!", HttpStatus.FORBIDDEN);
		}
		help.updateTimestamp(token);
		return new ResponseEntity<ArrayList<Coupon>>(systemDAO.findCouponsByCompanyId(id), HttpStatus.OK);
	}
	@GetMapping("/findcouponbyid")
	public ResponseEntity<?> getCouponById(@RequestParam(name = "id") long id, @RequestParam(name = "companyid") long companyid,
			@RequestParam(name = "token") UUID token) {
		if (!help.allowed(token, companyid)) {
			return new ResponseEntity<String>("Forbidden!", HttpStatus.FORBIDDEN);
		}
		help.updateTimestamp(token);
		return new ResponseEntity <Optional<Coupon>> (systemDAO.findCouponById(id), HttpStatus.OK);
	}
	

	@PutMapping("/updatecoupon")
	public ResponseEntity<?> updateCoupon(@RequestBody Coupon coupon, @RequestParam(name = "token") UUID token) {
		Optional<Coupon> existingCoupon = systemDAO.findCouponById(coupon.getId());
		if (!help.allowed(token, existingCoupon.get().getCompanyId())) {
			return new ResponseEntity<String>("Forbidden!", HttpStatus.FORBIDDEN);
		}
		if (existingCoupon.isPresent()) {
			if (help.isUnique(coupon)) {
				systemDAO.addCoupon(Help.compareFieldsCoupon(existingCoupon, coupon));
				help.updateTimestamp(token);
				return new ResponseEntity<String>("Coupon " + existingCoupon.get().getId() + " was updated",
						HttpStatus.OK);
			}
			help.updateTimestamp(token);
			return new ResponseEntity<String>(
					"Coupon with title *" + existingCoupon.get().getTitle() + "* already exists.", HttpStatus.IM_USED);
		}
		help.updateTimestamp(token);
		return new ResponseEntity<String>("No such coupon", HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/deletecoupon")
	public ResponseEntity<?> deleteCoupon(@RequestParam long id, @RequestParam(name = "token") UUID token) {
		if (systemDAO.findCouponById(id).isEmpty()) {
			return new ResponseEntity<String>("No such coupon", HttpStatus.NOT_FOUND);
		}

		Optional<Coupon> existingCoupon = systemDAO.findCouponById(id);
		if (!help.allowed(token, existingCoupon.get().getCompanyId())) {
			return new ResponseEntity<String>("Forbidden!", HttpStatus.FORBIDDEN);
		}

		if (id > 0 && existingCoupon.isPresent()) {
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
			systemDAO.deleteCoupon(systemDAO.findCouponById(id).get());
			existingCoupon = systemDAO.findCouponById(id);
			if (existingCoupon.isEmpty()) {
				help.updateTimestamp(token);
				return new ResponseEntity<String>("Coupon (id=" + id + ") was deleted", HttpStatus.OK);
			}
		}
		help.updateTimestamp(token);
		return new ResponseEntity<String>("Something went wrong.", HttpStatus.EXPECTATION_FAILED);
	}

	@GetMapping("/findcouponsbycategory")
	public ResponseEntity<?> getCompanyCouponsByCategory(@RequestParam(name = "category") Category category,
			@RequestParam(name = "companyid") long companyId, @RequestParam(name = "token") UUID token) {
		if (!help.allowed(token, companyId)) {
			return new ResponseEntity<String>("Forbidden!", HttpStatus.FORBIDDEN);
		}
		help.updateTimestamp(token);
		return new ResponseEntity<ArrayList<Coupon>>(
				(ArrayList<Coupon>) systemDAO.findCouponsByCategoryAndCompanyId(category, companyId), HttpStatus.OK);

	}

	@GetMapping("/findcouponsbyprice")
	public ResponseEntity<?> getCompanyCouponsByPrice(@RequestParam(name = "price") double price,
			@RequestParam(name = "companyid") long companyId, @RequestParam(name = "token") UUID token) {
		if (!help.allowed(token, companyId)) {
			return new ResponseEntity<String>("Forbidden!", HttpStatus.FORBIDDEN);
		}
		help.updateTimestamp(token);
		return new ResponseEntity<ArrayList<Coupon>>(
				(ArrayList<Coupon>) systemDAO.findCouponsByPriceAndCompanyId(price, companyId), HttpStatus.OK);

	}

	@GetMapping("/findcouponsbypriceandcategory")
	public ResponseEntity<?> getCompanyCouponsByPriceAndCategory(@RequestParam(name = "price") double price,
			@RequestParam(name = "category") Category category, @RequestParam(name = "companyid") long companyId,
			@RequestParam(name = "token") UUID token) {
		if (!help.allowed(token, companyId)) {
			return new ResponseEntity<String>("Forbidden!", HttpStatus.FORBIDDEN);
		}
		help.updateTimestamp(token);
		if (category == null) {
			return new ResponseEntity<ArrayList<Coupon>>(
					(ArrayList<Coupon>) systemDAO.findCouponsByPriceAndCompanyId(price, companyId), HttpStatus.OK);
		}
		if (price <= 0) {
			return new ResponseEntity<ArrayList<Coupon>>(
					(ArrayList<Coupon>) systemDAO.findCouponsByCategoryAndCompanyId(category, companyId),
					HttpStatus.OK);
		}
		return new ResponseEntity<ArrayList<Coupon>>(
				(ArrayList<Coupon>) systemDAO.findCouponsByPriceAndCategoryAndCompanyId(price, category, companyId),
				HttpStatus.OK);
	}

	@GetMapping("/getcompanybyid")
	public ResponseEntity<?> findCompanyById(@RequestParam(name = "id") long id,
			@RequestParam(name = "token") UUID token) {
		Optional<Company> existingCompany = systemDAO.findCompanyById(id);
		if (!help.allowed(token, existingCompany.get().getId())) {
			return new ResponseEntity<String>("Forbidden!", HttpStatus.FORBIDDEN);
		}
		if (existingCompany.isPresent()) {
			help.updateTimestamp(token);
			return new ResponseEntity<Company>(systemDAO.findCompanyById(id).get(), HttpStatus.OK);
		}
		help.updateTimestamp(token);
		return new ResponseEntity<String>("Company with id " + id + " not found.", HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/updatecompany")
	public ResponseEntity<?> updateCompany(@RequestBody Company company, 
			@RequestParam(name = "token") UUID token) {
		Optional<Company> existingCompany = systemDAO.findCompanyById(company.getId());
		if (!help.allowed(token, existingCompany.get().getId())) {
			return new ResponseEntity<String>("Forbidden!", HttpStatus.FORBIDDEN);
		}
		if (existingCompany.isPresent()) {
			if (help.isUnique(company)) {
				systemDAO.addCompany(Help.compareFieldsCompany(existingCompany, company));
				help.updateTimestamp(token);
				return new ResponseEntity<String>(
						"Company " + existingCompany.get().getName() + " (id=" + company.getId() + ")" + " updated",
						HttpStatus.OK);
			}
			help.updateTimestamp(token);
			return new ResponseEntity<String>("Name or Email already in use", HttpStatus.IM_USED);
		}
		help.updateTimestamp(token);
		return new ResponseEntity<String>("Company with id " + company.getId() + " not found.", HttpStatus.BAD_REQUEST);
	}
}

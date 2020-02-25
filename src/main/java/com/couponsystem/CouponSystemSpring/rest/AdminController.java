package com.couponsystem.CouponSystemSpring.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import com.couponsystem.CouponSystemSpring.beans.Company;
import com.couponsystem.CouponSystemSpring.beans.Coupon;
import com.couponsystem.CouponSystemSpring.beans.Customer;
import com.couponsystem.CouponSystemSpring.dao.SystemDAO;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	SystemDAO systemDAO;
	@Autowired
	Help help;

	// Companies

	@PostMapping("/addcompany")
	public ResponseEntity<?> addCompany(@RequestBody Company company) {

		if (help.isUnique(company)) {
			systemDAO.addCompany(company);
			return new ResponseEntity<String>("Company with id=" + company.getId() + " was added", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Name or Email already in use", HttpStatus.IM_USED);
	}

	@GetMapping("/getcompanybyid")
	public ResponseEntity<?> findCompanyById(@RequestParam(name = "id") long id) {
		Optional<Company> existingCompany = systemDAO.findCompanyById(id);
		if (existingCompany.isPresent()) {
			return new ResponseEntity<Company>(systemDAO.findCompanyById(id).get(), HttpStatus.OK);
		}
		return new ResponseEntity<String>("Company with id " + id + " not found.", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getcompany")
	public ResponseEntity<?> findCompanyByString(@RequestParam(name = "name") String name,
			@RequestParam(name = "email") String email, @RequestParam(name = "uid") String uid) {
		Optional<Company> existingCompany = systemDAO.findCompanyByString(name, email, uid);
		if (existingCompany.isPresent()) {
			return new ResponseEntity<Company>(systemDAO.findCompanyByString(name, email, uid).get(), HttpStatus.OK);
		}
		return new ResponseEntity<String>("Company not found.", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/name")
	public ResponseEntity<?> findCompanyByName(@RequestParam(name = "name") String name) {

		Optional<Company> existingCompany = systemDAO.findCompanyByName(name);

		if (existingCompany.isEmpty()) {
			return new ResponseEntity<String>("Company not found.", HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<Optional<Company>>(systemDAO.findCompanyByName(name), HttpStatus.OK);
	}

	@PutMapping("/updatecompany")
	public ResponseEntity<?> updateCompany(@RequestBody Company company) {

		Optional<Company> existingCompany = systemDAO.findCompanyById(company.getId());
		if (existingCompany.isPresent()) {
			if (help.isUnique(company)) {
				systemDAO.addCompany(Help.compareFieldsCompany(existingCompany, company));

				return new ResponseEntity<String>(
						"Company " + existingCompany.get().getName() + " (id=" + company.getId() + ")" + " updated",
						HttpStatus.OK);
			}
			return new ResponseEntity<String>("Name or Email already in use", HttpStatus.IM_USED);
		}
		return new ResponseEntity<String>("Company with id " + company.getId() + " not found.", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getallcompanies")
	public ResponseEntity<?> getAllCompanies() {
		return new ResponseEntity<ArrayList<Company>>((ArrayList<Company>) systemDAO.getAllCompanies(), HttpStatus.OK);
	}

	@DeleteMapping("/deletecompany")
	public ResponseEntity<?> deleteCompany(@RequestBody Company company) {
		Optional<Company> existingCompany = systemDAO.findCompanyById(company.getId());
		long id = company.getId();
		if (company.getId() > 0 && existingCompany.isPresent()) {

			ArrayList<Coupon> coupons = systemDAO.findCouponsByCompanyId(id);
			ArrayList<Customer> customers = (ArrayList<Customer>) systemDAO.getAllCustomers();
			for (Customer customer : customers) {
				int counter = customer.getCoupons().size();
				for (int i = 0; i < counter; i++) {
					List<Coupon> coup = (List<Coupon>) customer.getCoupons();
					Coupon c = coup.get(0);
					if (c.getCompanyId() == id) {
						customer.getCoupons().remove(c);
						systemDAO.addCustomer(customer);
					}
				}

			}

			systemDAO.deleteCompany(company);
			for (Coupon coupon : coupons) {

				systemDAO.deleteCoupon(coupon);
			}
			existingCompany = systemDAO.findCompanyById(company.getId());
			if (!existingCompany.isPresent()) {
				return new ResponseEntity<String>("Company (id=" + id + ") was deleted", HttpStatus.OK);
			}
		}
		return new ResponseEntity<String>("Something went wrong.", HttpStatus.EXPECTATION_FAILED);
	}

	// Customers

	@GetMapping("/getcustomerbyid")
	public ResponseEntity<?> findCustomerById(@RequestParam(name = "id") long id) {
		Optional<Customer> existingCustomer = systemDAO.findCustomerById(id);
		if (existingCustomer.isPresent()) {
			return new ResponseEntity<Customer>(systemDAO.findCustomerById(id).get(), HttpStatus.OK);
		}
		return new ResponseEntity<String>("Customer with id " + id + " not found.", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getcustomers")
	public ResponseEntity<?> findCustomersByString(@RequestParam(name = "firstname") String firstName,
			@RequestParam(name = "lastname") String lastName, @RequestParam(name = "email") String email,
			@RequestParam(name = "uid") String uid) {
		ArrayList<Customer> existingCustomers = systemDAO.findCustomerByString(firstName, lastName, email, uid);
		if (!existingCustomers.isEmpty()) {
			return new ResponseEntity<ArrayList<Customer>>(
					systemDAO.findCustomerByString(firstName, lastName, email, uid), HttpStatus.OK);
		}
		return new ResponseEntity<String>("Customer(s) not found.", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getallcustomers")
	public ResponseEntity<?> getAllCustomers() {
		return new ResponseEntity<ArrayList<Customer>>((ArrayList<Customer>) systemDAO.getAllCustomers(),
				HttpStatus.OK);
	}

	@PostMapping("/addcustomer")
	public ResponseEntity<?> addCustomer(@RequestBody Customer customer) {

		if (help.isUnique(customer)) {
			systemDAO.addCustomer(customer);
			return new ResponseEntity<String>("OK", HttpStatus.OK);
		}
		return new ResponseEntity<String>("Email already in use", HttpStatus.IM_USED);
	}

	@PutMapping("/updatecustomer")
	public ResponseEntity<?> updateCustomer(@RequestBody Customer customer) {

		Optional<Customer> existingCustomer = systemDAO.findCustomerById(customer.getId());
		if (existingCustomer.isPresent()) {
			if (help.isUnique(customer)) {
				systemDAO.addCustomer(Help.compareFieldsCustomer(existingCustomer, customer));

				return new ResponseEntity<String>("Customer " + existingCustomer.get().getLastName() + " (id="
						+ customer.getId() + ")" + " updated", HttpStatus.OK);
			}
			return new ResponseEntity<String>("Email already in use", HttpStatus.IM_USED);
		}
		return new ResponseEntity<String>("Customer with id " + customer.getId() + " not found.",
				HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/deletecustomer")
	public ResponseEntity<?> deleteCustomer(@RequestBody Customer customer) {
		Optional<Customer> existingCustomer = systemDAO.findCustomerById(customer.getId());
		long id = customer.getId();
		if (customer.getId() > 0 && existingCustomer.isPresent()) {
			systemDAO.deleteCustomer(customer);
			existingCustomer = systemDAO.findCustomerById(customer.getId());
			if (existingCustomer.isEmpty()) {
				return new ResponseEntity<String>("Customer(id=" + id + ") was deleted", HttpStatus.OK);
			}
		}
		return new ResponseEntity<String>("Something went wrong.", HttpStatus.EXPECTATION_FAILED);
	}

	// Coupons
	@GetMapping("/getallcoupons")
	public ResponseEntity<?> getAllCouponsEntity() {
		return new ResponseEntity<ArrayList<Coupon>>((ArrayList<Coupon>) systemDAO.getAllCoupons(), HttpStatus.OK);
	}

	@DeleteMapping("/cleanup")
	public synchronized ResponseEntity<?> deleteOutdatedCoupons() {
		System.out.println(Thread.currentThread());
		ArrayList<Customer> allCustomers = (ArrayList<Customer>) systemDAO.getAllCustomers();
		ArrayList<Coupon> allCoupons = (ArrayList<Coupon>) systemDAO.getAllCoupons();
		ArrayList<Coupon> outdatedCoupons = new ArrayList<Coupon>();
		for (Coupon coupon : allCoupons) {
			if (coupon.getEndDate().before(new Date())) {
				outdatedCoupons.add(coupon);
			}
		}
		if (!outdatedCoupons.isEmpty()) {
			for (Customer customer : allCustomers) {
				for (Coupon c : outdatedCoupons) {
					if (customer.getCoupons().contains(c)) {
						customer.getCoupons().remove(c);
						systemDAO.addCustomer(customer);
					}
				}
			}
			for (Coupon c : outdatedCoupons) {
				Optional<Company> company = systemDAO.findCompanyById(c.getCompanyId());
				company.get().getCoupons().remove(c);
				systemDAO.addCompany(company.get());
			}
			for (Coupon c : outdatedCoupons) {
				systemDAO.deleteCoupon(c);
			}
			return new ResponseEntity<String>("Outdated coupons were deleted", HttpStatus.OK);
		}
		return new ResponseEntity<String>("There were no outdated coupons", HttpStatus.GONE);
	}

}

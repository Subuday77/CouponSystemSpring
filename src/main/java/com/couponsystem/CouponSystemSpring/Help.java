package com.couponsystem.CouponSystemSpring;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.couponsystem.CouponSystemSpring.beans.Company;
import com.couponsystem.CouponSystemSpring.beans.Coupon;
import com.couponsystem.CouponSystemSpring.beans.Customer;
import com.couponsystem.CouponSystemSpring.beans.Login;
import com.couponsystem.CouponSystemSpring.dao.LoginDAO;
import com.couponsystem.CouponSystemSpring.dao.SystemDAO;
import com.couponsystem.CouponSystemSpring.repo.CompanyRepo;

@Component
public class Help {

	@Autowired
	SystemDAO systemDAO;
	@Autowired
	LoginDAO loginDAO;

	public static String getUUID() {
		return String.valueOf(UUID.randomUUID());
	}

	public static Company compareFieldsCompany(Optional<Company> existingCompany, Company company) {

		Company tempCompany = new Company();

		for (Field field : company.getClass().getDeclaredFields()) {

			field.setAccessible(true);
			try {
				switch (field.getName()) {
				case "id":

					tempCompany.setId(existingCompany.get().getId());

					break;

				case "uid":

					tempCompany.setUid(existingCompany.get().getUid());
					break;

				case "name":

					if (field.get(company) == null) {

						tempCompany.setName(existingCompany.get().getName());

					} else {
						tempCompany.setName(company.getName());
					}
					break;

				case "email":
					if (field.get(company) == null) {
						tempCompany.setEmail(existingCompany.get().getEmail());
					} else {
						tempCompany.setEmail(company.getEmail());
					}
					break;

				case "password":
					if (field.get(company) == null) {
						tempCompany.setPassword(existingCompany.get().getPassword());
					} else {
						tempCompany.setPassword(company.getPassword());
					}
					break;

				case "coupons":
					tempCompany.setCoupons(existingCompany.get().getCoupons());
					break;

				default:
					break;
				}

			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return tempCompany;
	}

	public static Customer compareFieldsCustomer(Optional<Customer> existingCustomer, Customer customer) {

		Customer tempCustomer = new Customer();

		for (Field field : customer.getClass().getDeclaredFields()) {

			field.setAccessible(true);
			try {
				switch (field.getName()) {
				case "id":

					tempCustomer.setId(existingCustomer.get().getId());

					break;

				case "uid":

					tempCustomer.setUid(existingCustomer.get().getUid());
					break;

				case "firstName":

					if (field.get(customer) == null) {

						tempCustomer.setFirstName(existingCustomer.get().getFirstName());

					} else {
						tempCustomer.setFirstName(customer.getFirstName());
					}
					break;

				case "lastName":

					if (field.get(customer) == null) {

						tempCustomer.setLastName(existingCustomer.get().getLastName());

					} else {
						tempCustomer.setLastName(customer.getLastName());
					}
					break;

				case "email":
					if (field.get(customer) == null) {
						tempCustomer.setEmail(existingCustomer.get().getEmail());
					} else {
						tempCustomer.setEmail(customer.getEmail());
					}
					break;

				case "password":
					if (field.get(customer) == null) {
						tempCustomer.setPassword(existingCustomer.get().getPassword());
					} else {
						tempCustomer.setPassword(customer.getPassword());
					}
					break;

				case "coupons":
					tempCustomer.setCoupons(existingCustomer.get().getCoupons());
					break;

				default:
					break;
				}

			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return tempCustomer;
	}

	public static Coupon compareFieldsCoupon(Optional<Coupon> existingCoupon, Coupon coupon) {

		Coupon tempCoupon = new Coupon();

		for (Field field : coupon.getClass().getDeclaredFields()) {

			field.setAccessible(true);
			try {
				switch (field.getName()) {
				case "id":

					tempCoupon.setId(existingCoupon.get().getId());

					break;

				case "uid":

					tempCoupon.setUid(existingCoupon.get().getUid());
					break;

				case "companyId":

					tempCoupon.setCompanyId(existingCoupon.get().getCompanyId());

					break;

				case "title":

					if (field.get(coupon) == null) {

						tempCoupon.setTitle(existingCoupon.get().getTitle());

					} else {
						tempCoupon.setTitle(coupon.getTitle());
					}
					break;

				case "description":

					if (field.get(coupon) == null) {

						tempCoupon.setDescription(existingCoupon.get().getDescription());

					} else {
						tempCoupon.setDescription(coupon.getDescription());
					}
					break;

				case "startDate":
					if (field.get(coupon) == null) {

						tempCoupon.setStartDate(existingCoupon.get().getStartDate());

					} else {
						tempCoupon.setStartDate(coupon.getStartDate());
					}
					break;
				case "endDate":
					if (field.get(coupon) == null) {

						tempCoupon.setEndDate(existingCoupon.get().getEndDate());

					} else {
						tempCoupon.setEndDate(coupon.getEndDate());
					}
					break;
				case "amount":
					if (field.get(coupon) == null) {

						tempCoupon.setAmount(existingCoupon.get().getAmount());

					} else {
						tempCoupon.setAmount(coupon.getAmount());
					}
					break;

				case "price":
					if (field.get(coupon) == null) {

						tempCoupon.setPrice(existingCoupon.get().getPrice());

					} else {
						tempCoupon.setPrice(coupon.getPrice());
					}
					break;

				case "image":
					if (field.get(coupon) == null) {

						tempCoupon.setImage(existingCoupon.get().getImage());

					} else {
						tempCoupon.setImage(coupon.getImage());
					}
					break;

				case "category":
					if (field.get(coupon) == null) {

						tempCoupon.setCategory(existingCoupon.get().getCategory());

					} else {
						tempCoupon.setCategory(coupon.getCategory());
					}
					break;

				default:
					break;
				}

			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return tempCoupon;
	}

	public boolean isUnique(Object object) {

		String type = object.getClass().getSimpleName();

		switch (type) {
		case "Company":
			Company tempCompany = (Company) object;
			Optional<Company> existingCompany = systemDAO.findCompanyByName(tempCompany.getName());
			if (existingCompany.isPresent()) {
				return false;
			}
			existingCompany = systemDAO.findCompanyByEmailOptional(tempCompany.getEmail());
			if (existingCompany.isPresent()) {
				return false;
			}
			return true;
		case "Customer":
			Customer tempCustomer = (Customer) object;

			Optional<Customer> existingCustomer = systemDAO.findCustomerByEmailOptional(tempCustomer.getEmail());
			if (existingCustomer.isPresent()) {
				return false;
			}
			return true;
		case "Coupon":
			Coupon tempCoupon = (Coupon) object;

			ArrayList<Coupon> existingCoupons = systemDAO.findCouponsByCompanyId(tempCoupon.getCompanyId());
			for (Coupon coupon : existingCoupons) {
				if (coupon.getTitle().equalsIgnoreCase(tempCoupon.getTitle())) {

					return false;
				}
			}

			return true;
		default:
			break;
		}
		return false;
	}

	public boolean isValid(String email, String password, int type) {

		switch (type) {
		case 1:
			ArrayList<Company> allCompanies = (ArrayList<Company>) systemDAO.getAllCompanies();
			for (Company company : allCompanies) {
				if (company.getEmail().equalsIgnoreCase(email)) {
					if (company.getPassword().equals(password)) {
						return true;
					}
				}
			}
			return false;

		case 2:
			ArrayList<Customer> allCustomers = (ArrayList<Customer>) systemDAO.getAllCustomers();
			for (Customer customer : allCustomers) {
				if (customer.getEmail().equalsIgnoreCase(email)) {
					if (customer.getPassword().equals(password)) {
						return true;
					}
				}
			}
			return false;

		default:
			return false;
		}
	}

	public boolean allowed(UUID token, long id) {
		if (loginDAO.findLoginByToken(token).isEmpty()) {
			return false;
		}
		return (loginDAO.findLoginByToken(token).get().getSubjectId() == id);
	}

	public void updateTimestamp(UUID token) {
		Optional<Login> login = loginDAO.findLoginByToken(token);
		login.get().setTimestamp(System.currentTimeMillis());
		loginDAO.addLogin(login.get());
	}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.controller;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.util.ControllerBean;
import cz.fi.muni.eshop.util.DataGenerator;
import cz.fi.muni.eshop.util.EntityValidator;
import cz.fi.muni.eshop.util.Identity;

/**
 * 
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Model
public class CustomerBean {
	private String email;
	private String name;
	private String password;

	@Inject
	private ControllerBean controller;
	@Inject
	private CustomerManager customerManager;
	@Inject
	private FacesContext facesContext;
	@Inject
	private Identity identity;
	private List<Customer> customers;
	@Inject
	private Logger log;
	@Inject
	private DataGenerator dataGenerator;

	@Inject
	private EntityValidator<Customer> validator;

	public void register() {
		if (validate()) {
			identity.logIn(customerManager.addCustomer(email, name, password));
			clearBean();
		}
	}

	public List<Customer> getCustomers() {
		return customerManager.getCustomers();
	}

	private void clearBean() {
		email = "";
		name = "";
		password = "";
	}

	public void addCustomer() {
		if (validate()) {
			customerManager.addCustomer(email, name, password);
			clearBean();
		}
	}

	public void logIn() {
		log.warning("trying to log in: " + "email=" + email + " password="
				+ password);
		this.name = "dummy";
		if (validate()) {
			identity.logIn(customerManager.verifyCustomer(email, password));
			clearBean();
		} else {
			addMessage("Unable to verify customer");
			clearBean();
		}
	}

	public void logOut() {
		identity.logOut();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void clearCustomers() {
		log.warning("Clearing customers data with all orders and invoices");
		addMessage("Clearing customers data with all orders and invoices");
		customerManager.clearCustomersTable();
	}

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void deleteCustomer(String email) {
		customerManager.deleteCustomer(email);
	}

	public void generateRandomCustomer() {
		log.info("generating random customer");
		dataGenerator.generateRandomCustomer();
	}

	// DO NOT USE required="true" property in input text widgets, unable to
	// generate random then without filling the form
	private boolean validate() {
		Set<ConstraintViolation<Customer>> violations = validator
				.validate(new Customer(email, name, password));
		if (violations.isEmpty()) {
			return true;
		} else {
			for (ConstraintViolation<Customer> constraintViolation : violations) {
				addMessage(constraintViolation.getPropertyPath() + " "
						+ constraintViolation.getMessageTemplate());
			}
		}
		return false;

	}
}

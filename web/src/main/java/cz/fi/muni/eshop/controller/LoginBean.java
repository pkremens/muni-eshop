package cz.fi.muni.eshop.controller;

import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.util.EntityValidator;
import cz.fi.muni.eshop.util.Identity;

@Model
public class LoginBean {

	private String email;
	private String password;
	private String status;

	@Inject
	private Identity identity;

	@Inject
	private Logger log;
	@Inject
	private CustomerManager customerManager;
	@Inject
	private EntityValidator<Customer> validator;
	@Inject
	private FacesContext facesContext;

	public void logIn() {
		log.warning("trying to log in: " + "email=" + email + " password="
				+ password);
		if (validate()) {
			Customer customer = customerManager.verifyCustomer(email, password);
			if (customer == null) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								"Wrong input", "inserted wrong data"));
				clearBean();
			} else {
				identity.logIn(customerManager.verifyCustomer(email, password));
				clearBean();
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Wrong input",
							"inserted invalid data"));
			clearBean();
		}
	}

	private void clearBean() {
		email = "";
		password = "";
	}

	public void logOut() {
		identity.logOut();
	}

	// DO NOT USE required="true" property in input text widgets, unable to
	// generate random then without filling the form
	// just front end validation
	private boolean validate() {
		Set<ConstraintViolation<Customer>> violations = validator
				.validate(new Customer(email, "dummy", password));
		if (violations.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}

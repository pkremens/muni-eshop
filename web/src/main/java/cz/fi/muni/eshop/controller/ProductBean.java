/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.fi.muni.eshop.controller;

import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.model.enums.Category;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.util.DataGenerator;
import cz.fi.muni.eshop.util.EntityValidator;
import javax.validation.ConstraintViolation;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 * 
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Model
public class ProductBean {
	private String name;
	private Long price;
	private Category category;
	private Long stored;
	private Long reserved;

	@Inject
	private FacesContext facesContext;

	@Inject
	private Logger log;
	@Inject
	private ProductManager productManager;
	@Inject
	private DataGenerator dataGenerator;
	@Inject
	private EntityValidator<Product> validator;

	@PostConstruct
	public void init() {
		clearBean();
	}

	public void createNewProduct() {

	}

	public void addProduct() {
		if (validate()) {
			productManager.addProduct(name, price, category, stored, reserved);
		}
		clearBean();
	}

	public List<Product> getProducts() {
		return productManager.getProducts();
	}

	public void refillProduct(Long id, long quantity) {
		productManager.hardRefillProduct(id, quantity);
	}

	public void initNewProduct() {
		this.name = "";
	}

	private void clearBean() {
		name = "";
		price = 1L;
		category = Category.TYPE1;
		stored = 0L;
		reserved = 0L;
	}

	public void deleteProduct(String name) {
		productManager.deleteProduct(name);
	}

	public void clearProducts() {
		productManager.clearProductsTable();
	}

	// just front end validation
	private boolean validate() {
		Set<ConstraintViolation<Product>> violations = validator
				.validate(new Product(name, price, category, stored, reserved));
		if (violations.isEmpty()) {
			return true;
		} else {
			for (ConstraintViolation<Product> constraintViolation : violations) {
				addMessage(constraintViolation.getPropertyPath() + " "
						+ constraintViolation.getMessageTemplate());
			}
		}
		return false;
	}

	private void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void generateRandomCustomer() {
		log.info("generating random product");
		dataGenerator.generateRandomProduct();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Long getStored() {
		return stored;
	}

	public void setStored(Long stored) {
		this.stored = stored;
	}

	public Long getReserved() {
		return reserved;
	}

	public void setReserved(Long reserved) {
		this.reserved = reserved;
	}

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.controller;

import cz.fi.muni.eshop.model.ProductEntity;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.util.quilifier.JPA;
import cz.fi.muni.eshop.util.quilifier.MyLogger;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
//TODO co vse musi implementovat serializable???
@Model
public class ProductController implements Serializable { 

    /**
	 * 
	 */
	private static final long serialVersionUID = 650851568081767180L;
	@Inject
    @JPA // using interface thus can later change to another implementation thanks to runtime bean type resolution see dummy translate test
    private ProductManager productManager;
    @Inject
    @MyLogger
    Logger log;
    private ProductEntity newProduct;
    @Inject
    private FacesContext facesContext;
    private static List<ProductEntity> productList; // should be static???

    @PostConstruct
    public void retrieveAllProducts() {
        productList = productManager.getProducts();
        initNewProduct();
    }

    @Produces
    @Named
    public ProductEntity getNewProduct() {
        return newProduct;
    }
//    // TODO bude potreba?
//    public void onProductListChanged(
//            @Observes(notifyObserver = Reception.IF_EXISTS) final ProductEntity product) {
//        retrieveAllProducts();
//    }

    @Produces
    @Named
    List<ProductEntity> getProductList() {
        return productList;
    }

    public void saveAction(ProductEntity product) {
        log.fine("Save action");
        product.setEditable(false);
        productManager.update(product);
    }

    public void editAction(ProductEntity product) {
        log.fine("Edit action");
        product.setEditable(true);
    }

    public void register() throws Exception {
        log.fine("Register new product");
        System.out.println(newProduct.toString());
        productManager.addProduct(newProduct);
        productList.add(newProduct);
        facesContext.addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_INFO, "Added!", "Product was added"));
        initNewProduct();
    }

    // TODO jeste upravit, jen dummy
    public void validatePriceRange(FacesContext context,
            UIComponent toValidate, Object value) {
        long input = (Long) value;

        if (input < 1 || input > 10000) {
            ((UIInput) toValidate).setValid(false);

            FacesMessage message = new FacesMessage("Invalid number");
            context.addMessage(toValidate.getClientId(context), message);
        }
    }
//
//    public void validateNumberRangeInBasket(FacesContext context,
//            UIComponent toValidate, Object value) {
//        int input = (Integer) value;
//
//        if (input < 1 || input > 10000) {
//            ((UIInput) toValidate).setValid(false);
//
//            FacesMessage message = new FacesMessage("Invalid number");
//            context.addMessage(toValidate.getClientId(context), message);
//        }
//    }
    public void initNewProduct() {
        log.fine("Init new product");
        newProduct = new ProductEntity();
    }
}

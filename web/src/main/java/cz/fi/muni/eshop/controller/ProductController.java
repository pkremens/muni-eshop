/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.controller;

import cz.fi.muni.eshop.model.ProductEntity;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.util.quilifier.JPA;
import cz.fi.muni.eshop.util.quilifier.MuniEshopLogger;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
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
@Named
@SessionScoped
public class ProductController implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 650851568081767180L;
    @Inject
    @JPA // using interface thus can later change to another implementation thanks to runtime bean type resolution see dummy translate test
    private ProductManager productManager;
    @Inject
    @MuniEshopLogger
    Logger log;
    private ProductEntity newProduct;
    @Inject
    private FacesContext facesContext;
    private static List<ProductEntity> productList; // should be static???
    @Produces
    private static boolean emptyProductsList; // potencialni misto k nejake chybe s vlakny, konzultovat!

    @PostConstruct
    public void retrieveAllProducts() {
        log.info("POST CONSTRUCT");
        log.info("Get all products");
        productList = productManager.getProducts();
        emptyProductsList = productList.isEmpty(); // isEmpty is calling to often to acces whole list all the time
        initNewProduct();
    }

    public boolean isEmptyProductsList() {
        log.log(Level.INFO, "Is list of products empty?: {0}", emptyProductsList);
        return emptyProductsList;
    }
    // TODO stejne, jde nejak udelat aby to JSF vyhodnotilo jen jednou?
//    @Produces
//    @Named("emptyProducts") // to be able to recognize which method is called not only 10x get product list
//    public boolean isProductListEmpty() {
//        log.info("Is product list empty");
//        return isEmpty;
//    }

    @Produces
    @Named
    public ProductEntity getNewProduct() {
        log.info("Get new product");
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
        log.info("Save action");
        product.setEditable(false);
        productManager.update(product);
    }

    public void editAction(ProductEntity product) {
        log.info("Edit action");
        product.setEditable(true);
    }

    public void register() throws Exception {
        log.info("Register new product: " + newProduct);
        emptyProductsList=false;
        productManager.addProduct(newProduct);
        productList.add(newProduct);
        facesContext.addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_INFO, "Added!", "Product was added"));
        initNewProduct();
    }

    // TODO jeste upravit, jen dummy
    public void validatePriceRange(FacesContext context,
            UIComponent toValidate, Object value) {
        log.info("Validate price range");
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
        log.info("Init new product");
        newProduct = new ProductEntity();
    }
}

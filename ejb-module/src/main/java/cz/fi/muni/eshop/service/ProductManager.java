/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service;

import cz.fi.muni.eshop.model.OrderEntity;
import cz.fi.muni.eshop.model.ProductEntity;
import java.util.List;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public interface ProductManager {

    void addProduct(ProductEntity product);

    void update(ProductEntity product);

    ProductEntity findProductById(long id);

    List<ProductEntity> getProducts();

	void updateOnStore(OrderEntity zoomOrder);
	
	 void fillTheStore(List<ProductEntity> products);
}

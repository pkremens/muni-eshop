/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.model.dao;

import cz.fi.muni.eshop.model.InvoiceItem;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@XmlRootElement
public class InvoiceItemDao {

    private Long id;
    private ProductDao product;
    private Long quantity;

    public InvoiceItemDao() {
    }

    public InvoiceItemDao(InvoiceItem invoiceItem) {
        this.id = invoiceItem.getId();
        this.product = new ProductDao(invoiceItem.getProduct());
        this.quantity = invoiceItem.getQuantity();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductDao getProduct() {
        return product;
    }

    public void setProduct(ProductDao product) {
        this.product = product;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final InvoiceItemDao other = (InvoiceItemDao) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.product != other.product && (this.product == null || !this.product.equals(other.product))) {
            return false;
        }
        if (this.quantity != other.quantity && (this.quantity == null || !this.quantity.equals(other.quantity))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 83 * hash + (this.product != null ? this.product.hashCode() : 0);
        hash = 83 * hash + (this.quantity != null ? this.quantity.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "InvoiceItemDao{" + "id=" + id + ", product=" + product + ", quantity=" + quantity + '}';
    }

    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Entity
@XmlRootElement
public class InvoiceItem implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    
    @OneToOne
    private Product product;
    
    @NotNull
    private Long quantity;

    public InvoiceItem() {
    }

    public InvoiceItem(Product product, Long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
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
        final InvoiceItem other = (InvoiceItem) obj;
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
        int hash = 3;
        hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 37 * hash + (this.product != null ? this.product.hashCode() : 0);
        hash = 37 * hash + (this.quantity != null ? this.quantity.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "InvoiceItem{" + "id=" + id + ", product=" + product + ", quantity=" + quantity + '}';
    }
    
    
}

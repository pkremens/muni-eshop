package cz.fi.muni.eshop.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Entity
public class OrderItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Product product;
    @NotNull
    @Min(1)
    private Long quantity;

    public OrderItem() {
    }

    public OrderItem(Product product, Long quantity) {
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
        final OrderItem other = (OrderItem) obj;
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
        hash = 59 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 59 * hash + (this.product != null ? this.product.hashCode() : 0);
        hash = 59 * hash + (this.quantity != null ? this.quantity.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "OrderItem{" + "id=" + id + ", product=" + product + ", quantity=" + quantity + '}';
    }
}
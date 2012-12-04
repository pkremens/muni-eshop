package cz.fi.muni.eshop.model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Entity(name = "orderLine")
@Table(name = "orderLine")
@NamedQueries({
    @NamedQuery(name = "orderLine.getOrderLines", query = "SELECT ol FROM orderLine ol")
})
public class OrderLineEntity implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    @NotNull
    private Long id;
    
    @OneToOne(cascade = {CascadeType.ALL})
    @NotNull
    private ProductEntity product;
    @Column(name = "QUANTITY", nullable = false)
    @NotNull
    private Long quantity;

    public OrderLineEntity() {
        super();
    }

    public OrderLineEntity(ProductEntity product, Long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
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
        final OrderLineEntity other = (OrderLineEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.product != other.product && (this.product == null || !this.product.equals(other.product))) {
            return false;
        }
        if (this.quantity != other.quantity) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 61 * hash + (this.product != null ? this.product.hashCode() : 0);
        hash = 61 * hash + (this.quantity != null ? this.quantity.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "OrderLineEntity{" + "id=" + id + ", product=" + product + ", quantity=" + quantity + '}';
    }
}
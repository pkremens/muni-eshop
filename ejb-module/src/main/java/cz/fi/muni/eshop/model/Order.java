package cz.fi.muni.eshop.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Entity
@Table(name = "Orders")
public class Order extends OrderRoot implements Serializable {

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "order_fk")
    private List<OrderItem> orderItems;
    @OneToOne(orphanRemoval = true) 
    private Invoice invoice;
    private Long totalPrice;
    public Order() {
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        super.equals(obj);
        if (this.orderItems != other.orderItems && (this.orderItems == null || !this.orderItems.equals(other.orderItems))) {
            return false;
        }
        if (this.invoice != other.invoice && (this.invoice == null || !this.invoice.equals(other.invoice))) {
            return false;
        }
        if (this.totalPrice != other.totalPrice && (this.totalPrice == null || !this.totalPrice.equals(other.totalPrice))) {
            return false;
        }
        return true;
    }

    public void addOrderItem(OrderItem orderItem) {
        if (orderItems == null) {
            this.orderItems = new ArrayList<OrderItem>();
        }
        orderItems.add(orderItem);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 97 * hash + (this.totalPrice != null ? this.totalPrice.hashCode() : 0);
        hash = 97 * hash + (this.orderItems != null ? this.orderItems.hashCode() : 0);
        hash = 97 * hash + (this.invoice != null ? this.invoice.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return super.toString() + " Order{" + "orderItems=" + orderItems + ", totalPrice=" + totalPrice + ", invoice=" + invoice + '}';
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.model.dao;

import cz.fi.muni.eshop.model.Order;
import cz.fi.muni.eshop.model.OrderItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@XmlRootElement(name="name")
public class OrderDao {

    private Long id;
    private CustomerLiteDao customer;
    private Date creationDate;
    private List<OrderItemDao> orderItems = new ArrayList<OrderItemDao>();
    private Long totalPrice;

    public OrderDao() {
    }

    public OrderDao(Order order) {
        this.id = order.getId();
        this.creationDate = order.getCreationDate();
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItems.add(new OrderItemDao(orderItem));
        }
        this.totalPrice = order.getTotalPrice();
        this.customer = new CustomerLiteDao(order.getCustomer());
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public CustomerLiteDao getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerLiteDao customer) {
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderItemDao> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDao> orderItems) {
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
        final OrderDao other = (OrderDao) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.customer != other.customer && (this.customer == null || !this.customer.equals(other.customer))) {
            return false;
        }
        if (this.creationDate != other.creationDate && (this.creationDate == null || !this.creationDate.equals(other.creationDate))) {
            return false;
        }
        if (this.orderItems != other.orderItems && (this.orderItems == null || !this.orderItems.equals(other.orderItems))) {
            return false;
        }
        if (this.totalPrice != other.totalPrice && (this.totalPrice == null || !this.totalPrice.equals(other.totalPrice))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 89 * hash + (this.customer != null ? this.customer.hashCode() : 0);
        hash = 89 * hash + (this.creationDate != null ? this.creationDate.hashCode() : 0);
        hash = 89 * hash + (this.orderItems != null ? this.orderItems.hashCode() : 0);
        hash = 89 * hash + (this.totalPrice != null ? this.totalPrice.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "OrderDao{" + "id=" + id + ", customer=" + customer + ", creationDate=" + creationDate + ", orderItems=" + orderItems + ", totalPrice=" + totalPrice + '}';
    }
}

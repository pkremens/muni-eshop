package cz.fi.muni.eshop.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */



@Entity(name = "orderEntity")
@Table(name = "orderEntity")
@NamedQueries({
    @NamedQuery(name = "order.getOrderById", query = "SELECT o FROM orderEntity o WHERE o.id = :id"),
    @NamedQuery(name = "order.getOrders", query = "SELECT o FROM orderEntity o"),
    @NamedQuery(name = "order.getOrdersByOpen", query = "SELECT o FROM orderEntity o WHERE o.openOrder=:open ORDER BY o.customer,o.creationDate ASC"), 
    })
public class OrderEntity implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    @NotNull
    private Long id;
    
    @OneToOne
    @NotNull
    private CustomerEntity customer;
    @Column(name = "OPENORDER", nullable = false)
    private boolean openOrder;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}) 
    private List<OrderLineEntity> orderLines;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATIONDATE", nullable = false)
    private Date creationDate;

    public OrderEntity() {
        super();
    }

    public OrderEntity(CustomerEntity customer, List<OrderLineEntity> orderLines) {
        this.customer = customer;
        this.openOrder = true;
        this.orderLines = orderLines;        
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isOpenOrder() {
        return openOrder;
    }

    public void setOpenOrder(boolean openOrder) {
        this.openOrder = openOrder;
    }

    public List<OrderLineEntity> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderLineEntity> orderLines) {
        this.orderLines = orderLines;
    }

    public Long getTotalPrice() {
    	long totalPrice = 0L;
    	for (OrderLineEntity orderLine : orderLines) {
			totalPrice += orderLine.getPrice();
		}
        return totalPrice;
    }
    
    public Long getTotalProductsQuantity() {
    	long totalProducts = 0L;
    	for (OrderLineEntity orderLine : orderLines) {
			totalProducts += orderLine.getQuantity();
		}
    	return totalProducts;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OrderEntity other = (OrderEntity) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.customer != other.customer && (this.customer == null || !this.customer.equals(other.customer))) {
            return false;
        }
        if (this.orderLines != other.orderLines && (this.orderLines == null || !this.orderLines.equals(other.orderLines))) {
            return false;
        }
        if (this.creationDate != other.creationDate && (this.creationDate == null || !this.creationDate.equals(other.creationDate))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 41 * hash + (this.customer != null ? this.customer.hashCode() : 0);
        hash = 41 * hash + (this.orderLines != null ? this.orderLines.hashCode() : 0);
        hash = 41 * hash + (this.creationDate != null ? this.creationDate.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "OrderEntity{" + "id=" + id + ", customer=" + customer + ", openOrder=" + openOrder + ", orderLines=" + orderLines + ", creationDate=" + creationDate + ", totalPrice=" + getTotalPrice() + '}';
    }
    
    
}

package cz.fi.muni.eshop.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Entity(name = "orderEntity")
@XmlRootElement
@Table(name = "orderEntity")
public class Order implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    @JoinColumn(nullable=false)
    @ManyToOne
    private Customer customer;
    
    @JoinColumn
    @ManyToOne
    private Storeman storeman;
    
    @OneToMany(cascade = {CascadeType.ALL})
    private List<OrderItem> orderLines;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @OneToOne
    private Invoice invoice;

    public Order() {
    }

    public Order(Customer customer, Storeman storeman) {
        this.customer = customer;
        this.storeman = storeman;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public List<OrderItem> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderItem> orderLines) {
        this.orderLines = orderLines;
    }

    public Storeman getStoreman() {
        return storeman;
    }

    public void setStoreman(Storeman storeman) {
        this.storeman = storeman;
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
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.customer != other.customer && (this.customer == null || !this.customer.equals(other.customer))) {
            return false;
        }
        if (this.storeman != other.storeman && (this.storeman == null || !this.storeman.equals(other.storeman))) {
            return false;
        }
        if (this.orderLines != other.orderLines && (this.orderLines == null || !this.orderLines.equals(other.orderLines))) {
            return false;
        }
        if (this.creationDate != other.creationDate && (this.creationDate == null || !this.creationDate.equals(other.creationDate))) {
            return false;
        }
        if (this.invoice != other.invoice && (this.invoice == null || !this.invoice.equals(other.invoice))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 41 * hash + (this.customer != null ? this.customer.hashCode() : 0);
        hash = 41 * hash + (this.storeman != null ? this.storeman.hashCode() : 0);
        hash = 41 * hash + (this.orderLines != null ? this.orderLines.hashCode() : 0);
        hash = 41 * hash + (this.creationDate != null ? this.creationDate.hashCode() : 0);
        hash = 41 * hash + (this.invoice != null ? this.invoice.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", customer=" + customer + ", storeman=" + storeman + ", orderLines=" + orderLines + ", creationDate=" + creationDate + ", invoice=" + invoice + '}';
    }
    
    
}

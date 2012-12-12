/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Entity
@XmlRootElement
public class Invoice implements Serializable {
   
    @Id
    @GeneratedValue
    private Long id;
    
    @JoinColumn(nullable = false)
    @ManyToOne
    private Customer customer;
    
    @JoinColumn(nullable = false)
    @ManyToOne
    private Storeman storeman;
    

    @OneToMany
    private List<InvoiceItem> invoiceLines;
   
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    
    @OneToOne
    @JoinColumn(nullable = false)
    private Order order;

    public Invoice() {
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

    public List<InvoiceItem> getInvoiceLines() {
        return invoiceLines;
    }

    public void setInvoiceLines(List<InvoiceItem> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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
        final Invoice other = (Invoice) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.customer != other.customer && (this.customer == null || !this.customer.equals(other.customer))) {
            return false;
        }
        if (this.storeman != other.storeman && (this.storeman == null || !this.storeman.equals(other.storeman))) {
            return false;
        }
        if (this.invoiceLines != other.invoiceLines && (this.invoiceLines == null || !this.invoiceLines.equals(other.invoiceLines))) {
            return false;
        }
        if (this.creationDate != other.creationDate && (this.creationDate == null || !this.creationDate.equals(other.creationDate))) {
            return false;
        }
        if (this.order != other.order && (this.order == null || !this.order.equals(other.order))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 67 * hash + (this.customer != null ? this.customer.hashCode() : 0);
        hash = 67 * hash + (this.storeman != null ? this.storeman.hashCode() : 0);
        hash = 67 * hash + (this.invoiceLines != null ? this.invoiceLines.hashCode() : 0);
        hash = 67 * hash + (this.creationDate != null ? this.creationDate.hashCode() : 0);
        hash = 67 * hash + (this.order != null ? this.order.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Invoice{" + "id=" + id + ", customer=" + customer + ", storeman=" + storeman + ", invoiceLines=" + invoiceLines + ", creationDate=" + creationDate + ", order=" + order + '}';
    }

    
    
    

    

    
    

}

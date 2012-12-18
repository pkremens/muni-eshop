/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.model.dao;

import cz.fi.muni.eshop.model.Invoice;
import cz.fi.muni.eshop.model.InvoiceItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@XmlRootElement
public class InvoiceDao {

    private Long id;
    private CustomerLiteDao customer;
    private Date creationDate;
    private List<InvoiceItemDao> invoiceItems = new ArrayList<InvoiceItemDao>();
    private OrderDao order;

    public InvoiceDao() {
    }

    public InvoiceDao(Invoice invoice) {
        this.id = invoice.getId();
        this.customer = new CustomerLiteDao(invoice.getCustomer());
        this.creationDate = invoice.getCreationDate();
        for (InvoiceItem invoiceItem : invoice.getInvoiceItems()) {
            invoiceItems.add(new InvoiceItemDao(invoiceItem));
        }
        this.order = new OrderDao(invoice.getOrder());
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

    public List<InvoiceItemDao> getInvoiceItems() {
        return invoiceItems;
    }

    public void setInvoiceItems(List<InvoiceItemDao> invoiceItems) {
        this.invoiceItems = invoiceItems;
    }

    public OrderDao getOrder() {
        return order;
    }

    public void setOrder(OrderDao order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final InvoiceDao other = (InvoiceDao) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if (this.customer != other.customer && (this.customer == null || !this.customer.equals(other.customer))) {
            return false;
        }
        if (this.creationDate != other.creationDate && (this.creationDate == null || !this.creationDate.equals(other.creationDate))) {
            return false;
        }
        if (this.invoiceItems != other.invoiceItems && (this.invoiceItems == null || !this.invoiceItems.equals(other.invoiceItems))) {
            return false;
        }
        if (this.order != other.order && (this.order == null || !this.order.equals(other.order))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 83 * hash + (this.customer != null ? this.customer.hashCode() : 0);
        hash = 83 * hash + (this.creationDate != null ? this.creationDate.hashCode() : 0);
        hash = 83 * hash + (this.invoiceItems != null ? this.invoiceItems.hashCode() : 0);
        hash = 83 * hash + (this.order != null ? this.order.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "InvoiceDao{" + "id=" + id + ", customer=" + customer + ", creationDate=" + creationDate + ", invoiceItems=" + invoiceItems + ", order=" + order + '}';
    }

    
}

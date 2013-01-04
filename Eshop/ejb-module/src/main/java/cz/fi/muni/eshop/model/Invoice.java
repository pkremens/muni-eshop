/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Entity
public class Invoice extends OrderRoot implements Serializable {

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "invoice_fk")
    private List<InvoiceItem> invoiceLines;
    @OneToOne
    @JoinColumn(nullable = false)
    private Order order;

    public Invoice() {
    }

    public List<InvoiceItem> getInvoiceItems() {
        return invoiceLines;
    }

    public void setInvoiceItems(List<InvoiceItem> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
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
        final Invoice other = (Invoice) obj;
        super.equals(obj);
        if (this.order != other.order && (this.order == null || !this.order.equals(other.order))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 29 * hash + (this.invoiceLines != null ? this.invoiceLines.hashCode() : 0);
        hash = 29 * hash + (this.order != null ? this.order.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return super.toString() + " Invoice{" + "invoiceLines=" + invoiceLines + ", order=" + order.getId() + '}';
    }
}
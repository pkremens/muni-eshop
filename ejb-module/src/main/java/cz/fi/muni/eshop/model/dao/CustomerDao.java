/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.model.dao;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.model.Invoice;
import cz.fi.muni.eshop.model.Order;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@XmlRootElement
public class CustomerDao {
    private Long id;
    private String email;
    private String name;
    private String password;
    private List<OrderDao> order = new ArrayList<OrderDao>();
    private List<InvoiceDao> invoice = new ArrayList<InvoiceDao>();

    public CustomerDao() {
    }
    
    public CustomerDao(Customer customer) {
        this.id = customer.getId();
        this.email = customer.getEmail();
        this.name = customer.getName();
        this.password = customer.getPassword();
        for (Order orderCust : customer.getOrder()) {
            order.add(new OrderDao(orderCust));
        }
        for (Invoice invoiceCust : customer.getInvoice()) {
            invoice.add(new InvoiceDao(invoiceCust));
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<InvoiceDao> getInvoice() {
        return invoice;
    }

    public void setInvoice(List<InvoiceDao> invoice) {
        this.invoice = invoice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OrderDao> getOrder() {
        return order;
    }

    public void setOrder(List<OrderDao> order) {
        this.order = order;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CustomerDao other = (CustomerDao) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.email == null) ? (other.email != null) : !this.email.equals(other.email)) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.password == null) ? (other.password != null) : !this.password.equals(other.password)) {
            return false;
        }
        if (this.order != other.order && (this.order == null || !this.order.equals(other.order))) {
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
        hash = 79 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 79 * hash + (this.email != null ? this.email.hashCode() : 0);
        hash = 79 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 79 * hash + (this.password != null ? this.password.hashCode() : 0);
        hash = 79 * hash + (this.order != null ? this.order.hashCode() : 0);
        hash = 79 * hash + (this.invoice != null ? this.invoice.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "CustomerDao{" + "id=" + id + ", email=" + email + ", name=" + name + ", password=" + password + ", order=" + order + ", invoice=" + invoice + '}';
    }
    
    
}

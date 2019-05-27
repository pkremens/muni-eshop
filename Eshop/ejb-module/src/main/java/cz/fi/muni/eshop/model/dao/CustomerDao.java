/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.model.dao;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.model.Invoice;
import cz.fi.muni.eshop.model.Order;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@XmlRootElement(name = "customer")
public class CustomerDao {

    private Long id;
    private String email;
    private String name;
    private String password;
    private List<OrderDao> orders = new ArrayList<OrderDao>();
    private List<InvoiceDao> invoices = new ArrayList<InvoiceDao>();

    public CustomerDao() {
    }

    public CustomerDao(Customer customer) {
        this.id = customer.getId();
        this.email = customer.getEmail();
        this.name = customer.getName();
        this.password = customer.getPassword();
        for (Order order : customer.getOrder()) {
            orders.add(new OrderDao(order));
        }
        for (Invoice invoice : customer.getInvoice()) {
            invoices.add(new InvoiceDao(invoice));
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

    public List<InvoiceDao> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<InvoiceDao> invoices) {
        this.invoices = invoices;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OrderDao> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDao> orders) {
        this.orders = orders;
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
        if (this.orders != other.orders && (this.orders == null || !this.orders.equals(other.orders))) {
            return false;
        }
        return this.invoices == other.invoices || (this.invoices != null && this.invoices.equals(other.invoices));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 73 * hash + (this.email != null ? this.email.hashCode() : 0);
        hash = 73 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 73 * hash + (this.password != null ? this.password.hashCode() : 0);
        hash = 73 * hash + (this.orders != null ? this.orders.hashCode() : 0);
        hash = 73 * hash + (this.invoices != null ? this.invoices.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "CustomerDao{" + "id=" + id + ", email=" + email + ", name=" + name + ", password=" + password + ", orders=" + orders + ", invoices=" + invoices + '}';
    }
}

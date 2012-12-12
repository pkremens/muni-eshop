/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Entity
@XmlRootElement
public class Storeman implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    private String name;
    
    @OneToMany(mappedBy = "storeman")
    private List<Order> order;
   
    @OneToMany(mappedBy = "storeman")
    private List<Invoice> invoice;

    public Storeman() {
    }

    public Storeman(String name) {
        this.name = name;
    }
    
    public Storeman(String name, List<Order> order, List<Invoice> invoice) {
        this(name);
        this.order = order;
        this.invoice = invoice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Invoice> getInvoice() {
        return invoice;
    }

    public void setInvoice(List<Invoice> invoice) {
        this.invoice = invoice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Order> getOrder() {
        return order;
    }

    public void setOrder(List<Order> order) {
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
        final Storeman other = (Storeman) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
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
        int hash = 7;
        hash = 97 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 97 * hash + (this.order != null ? this.order.hashCode() : 0);
        hash = 97 * hash + (this.invoice != null ? this.invoice.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Storeman{" + "id=" + id + ", name=" + name + ", order=" + order + ", invoice=" + invoice + '}';
    }
    
}

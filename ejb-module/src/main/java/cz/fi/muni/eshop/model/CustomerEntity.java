/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.model;

import cz.fi.muni.eshop.util.Role;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;
import org.picketlink.idm.api.User;

@Entity(name = "customer")
@NamedQueries({
    @NamedQuery(name = "customer.getCustomers", query = "SELECT c FROM customer c"),
    @NamedQuery(name = "customer.findCustomersOrderedByMail", query = "select c from customer c ORDER BY c.email ASC"),
    @NamedQuery(name = "customer.findByEmail", query = "SELECT c FROM customer c WHERE c.email = :email")
})
@Table(name = "customer")
public class CustomerEntity implements User, Serializable {

    @Id
    @Size(min = 1, max = 25)
    @Pattern(regexp = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message = "not a valid email address")
    private String email;
    // allow also number to simplify creation of dummy data
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[A-Za-z0-9 ]*", message = "must contain only letters, numbers and spaces")
    private String name;
    @NotEmpty
    @NotNull
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Transient
    boolean editable = false;

    public CustomerEntity() {
    }

    public CustomerEntity(String email, String name, String password, Role role) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public String getPasswordSubstring() {
        try {
            return password.substring(0, 10);
        } catch (StringIndexOutOfBoundsException sioube) {
            return password; // TODO just for test purposes
        }
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CustomerEntity other = (CustomerEntity) obj;
        if ((this.email == null) ? (other.email != null) : !this.email.equals(other.email)) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.role == null) ? (other.role != null) : !this.role.equals(other.role)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.email != null ? this.email.hashCode() : 0);
        hash = 37 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 37 * hash + (this.role != null ? this.role.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "email: " + email + " name: " + name;
    }

    public String toLog() {
        return "Customer [email=" + email + ", name=" + name + ", password="
                + getPasswordSubstring() + ", role=" + role + "]";
    }

    @Override
    public String getKey() {
        return getId();
    }

    @Override
    public String getId() {
        return getEmail();
    }
}
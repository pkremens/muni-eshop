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
import javax.xml.bind.annotation.XmlRootElement;
import javax.validation.constraints.NotNull;

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
}

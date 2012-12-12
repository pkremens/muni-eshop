/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    

    

    
    

}

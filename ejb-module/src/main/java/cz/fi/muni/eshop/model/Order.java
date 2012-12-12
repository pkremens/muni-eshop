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
}

package cz.fi.muni.eshop.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Entity
@XmlRootElement
public class OrderItem implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    @OneToOne
    private Product product;
    
    @NotNull
    private Long quantity;
}
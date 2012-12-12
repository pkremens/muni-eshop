package cz.fi.muni.eshop.model;

import cz.fi.muni.eshop.model.enums.Category;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Entity
@XmlRootElement
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String productName;
    @NotNull
    private Long base;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;
    @NotNull
    private Long onStore;
    @NotNull
    private Long reserved;
}

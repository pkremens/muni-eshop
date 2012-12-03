package cz.fi.muni.eshop.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Entity(name = "product")
@Table(name = "product")
@NamedQueries({
    @NamedQuery(name = "product.allProducts", query = "SELECT p FROM product p")
})
public class ProductEntity implements Serializable {

    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EAN", nullable = false)
    @NotNull
    private Long ean;
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[A-Za-z0-9 ]*", message = "must contain only letters ,spaces and numbers")
    @NotNull
    @Column(name = "PRODUCTNAME", nullable = false)
    private String productName;
    @NotNull
    @Column(name = "BASEPRICE", nullable = false)
    private Long basePrice;

    public ProductEntity() {
        super();
    }

    public ProductEntity(Long ean, String productName, Long basePrice) {
        this.ean = ean;
        this.productName = productName;
        this.basePrice = basePrice;
    }

    public Long getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Long basePrice) {
        this.basePrice = basePrice;
    }

    public Long getEan() {
        return ean;
    }

    public void setEan(Long ean) {
        this.ean = ean;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

   
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProductEntity other = (ProductEntity) obj;
        if (this.ean != other.ean && (this.ean == null || !this.ean.equals(other.ean))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.ean != null ? this.ean.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "ProductBaseModelBean{" + "ean=" + ean + ", productName=" + productName + ", basePrice=" + basePrice + '}';
    }
}

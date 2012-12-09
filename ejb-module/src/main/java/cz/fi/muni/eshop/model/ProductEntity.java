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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@XmlRootElement
@Entity(name = "product")
@Table(name = "product")
@NamedQueries({
    @NamedQuery(name = "product.getProducts", query = "SELECT p FROM product p"),
    @NamedQuery(name = "product.findProductById", query = "SELECT p FROM product p WHERE p.id = :id")
})
public class ProductEntity implements Serializable, Comparable<ProductEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    @NotNull
    private Long id;
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[A-Za-z0-9 ]*", message = "must contain only letters ,spaces and numbers")
    @NotNull
    @Column(name = "PRODUCTNAME", nullable = false)
    private String productName;
    @NotNull
    @Column(name = "BASEPRICE", nullable = false)
    private Long basePrice;
    @Transient
    boolean editable = false;
    @Transient
    Long quantityInBasket = 1L;
    @NotNull
    private Long onStore = 0L;

    public ProductEntity() {
        super();
    }

    public ProductEntity(String productName, Long basePrice) {
        this.productName = productName;
        this.basePrice = basePrice;
    }

    public Long getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Long basePrice) {
        this.basePrice = basePrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void switchEditable() {
        editable = !editable;
    }

    public Long getQuantityInBasket() {
        return quantityInBasket;
    }

    public void setQuantityInBasket(Long quantityInBasket) {
        this.quantityInBasket = quantityInBasket;
    }
   
    public Long getOnStore() {
		return onStore;
	}

	public void setOnStore(Long onStore) {
		this.onStore = onStore;
	}



    @Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductEntity other = (ProductEntity) obj;
		if (basePrice == null) {
			if (other.basePrice != null)
				return false;
		} else if (!basePrice.equals(other.basePrice))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((basePrice == null) ? 0 : basePrice.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((productName == null) ? 0 : productName.hashCode());
		return result;
	}

    @Override
    public String toString() {
        return "ProductEntity{" + "id=" + id + ", productName=" + productName + ", basePrice=" + basePrice + ", onStore=" + onStore + '}';
    }
 

    @Override
    public int compareTo(ProductEntity product) {
        return this.id.compareTo(product.id);
    }

}

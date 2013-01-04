package cz.fi.muni.eshop.model;

import cz.fi.muni.eshop.model.enums.Category;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Entity
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Size(min = 1, max = 25, message = "Name may not be null")
    @Pattern(regexp = "[A-Za-z0-9]*", message = "Name must contain only letters and numbers")
    @Column(unique = true)
    private String name;
    @NotNull
    @Min(1)
    private Long price;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;
    @NotNull
    //@Min(0)
    private Long stored;
    @NotNull
    //@Min(0)
    private Long reserved = 0L;

    public Product() {
    }

    public Product(String name, Long price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Product(String name, Long price, Category category, Long stored, Long reserved) {
        this(name, price, category);
        this.stored = stored;
        this.reserved = reserved;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStored() {
        return stored;
    }

    public void setStored(Long stored) {
        this.stored = stored;
    }

    public String getProductName() {
        return name;
    }

    public void setProductName(String name) {
        this.name = name;
    }

    public Long getReserved() {
        return reserved;
    }

    public void setReserved(Long reserved) {
        this.reserved = reserved;
    }

    public Long addReserved(Long toAdd) {
        return reserved += toAdd;
    }

    public Long addStored(Long toAdd) {
        return stored += toAdd;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Product other = (Product) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.price != other.price && (this.price == null || !this.price.equals(other.price))) {
            return false;
        }
        if (this.category != other.category) {
            return false;
        }
        if (this.stored != other.stored && (this.stored == null || !this.stored.equals(other.stored))) {
            return false;
        }
        if (this.reserved != other.reserved && (this.reserved == null || !this.reserved.equals(other.reserved))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 47 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 47 * hash + (this.price != null ? this.price.hashCode() : 0);
        hash = 47 * hash + (this.category != null ? this.category.hashCode() : 0);
        hash = 47 * hash + (this.stored != null ? this.stored.hashCode() : 0);
        hash = 47 * hash + (this.reserved != null ? this.reserved.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Product{" + "id=" + id + ", name=" + name + ", price=" + price + ", category=" + category + ", stored=" + stored + ", reserved=" + reserved + '}';
    }
}

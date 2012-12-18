/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.model.dao;

import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.model.enums.Category;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@XmlRootElement
public class ProductDao {

    private Long id;
    private String name;
    private Long price;
    private Category category;
    private Long stored;
    private Long reserved;

    public ProductDao() {
    }

    public ProductDao(Product product) {
        this.id = product.getId();
        this.name = product.getProductName();
        this.price = product.getPrice();
        this.reserved = product.getReserved();
        this.category = product.getCategory();
        this.stored = product.getStored();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getStored() {
        return stored;
    }

    public void setStored(Long stored) {
        this.stored = stored;
    }

    public Long getReserved() {
        return reserved;
    }

    public void setReserved(Long reserved) {
        this.reserved = reserved;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 13 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 13 * hash + (this.price != null ? this.price.hashCode() : 0);
        hash = 13 * hash + (this.category != null ? this.category.hashCode() : 0);
        hash = 13 * hash + (this.stored != null ? this.stored.hashCode() : 0);
        hash = 13 * hash + (this.reserved != null ? this.reserved.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProductDao other = (ProductDao) obj;
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
    public String toString() {
        return "ProductDao{" + "id=" + id + ", name=" + name + ", price=" + price + ", category=" + category + ", stored=" + stored + ", reserved=" + reserved + '}';
    }
}    

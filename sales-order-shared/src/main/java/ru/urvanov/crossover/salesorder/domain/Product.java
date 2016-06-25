package ru.urvanov.crossover.salesorder.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "product")
public class Product implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1287784049943467759L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    @Size(min = 1, max = 200)
    @NotNull
    private String code;

    @Column
    @Size(min = 1, max = 200)
    @NotNull
    private String description;

    @Column
    @NotNull
    private BigDecimal price;

    @Column
    @NotNull
    private Integer quantity;

    @Column
    @Version
    private Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Integer getVersion() {
        return version;
    }
    
    @Override
    public int hashCode() {
        if (this.id == null)
            return super.hashCode();
        else
            return this.id.hashCode();
    }
    
    @Override
    public boolean equals(Object other) {
        if (other instanceof Product) {
            Product productOther = (Product) other;
            if (productOther.getId() != null && id != null
                    && id.equals(productOther.getId()))
                return true;
        }
        return false;

    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", code=" + code + ", description="
                + description + ", price=" + price + ", quantity=" + quantity
                + ", version=" + version + "]";
    }
    
    
}

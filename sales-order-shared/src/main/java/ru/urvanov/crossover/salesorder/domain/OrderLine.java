package ru.urvanov.crossover.salesorder.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "order_line")
public class OrderLine implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 4422191700754388239L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "sale_order_id")
    private SaleOrder saleOrder;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    @Column
    private Integer quantity;

    @NotNull
    @Column
    private BigDecimal price;

    @Column
    @Version
    private Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SaleOrder getSaleOrder() {
        return saleOrder;
    }

    public void setSaleOrder(SaleOrder saleOrder) {
        this.saleOrder = saleOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
        if (other instanceof OrderLine) {
            OrderLine orderLineOther = (OrderLine) other;
            if (orderLineOther.getId() != null && id != null
                    && id.equals(orderLineOther.getId()))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "OrderLine [id=" + id 
                + ", product=" + product + ", quantity=" + quantity
                + ", price=" + price + ", version=" + version + "]";
    }

}

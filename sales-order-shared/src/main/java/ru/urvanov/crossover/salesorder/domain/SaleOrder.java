package ru.urvanov.crossover.salesorder.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "sale_order")
public class SaleOrder implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7110224946928935372L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    @Size(min = 1, max = 200)
    @NotNull
    private String number;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "saleOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<OrderLine> orderLines = new HashSet<>();

    @Column
    @Version
    private Integer version;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Set<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(Set<OrderLine> orderLines) {
        this.orderLines = orderLines;
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
        if (other instanceof SaleOrder) {
            SaleOrder orderOther = (SaleOrder) other;
            if (orderOther.getId() != null && id != null
                    && id.equals(orderOther.getId()))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "SaleOrder [id=" + id + ", customer=" + customer
                 + ", version=" + version + "]";
    }

}

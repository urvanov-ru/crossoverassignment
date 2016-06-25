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
@Table(name = "customer")
public class Customer implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8596445772945564946L;

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
    private String name;

    @Column
    @Size(min = 1, max = 50)
    @NotNull
    private String phone1;

    @Column
    @Size(min = 1, max = 50)
    @NotNull
    private String phone2;

    @Column
    @Size(min = 1, max = 200)
    @NotNull
    private String address;

    @Column(name = "current_credit")
    @NotNull
    private BigDecimal currentCredit;

    @Column(name = "credit_limit")
    @NotNull
    private BigDecimal creditLimit;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getCurrentCredit() {
        return currentCredit;
    }

    public void setCurrentCredit(BigDecimal currentCredit) {
        this.currentCredit = currentCredit;
    }
    
    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
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
        if (other instanceof Customer) {
            Customer customerOther = (Customer) other;
            if (customerOther.getId() != null && id != null
                    && id.equals(customerOther.getId()))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Customer [id=" + id + ", code=" + code + ", name=" + name
                + ", phone1=" + phone1 + ", phone2=" + phone2 + ", address="
                + address + ", currentCredit=" + currentCredit + ", version="
                + version + "]";
    }

}

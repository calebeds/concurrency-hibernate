package com.calebe.concurrency.domain;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "products")
@DynamicUpdate
public class VersionedProduct {
    @Id
    private Integer id;
    private String name;
    private Integer stock;
    @Version
    private int version;

    public VersionedProduct() {
    }

    public VersionedProduct(Integer id, String name, Integer stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}

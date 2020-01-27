package com.modnsolutions.project.db.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class PaymentOption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;

    @Column(nullable = false)
    private String option;

    @Column(nullable = false)
    private Double discount;

    @OneToMany(targetEntity = MealOrder.class, mappedBy = "paymentOption")
    private List<MealOrder> orders;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    public PaymentOption() {
    }

    public PaymentOption(String option, Double discount) {
        this.option = option;
        this.discount = discount;
    }
}

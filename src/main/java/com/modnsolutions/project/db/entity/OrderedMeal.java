package com.modnsolutions.project.db.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class OrderedMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(targetEntity = MealOrder.class)
    private MealOrder mealOrder;

    @ManyToOne(targetEntity = Meal.class)
    private Meal meal;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    public OrderedMeal() {
    }

    public OrderedMeal(String name, Double price, Integer quantity, MealOrder mealOrder, Meal meal) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.mealOrder = mealOrder;
        this.meal = meal;
    }
}

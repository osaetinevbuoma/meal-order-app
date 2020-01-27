package ng.com.byteworks.project.db.entity;

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
public class MealOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;

    @Column(nullable = false)
    private Integer reference;

    @Column(nullable = false)
    private Boolean isPlacedNow;

    @Column(nullable = false)
    private Boolean isDispatched;

    @Column(nullable = false)
    private Boolean isPaid;

    @OneToMany(targetEntity = OrderedMeal.class, mappedBy = "mealOrder")
    private List<OrderedMeal> orderedMeals;

    @ManyToOne(targetEntity = User.class)
    private User user;

    @ManyToOne(targetEntity = PaymentOption.class)
    private PaymentOption paymentOption;

    @ManyToOne(targetEntity = DeliveryType.class)
    private DeliveryType deliveryType;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    public MealOrder() {
    }

    public MealOrder(Integer reference, Boolean isPaid) {
        this.reference = reference;
        this.isPlacedNow = true;
        this.isDispatched = false;
        this.isPaid = isPaid;
    }
}

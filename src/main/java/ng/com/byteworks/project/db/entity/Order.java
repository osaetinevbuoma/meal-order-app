package ng.com.byteworks.project.db.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Boolean isPlacedNow;

    @Column(nullable = false)
    private Boolean isDispatched;

    @Column(nullable = false)
    private Boolean isPaid;

    @ManyToOne(targetEntity = User.class)
    private User user;

    @ManyToOne(targetEntity = PaymentOption.class)
    private PaymentOption paymentOption;

    @ManyToOne(targetEntity = DeliveryType.class)
    private DeliveryType deliveryType;

    @ManyToOne(targetEntity = Meal.class)
    private Meal meal;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    public Order() {
    }

    public Order(Integer quantity, Boolean isPaid) {
        this.quantity = quantity;
        this.isPlacedNow = true;
        this.isDispatched = false;
        this.isPaid = isPaid;
    }
}

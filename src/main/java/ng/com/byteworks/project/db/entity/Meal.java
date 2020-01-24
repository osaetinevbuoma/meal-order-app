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
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false, length = 20000)
    private String description;

    @Column(nullable = false)
    private Boolean isAvailable;

    @OneToMany(targetEntity = Cart.class, mappedBy = "meal")
    private List<Cart> carts;

    @OneToMany(targetEntity = Order.class, mappedBy = "meal")
    private List<Order> orders;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    public Meal() {
    }

    public Meal(String name, String image, Double price, String description, Boolean isAvailable) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.description = description;
        this.isAvailable = isAvailable;
    }
}

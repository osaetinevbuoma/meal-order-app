package ng.com.byteworks.project.controller;

import ng.com.byteworks.project.db.entity.Order;
import ng.com.byteworks.project.service.EmailService;
import ng.com.byteworks.project.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Secured("ROLE_DEVELOPER")
@RequestMapping("/api")
public class OrderController {
    private final EmailService emailService;
    private final OrderService orderService;

    public OrderController(EmailService emailService, OrderService orderService) {
        this.emailService = emailService;
        this.orderService = orderService;
    }

    @PostMapping(value = "/order/save", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveOrder(@RequestBody List<Order> orders) {
        orderService.saveOrder(orders);

        // send email to vendor
        emailService.sendOrderConfirmationEmail(orders);

        return ResponseEntity.ok().build();
    }
}

package ng.com.byteworks.project.controller;

import ng.com.byteworks.project.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Secured("ROLE_DEVELOPER")
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/order")
    public ResponseEntity<?> order() {
        return ResponseEntity.ok(orderService.checkoutInformation());
    }
}

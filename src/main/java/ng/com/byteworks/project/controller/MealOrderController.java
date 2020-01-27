package ng.com.byteworks.project.controller;

import ng.com.byteworks.project.db.entity.MealOrder;
import ng.com.byteworks.project.service.EmailService;
import ng.com.byteworks.project.service.MealOrderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@Secured("ROLE_DEVELOPER")
@RequestMapping("/api")
public class MealOrderController {
    private final static Logger log = Logger.getLogger(MealOrderController.class.getName());

    private final EmailService emailService;
    private final MealOrderService mealOrderService;

    public MealOrderController(EmailService emailService, MealOrderService mealOrderService) {
        this.emailService = emailService;
        this.mealOrderService = mealOrderService;
    }

    @PostMapping(value = "/order/save", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> saveOrder(@RequestBody MealOrder order) {
        log.info(order.toString());
        mealOrderService.saveOrder(order);

        // send email to vendor notifying them of a placed order
        emailService.sendOrderConfirmationEmail(order);

        return ResponseEntity.ok().build();
    }
}

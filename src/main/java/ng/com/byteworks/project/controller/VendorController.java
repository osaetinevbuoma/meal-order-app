package ng.com.byteworks.project.controller;

import ng.com.byteworks.project.db.entity.Meal;
import ng.com.byteworks.project.db.entity.MealOrder;
import ng.com.byteworks.project.service.VendorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@Secured("ROLE_VENDOR")
@RequestMapping("/api/vendor")
public class VendorController {
    private final VendorService vendorService;

    private final static Logger log = Logger.getLogger(VendorController.class.getName());

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping(value = "/meals", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listMeals() {
        return ResponseEntity.ok(vendorService.listMeals());
    }

    @PostMapping(value = "/meal/add", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addMeal(@RequestBody Meal meal) {
        if (null == meal) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        log.info(meal.toString());

        Map<String, Object> mealMap = vendorService.addMeal(meal);
        return ResponseEntity.ok(mealMap);
    }

    @GetMapping(value = "/meal/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMeal(@PathVariable("id") int id) {
        Map<String, Object> mealMap = vendorService.getMeal(id);
        if (null == mealMap) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok(mealMap);
    }

    @PutMapping(value = "/meal/update", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateMeal(@RequestBody Meal meal) {
        if (null == meal) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        Map<String, Object> mealMap = vendorService.updateMeal(meal);
        if (null == mealMap) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok(mealMap);
    }

    @GetMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listOrders() {
        return ResponseEntity.ok(vendorService.listOrders());
    }

    @GetMapping(value = "/order/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getOrderDetails(@PathVariable("id") int id) {
        List<Map<String, Object>> orderDetails = vendorService.getOrderDetails(id);
        if (null == orderDetails) return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Requested order not found.");

        return ResponseEntity.ok(orderDetails);
    }

    @GetMapping(value = "/payment-options", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listPaymentOptions() {
        return ResponseEntity.ok(vendorService.listPaymentOptions());
    }

    @GetMapping(value = "/delivery-types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listDeliveryTypes() {
        return ResponseEntity.ok(vendorService.listDeliveryTypes());
    }

    @PutMapping(value = "/fulfil-order", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> fulfilOrder(@RequestBody MealOrder mealOrder) {
        vendorService.fulfilOrder(mealOrder);
        return ResponseEntity.ok().body("Saved");
    }
}

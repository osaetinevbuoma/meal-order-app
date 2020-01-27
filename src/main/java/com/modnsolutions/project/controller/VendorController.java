package com.modnsolutions.project.controller;

import com.modnsolutions.project.db.entity.Meal;
import com.modnsolutions.project.db.entity.MealOrder;
import com.modnsolutions.project.service.VendorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public ResponseEntity<?> listMeals(@RequestParam("page") Optional<Integer> page) {
        Map<String, Object> meals = new HashMap<>();

        int currentPage = page.orElse(1);
        int itemsPerPage = 8;

        Page<Meal> mPage = vendorService.listMeals(PageRequest.of(currentPage - 1,
                itemsPerPage));
        meals.put("meals", mPage);

        int totalPages = mPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            meals.put("pageNumbers", pageNumbers);
        }

        return ResponseEntity.ok(meals);
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

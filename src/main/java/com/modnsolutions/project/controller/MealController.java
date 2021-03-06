package com.modnsolutions.project.controller;

import com.modnsolutions.project.db.entity.Meal;
import com.modnsolutions.project.service.MealService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestController
@Secured("ROLE_DEVELOPER")
@RequestMapping("/api")
public class MealController {
    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @GetMapping("/meals")
    public ResponseEntity<?> meals(@RequestParam("page") Optional<Integer> page) {
        Map<String, Object> meals = new HashMap<>();

        int currentPage = page.orElse(1);
        int itemsPerPage = 12;

        Page<Meal> mealPage = mealService.listMeals(PageRequest.of(currentPage - 1,
                itemsPerPage));
        meals.put("meals", mealPage);

        int totalPages = mealPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            meals.put("pageNumbers", pageNumbers);
        }

        return ResponseEntity.ok(meals);
    }

    @GetMapping("/meal/{id}")
    public ResponseEntity<?> mealDetail(@PathVariable("id") int id) {
        Map<String, Object> meal = mealService.getMeal(id);
        if (null == meal) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok(meal);
    }
}

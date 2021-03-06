package com.modnsolutions.project.controller;

import com.modnsolutions.project.service.AuthenticationService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Optional;

@Controller
public class StaticController {
    private final AuthenticationService authenticationService;

    public StaticController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/")
    public String index(@RequestParam("auth-failed") Optional<String> failed, Model model) {
        if (failed.isPresent()) {
            model.addAttribute("error", "Invalid email and password " +
                    "combination");
        }

        return "static/index";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        Authentication authentication = authenticationService.getAuth();
        if (null != authentication) {
            try {
                new HttpServletRequestWrapper(request).logout();
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }

        return "redirect:/";
    }

    /*
     * Meal ordering static pages.
     */
    @GetMapping("/meals")
    @Secured("ROLE_DEVELOPER")
    public String meals() {
        return "static/meals";
    }

    @GetMapping("/meal/{id}")
    @Secured("ROLE_DEVELOPER")
    public String mealDetail(@PathVariable("id") int id) {
        return "static/meal_detail";
    }

    @GetMapping("/cart")
    @Secured("ROLE_DEVELOPER")
    public String cart() {
        return "static/cart";
    }

    @GetMapping("/order/confirmed")
    @Secured("ROLE_DEVELOPER")
    public String orderConfirmed() {
        return "static/confirmed";
    }

    /*
     * Vendor meal management static pages.
     */
    @GetMapping("/vendor/meals")
    @Secured("ROLE_VENDOR")
    public String vendorMeals() {
        return "static/vendor_meals";
    }

    @GetMapping("/vendor/orders")
    @Secured("ROLE_VENDOR")
    public String vendorOrders() {
        return "static/orders";
    }
}

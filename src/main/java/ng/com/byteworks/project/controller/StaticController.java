package ng.com.byteworks.project.controller;

import ng.com.byteworks.project.service.AuthenticationService;
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
    public String meals() {
        return null;
    }

    @GetMapping("/meal/{id}")
    public String mealDetail(@PathVariable("id") int id) {
        return null;
    }

    @GetMapping("/cart")
    public String cart() {
        return null;
    }

    @GetMapping("/order")
    public String order() {
        return null;
    }

    @GetMapping("/order/purchase")
    public String purchase() {
        return null;
    }

    @GetMapping("/order/confirmed")
    public String orderConfirmed() {
        return null;
    }

    /*
     * Vendor meal management static pages.
     */
    @GetMapping("/vendor/meals")
    public String vendorMeals() {
        return "static/vendor_meals";
    }

    @GetMapping("/vendor/orders")
    public String vendorOrders() {
        return null;
    }
}

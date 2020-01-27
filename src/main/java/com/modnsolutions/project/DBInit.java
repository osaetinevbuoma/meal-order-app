package com.modnsolutions.project;

import com.modnsolutions.project.db.entity.*;
import com.modnsolutions.project.db.repository.*;
import ng.com.byteworks.project.db.entity.*;
import ng.com.byteworks.project.db.repository.*;
import com.modnsolutions.project.enums.RoleEnum;
import com.modnsolutions.project.service.UtilService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnProperty(name = "ng.com.byteworks.db.init", havingValue = "true")
public class DBInit implements CommandLineRunner {
    private final DeliveryTypeRepository deliveryTypeRepository;
    private final PaymentOptionRepository paymentOptionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UtilService utilService;
    /*private final MealOrderRepository mealOrderRepository;*/
    private final MealRepository mealRepository;

    public DBInit(DeliveryTypeRepository deliveryTypeRepository,
                  PaymentOptionRepository paymentOptionRepository, RoleRepository roleRepository,
                  UserRepository userRepository,/*, MealOrderRepository mealOrderRepository,*/
                  MealRepository mealRepository, UtilService utilService) {
        this.deliveryTypeRepository = deliveryTypeRepository;
        this.paymentOptionRepository = paymentOptionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.utilService = utilService;
//        this.mealOrderRepository = mealOrderRepository;
        this.mealRepository = mealRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            System.out.println("=================================================================");
            System.out.println("Initializing database.");

            /*mealRepository.deleteAll();
            mealOrderRepository.deleteAll();*/

            List<Role> roles = roles();
            if (roles.size() > 0) {
                roleRepository.saveAll(roles);
                System.out.println("-- Role table initialized");
            }

            List<DeliveryType> deliveryTypes = deliveryTypes();
            if (deliveryTypes.size() > 0) {
                deliveryTypeRepository.saveAll(deliveryTypes);
                System.out.println("-- Delivery type initialized");
            }

            List<PaymentOption> paymentOptions = paymentOptions();
            if (paymentOptions.size() > 0) {
                paymentOptionRepository.saveAll(paymentOptions);
                System.out.println("-- Payment option initialized");
            }

            User vendor = vendor();
            if (null != vendor) {
                userRepository.save(vendor);
                System.out.println("-- Vendor account created");
            }

            List<Meal> meals = meals();
            if (meals.size() > 0) {
                mealRepository.saveAll(meals);
                System.out.println("-- Meals initialized");
            }

            System.out.println("Database initialization complete.");
            System.out.println("=================================================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Role> roles() {
        List<Role> roles = new ArrayList<>();
        String[] roleArr = { RoleEnum.ROLE_DEVELOPER.toString(), RoleEnum.ROLE_VENDOR.toString() };
        for (String role : roleArr) {
            if (!roleRepository.findByRole(role).isPresent()) {
                roles.add(new Role(role));
            }
        }

        return roles;
    }

    private List<DeliveryType> deliveryTypes() {
        List<DeliveryType> deliveryTypes = new ArrayList<>();
        String[] types = { "Office Delivery", "Pick Up" };
        double[] amounts = { 10, 0 };
        for (int i = 0; i < types.length; i++) {
            if (!deliveryTypeRepository.findByType(types[i]).isPresent()) {
                deliveryTypes.add(new DeliveryType(types[i], amounts[i]));
            }
        }

        return deliveryTypes;
    }

    private List<PaymentOption> paymentOptions() {
        List<PaymentOption> paymentOptions = new ArrayList<>();
        String[] options = { "Card Payment", "Pay On Delivery" };
        double[] discounts = { 2.5, 0 };
        for (int i = 0; i < options.length; i++) {
            if (!paymentOptionRepository.findByOption(options[i]).isPresent()) {
                paymentOptions.add(new PaymentOption(options[i], discounts[i]));
            }
        }

        return paymentOptions;
    }

    private User vendor() {
        Optional<Role> role = roleRepository.findByRole(RoleEnum.ROLE_VENDOR.toString());
        if (!role.isPresent()) return null;

        String firstName = "John";
        String lastName = "Doe";
        String email = "osaetinevbuoma@gmail.com"; // change to desired email
        String password = new BCryptPasswordEncoder().encode("123");

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) return null;

        User user = new User(firstName, lastName, email, password);
        user.setRole(role.get());

        return user;
    }

    private List<Meal> meals() {
        List<Meal> meals = new ArrayList<>();
        String[] names = {
                "Jollof Rice and Grilled Chicken", "Pounded Yam and Efo Riro",
                "Spiced Chicken and Cous-Cous", "Egusi and Pounded Yam",
                "White Rice and Vegetable Gravy", "Jollof Rice and Sauced Chicken",
                "Ila Asepo", "Jollof Rice, Plantain and Grilled Chicken",
                "White Rice, Meat Balls and Plantain", "The Round-House Special",
                "Hawaiian Jollof", "Jollof with Stew-laced Egg", "Indomie Bulgogi",
                "Vegetable Sandwich", "Fresh Organge Juice", "Shawarma Kebab",
                "Strawberry Ice Cream", "Greek Yorghurt", "Strawberry Yorghurt",
                "Chocolate Delight Ice Cream"
        };
        final String IMAGE_PREFIX = "/food/food_";
        String[] images = new String[names.length];
        for (int i = 1; i <= images.length; i++) {
            images[i-1] = IMAGE_PREFIX + i + ".jpg";
        }
        Double[] prices = new Double[names.length];
        for (int i = 1; i <= prices.length; i++) {
            prices[i-1] = (double) utilService.generateRandomNumber(500, 3000);
        }
        String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do " +
                "eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, " +
                "quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu " +
                "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in " +
                "culpa qui officia deserunt mollit anim id est laborum.";

        for (int i = 0; i < names.length; i++) {
            if (!mealRepository.findByName(names[i]).isPresent()) {
                meals.add(new Meal(names[i], images[i], prices[i], description, true));
            }
        }

        return meals;
    }
}

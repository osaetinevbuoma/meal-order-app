package com.modnsolutions.project;

import com.modnsolutions.project.db.entity.Cart;
import com.modnsolutions.project.db.entity.Meal;
import com.modnsolutions.project.db.entity.Role;
import com.modnsolutions.project.db.entity.User;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Utilities {
    private final TestEntityManager entityManager;

    public static final String DEVELOPER_EMAIL = "j.doe@testing.net";
    public static final String PASSWORD = "password";
    public static final String DEVELOPER_FIRST_NAME = "John";
    public static final String DEVELOPER_SURNAME = "Doe";
    public static final String DEVELOPER_ROLE = "ROLE_DEVELOPER";

    public static final String VENDOR_EMAIL = "jane.doe@testing.net";
    public static final String VENDOR_FIRST_NAME = "Jane";
    public static final String VENDOR_SURNAME = "Doe";
    public static final String VENDOR_ROLE = "ROLE_VENDOR";

    public static final String MEAL_NAME = "Amala";
    public static final String MEAL_IMAGE = "image_location";
    public static final Double MEAL_PRICE = 200.50;
    public static final String MEAL_DESCRIPTION = "meal description";
    public static final Boolean MEAL_IS_AVAILABLE = true;

    public static final Integer CART_QUANTITY = 10;

    public Utilities(TestEntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void createDeveloperAccount() {
        Role developerRole = new Role(DEVELOPER_ROLE);
        entityManager.persist(developerRole);

        User developer = new User(DEVELOPER_FIRST_NAME, DEVELOPER_SURNAME, DEVELOPER_EMAIL,
                new BCryptPasswordEncoder().encode(PASSWORD));
        developer.setRole(developerRole);

        entityManager.persist(developer);
        entityManager.flush();
    }

    public void createVendorAccount() {
        Role vendorRole = new Role(VENDOR_ROLE);
        entityManager.persist(vendorRole);

        User vendor = new User(VENDOR_FIRST_NAME, VENDOR_SURNAME, VENDOR_EMAIL,
                new BCryptPasswordEncoder().encode(PASSWORD));
        vendor.setRole(vendorRole);

        entityManager.persist(vendor);
        entityManager.flush();
    }

    public void createMeal() {
        Meal meal = new Meal(MEAL_NAME, MEAL_IMAGE, MEAL_PRICE, MEAL_DESCRIPTION, MEAL_IS_AVAILABLE);
        entityManager.persist(meal);
        entityManager.flush();
    }

    public void createCart(Meal meal, User user) {
        Cart cart = new Cart(CART_QUANTITY);
        cart.setUser(user);
        cart.setMeal(meal);

        entityManager.persist(cart);
        entityManager.flush();
    }
}

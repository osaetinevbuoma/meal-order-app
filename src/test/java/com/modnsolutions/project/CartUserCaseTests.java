package com.modnsolutions.project;

import com.modnsolutions.project.db.entity.Cart;
import com.modnsolutions.project.db.repository.*;
import com.modnsolutions.project.security.WithMockDeveloper;
import com.modnsolutions.project.service.AuthenticationService;
import com.modnsolutions.project.service.CartService;
import com.modnsolutions.project.service.UtilService;
import ng.com.byteworks.project.db.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration
@WithMockDeveloper
public class CartUserCaseTests {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    DeliveryTypeRepository deliveryTypeRepository;

    @Autowired
    MealRepository mealRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PaymentOptionRepository paymentOptionRepository;

    @Autowired
    TestEntityManager entityManager;

    private AuthenticationService authenticationService;
    private CartService cartService;
    private Utilities utilities;
    private UtilService utilService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(userRepository);
        cartService = new CartService(authenticationService, cartRepository, deliveryTypeRepository,
                paymentOptionRepository, utilService);
        utilService = new UtilService();
        utilities = new Utilities(entityManager);
        utilities.createMeal();
    }

    @Test
    void testAddingMealToCart() {
        Cart cart = new Cart(Utilities.CART_QUANTITY);
        cartService.addToCart(cart);

        List<Cart> carts = cartRepository.findAllByUser(authenticationService
                .getAuthenticatedUser().getUser());
        Assertions.assertThat(carts.size()).isGreaterThan(0);
    }
}

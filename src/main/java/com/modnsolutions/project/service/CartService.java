package com.modnsolutions.project.service;

import com.modnsolutions.project.db.entity.Cart;
import com.modnsolutions.project.db.entity.DeliveryType;
import com.modnsolutions.project.db.entity.PaymentOption;
import com.modnsolutions.project.db.entity.User;
import com.modnsolutions.project.db.repository.CartRepository;
import com.modnsolutions.project.db.repository.DeliveryTypeRepository;
import com.modnsolutions.project.db.repository.PaymentOptionRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
@Secured("ROLE_DEVELOPER")
public class CartService {
    private final AuthenticationService authenticationService;
    private final CartRepository cartRepository;
    private final DeliveryTypeRepository deliveryTypeRepository;
    private final PaymentOptionRepository paymentOptionRepository;
    private final UtilService utilService;

    private final static Logger log = Logger.getLogger(CartService.class.getName());

    public CartService(AuthenticationService authenticationService, CartRepository cartRepository,
                       DeliveryTypeRepository deliveryTypeRepository,
                       PaymentOptionRepository paymentOptionRepository, UtilService utilService) {
        this.authenticationService = authenticationService;
        this.cartRepository = cartRepository;
        this.deliveryTypeRepository = deliveryTypeRepository;
        this.paymentOptionRepository = paymentOptionRepository;
        this.utilService = utilService;
    }

    /**
     * Get the developer's list of cart items and their email.
     * Attach list of delivery types and payment options for selection.
     * @return a map of cart items, developer's email, payment options and delivery types
     */
    public Map<String, Object> listCart() {
        Map<String, Object> cartMap = new HashMap<>();
        List<Map<String, Object>> cartList = new ArrayList<>();

        User user = authenticationService.getAuthenticatedUser().getUser();
        List<Cart> carts = cartRepository.findAllByUser(user);
        carts.forEach(cart -> cartList.add(utilService.generateCartMap(cart)));
        cartMap.put("cartItems", cartList);
        cartMap.put("developerEmail", user.getEmail());

        List<Map<String, Object>> types = new ArrayList<>();
        List<DeliveryType> deliveryTypes = deliveryTypeRepository.findAll(Sort.by(Sort.Direction.ASC,
                "type"));
        deliveryTypes.forEach(deliveryType -> {
            Map<String, Object> typeMap = new HashMap<>();
            typeMap.put("id", deliveryType.getId());
            typeMap.put("type", deliveryType.getType());
            typeMap.put("amount", deliveryType.getAmount());
            types.add(typeMap);
        });

        cartMap.put("deliveryOptions", types);

        List<Map<String, Object>> payments = new ArrayList<>();
        List<PaymentOption> paymentOptions = paymentOptionRepository.findAll(Sort.by(
                Sort.Direction.ASC, "option"));
        paymentOptions.forEach(paymentOption -> {
            Map<String, Object> optionMap = new HashMap<>();
            optionMap.put("id", paymentOption.getId());
            optionMap.put("option", paymentOption.getOption());
            optionMap.put("discount", paymentOption.getDiscount());
            payments.add(optionMap);
        });

        cartMap.put("paymentOptions", payments);

        return cartMap;
    }

    public Cart addToCart(Cart cart) {
        Optional<Cart> cartOptional = cartRepository.findByMeal(cart.getMeal());
        if (cartOptional.isPresent()) return null;

        User user = authenticationService.getAuthenticatedUser().getUser();
        cart.setUser(user);
        cartRepository.save(cart);

        return cart;
    }

    public void updateCart(List<Cart> cartList) {
        User user = authenticationService.getAuthenticatedUser().getUser();
        cartList.forEach(cart -> {
            Optional<Cart> cartOptional = cartRepository.findByIdAndUser(cart.getId(), user);
            if (cartOptional.isPresent()) {
                cartOptional.get().setQuantity(cart.getQuantity());
                cartRepository.save(cartOptional.get());
            }
        });
    }

    public void deleteCartItem(int id) {
        User user = authenticationService.getAuthenticatedUser().getUser();
        Optional<Cart> cartOptional = cartRepository.findByIdAndUser(id, user);
        cartOptional.ifPresent(cartRepository::delete);
    }

    public void deleteAllCartItems() {
        User user = authenticationService.getAuthenticatedUser().getUser();
        List<Cart> cartList = cartRepository.findAllByUser(user);
        cartRepository.deleteAll(cartList);
    }
}

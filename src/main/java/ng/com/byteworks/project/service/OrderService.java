package ng.com.byteworks.project.service;

import ng.com.byteworks.project.db.entity.*;
import ng.com.byteworks.project.db.repository.CartRepository;
import ng.com.byteworks.project.db.repository.DeliveryTypeRepository;
import ng.com.byteworks.project.db.repository.OrderRepository;
import ng.com.byteworks.project.db.repository.PaymentOptionRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Secured("ROLE_DEVELOPER")
public class OrderService {
    private final AuthenticationService authenticationService;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final DeliveryTypeRepository deliveryTypeRepository;
    private final OrderRepository orderRepository;
    private final PaymentOptionRepository paymentOptionRepository;

    public OrderService(AuthenticationService authenticationService, CartRepository cartRepository,
                        CartService cartService, DeliveryTypeRepository deliveryTypeRepository,
                        OrderRepository orderRepository,
                        PaymentOptionRepository paymentOptionRepository) {
        this.authenticationService = authenticationService;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.deliveryTypeRepository = deliveryTypeRepository;
        this.orderRepository = orderRepository;
        this.paymentOptionRepository = paymentOptionRepository;
    }

    public Map<String, Object> checkoutInformation() {
        Map<String, Object> checkoutInfo = new HashMap<>();
        checkoutInfo.put("cart", cartService.listCart());

        List<Map<String, Object>> types = new ArrayList<>();
        List<DeliveryType> deliveryTypes = deliveryTypeRepository.findAll(Sort.by(Sort.Direction.ASC,
                "type"));
        deliveryTypes.forEach(deliveryType -> {
            Map<String, Object> typeMap = new HashMap<>();
            typeMap.put("id", deliveryType.getId());
            typeMap.put("type", deliveryType.getType());
            types.add(typeMap);
        });

        checkoutInfo.put("delivery", types);

        List<Map<String, Object>> payments = new ArrayList<>();
        List<PaymentOption> paymentOptions = paymentOptionRepository.findAll(Sort.by(
                Sort.Direction.ASC, "option"));
        paymentOptions.forEach(paymentOption -> {
            Map<String, Object> optionMap = new HashMap<>();
            optionMap.put("id", paymentOption.getId());
            optionMap.put("option", paymentOption.getOption());
            payments.add(optionMap);
        });

        checkoutInfo.put("payment", payments);

        return checkoutInfo;
    }

    public void saveOrder(List<Order> orders) {
        User user = authenticationService.getAuthenticatedUser().getUser();
        List<Cart> carts = cartRepository.findAllByUser(user);
        cartRepository.deleteAll(carts);
        orderRepository.saveAll(orders);
    }
}

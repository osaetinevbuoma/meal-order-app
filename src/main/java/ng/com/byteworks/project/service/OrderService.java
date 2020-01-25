package ng.com.byteworks.project.service;

import ng.com.byteworks.project.db.entity.Cart;
import ng.com.byteworks.project.db.entity.Order;
import ng.com.byteworks.project.db.entity.User;
import ng.com.byteworks.project.db.repository.CartRepository;
import ng.com.byteworks.project.db.repository.OrderRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Secured("ROLE_DEVELOPER")
public class OrderService {
    private final AuthenticationService authenticationService;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    public OrderService(AuthenticationService authenticationService, CartRepository cartRepository,
                        OrderRepository orderRepository) {
        this.authenticationService = authenticationService;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }

    public void saveOrder(List<Order> orders) {
        User user = authenticationService.getAuthenticatedUser().getUser();
        orders.forEach(order -> order.setUser(user));

        List<Cart> carts = cartRepository.findAllByUser(user);
        cartRepository.deleteAll(carts);
        orderRepository.saveAll(orders);
    }
}

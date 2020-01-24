package ng.com.byteworks.project.service;

import ng.com.byteworks.project.db.entity.Cart;
import ng.com.byteworks.project.db.entity.User;
import ng.com.byteworks.project.db.repository.CartRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Secured("ROLE_DEVELOPER")
public class CartService {
    private final AuthenticationService authenticationService;
    private final CartRepository cartRepository;
    private final UtilService utilService;

    public CartService(AuthenticationService authenticationService,
                       CartRepository cartRepository, UtilService utilService) {
        this.authenticationService = authenticationService;
        this.cartRepository = cartRepository;
        this.utilService = utilService;
    }

    public List<Map<String, Object>> listCart() {
        List<Map<String, Object>> cartList = new ArrayList<>();

        User user = authenticationService.getAuthenticatedUser().getUser();
        List<Cart> carts = cartRepository.findAllByUser(user);
        carts.forEach(cart -> cartList.add(utilService.generateCartMap(cart)));

        return cartList;
    }

    public Cart addToCart(Cart cart) {
        Optional<Cart> cartOptional = cartRepository.findByMeal(cart.getMeal());
        if (cartOptional.isPresent()) return null;

        User user = authenticationService.getAuthenticatedUser().getUser();
        cart.setUser(user);
        cartRepository.save(cart);

        return cart;
    }
}

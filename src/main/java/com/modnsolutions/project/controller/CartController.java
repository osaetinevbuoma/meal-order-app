package com.modnsolutions.project.controller;

import com.modnsolutions.project.db.entity.Cart;
import com.modnsolutions.project.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@Secured("ROLE_DEVELOPER")
@RequestMapping("/api")
public class CartController {
    private final CartService cartService;

    private final static Logger log = Logger.getLogger(CartController.class.getName());

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping(value = "/cart", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> cart() {
        return ResponseEntity.ok(cartService.listCart());
    }

    @PostMapping(value = "/cart/add", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addToCart(@RequestBody Cart cart) {
        if (null == cart) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        log.info(cart.toString());

        cart = cartService.addToCart(cart);
        if (null == cart) return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Meal already added to cart");

        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/cart/update", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCart(@RequestBody List<Cart> carts) {
        cartService.updateCart(carts);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/cart/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCart(@PathVariable("id") String id) {
        if (id.equals("all")) cartService.deleteAllCartItems();
        else cartService.deleteCartItem(Integer.parseInt(id));
        return ResponseEntity.ok().build();
    }
}

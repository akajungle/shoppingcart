package com.shop;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.common.requests.ResponseHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {
	@Autowired
    private CartService cartService;

	@PostMapping(path = "/cart")
    public ResponseEntity addCart(@RequestParam(required=false) Long cartId, @RequestBody List<CartDetail> cartDetails) throws Exception {
        
        return new ResponseEntity<>(cartService.saveCart(cartId, cartDetails), HttpStatus.OK);
    }


    @PostMapping(path="/checkout")
    public ResponseEntity checkoutCart(@RequestParam Long cartId) throws Exception {
		/*HttpHeaders header = new HttpHeaders();
		header.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:8080");*/
        cartService.checkout(cartId);
        return new ResponseEntity<>("Checked out", HttpStatus.OK);
    }

    @GetMapping(path="/cart/{cartId}")
    public ResponseEntity getCart(@PathVariable Long cartId) throws Exception {
        Cart cart = cartService.getCart(cartId);
        if (cart == null) 
        	throw new Exception("Unable locate shopping cart with id"+cartId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
}

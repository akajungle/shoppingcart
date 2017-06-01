package com.shop;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Service
public class CartService {
	private final Log log = LogFactory.getLog(CartService.class);
    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartDetailRepository cartDetailRepository;
    
    @Transactional
    public Cart saveCart(Long cartId, List<CartDetail> details) throws Exception {
    	if (cartId == null) {
    		final Cart cart = new Cart();
    		cart.setCartDetails(details);
    		details.stream().forEach(d -> d.setCart(cart));
    		cartRepository.save(cart);
    		return cart;
    	}
    	else {
    		final Cart cart = cartRepository.findCartById(cartId);
    		final Set<CartDetail> result = new HashSet<CartDetail>();
    		cart.getCartDetails().stream().forEach(detail -> {
    			System.out.println("-----------"+detail+"???????????"+details);
        		details.stream()
        		.forEach((d) -> {
        			System.out.println("+++++++++++++++"+d+"-----------"+detail);
        					if (detail.getId().equals(d.getId())) {
        						detail.setQuantity(d.getQuantity());
        						result.add(detail);
        					}
        					else {
        						d.setCart(cart);
        						result.add(d);
        					}
        					
        		});
        	});
    		cart.setCartDetails(result);
    		cartRepository.save(cart);
    		return cart;
    	}
    }

    public Cart getCart(Long cartId) throws Exception {
    	return cartRepository.findCartById(cartId);
    }

    public void checkout(Long cartId) throws Exception {
    	Cart cart = cartRepository.findCartById(cartId);
    	String productIds = cart.getCartDetails().stream().map(d -> String.valueOf(d.getProductId())).collect(Collectors.joining(","));
    	List<Product> productList = Arrays.asList(restTemplate.getForObject("http://localhost:8081/product/inventory?ids="+productIds, Product[].class));
    	
    	StringJoiner sj = new StringJoiner(",");
    	cart.getCartDetails().stream().forEach(detail -> {
    		productList.stream()
    		.filter(p -> p.getId().equals(detail.getId()))
    		.forEach((p) -> {
    					if (p.getQuantity()<detail.getQuantity()) {
    						sj.add(p.getName().toString());
    					}
    					else {
    						p.setQuantity(p.getQuantity()-detail.getQuantity());
    					}
    		});
    	});
    	
    	if (0 == sj.length()) {
    		restTemplate.put("http://localhost:8081/product/",productList);
    		return;
    	}
    	throw new Exception("Not enough inventory for product "+sj.toString());
    }
}

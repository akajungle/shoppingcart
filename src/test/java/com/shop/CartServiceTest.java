package com.shop;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration
public class CartServiceTest {

	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	CartDetailRepository cartDetailRepository;
	
	@Autowired
	CartService service;
	
	@Before
	public void before() {
		cartRepository.deleteAll();
		cartDetailRepository.deleteAll();
	}
	
	@Test 
	public void checkoutTest() throws Exception {
		CartDetail cd = new CartDetail();
		cd.setPrice(1);
		cd.setProductId(1);
		cd.setQuantity(1);
		ArrayList<CartDetail> list = new ArrayList<>();
		list.add(cd);
		Cart cart = service.saveCart(new Long(-1), list);
		assertTrue(cartRepository.findCartById(cart.getId()).getId().equals(cart.getId()));
		
		cart = service.getCart(cart.getId());
		assertTrue(cart.getCartDetails().size() == cartRepository.findCartById(cart.getId()).getCartDetails().size());
	}
}

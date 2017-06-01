package com.shop;

import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, String>{
	
	Cart findCartById(Long id);

}
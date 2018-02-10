package net.vargadaniel.re.ordermanager;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import net.vargadaniel.re.ordermanager.model.Product;

@Component
public interface ProductRepository extends MongoRepository<Product, String> {
	
	Product findByName(@Param("name") String name);

}
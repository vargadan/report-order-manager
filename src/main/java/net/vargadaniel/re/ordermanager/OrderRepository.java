package net.vargadaniel.re.ordermanager;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import net.vargadaniel.re.ordermanager.model.Order;

@Component
public interface OrderRepository extends MongoRepository<Order, String> {
}

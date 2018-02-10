package net.vargadaniel.re.ordermanager;

import java.util.List;
import java.util.stream.Collectors;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.vargadaniel.re.ordermanager.ex.OrderExistsException;
import net.vargadaniel.re.ordermanager.model.Order;
import net.vargadaniel.re.ordermanager.model.OrderStatus;
import net.vargadaniel.re.ordermanager.model.Product;
import net.vargadaniel.re.ordermanager.vo.Converter;
import net.vargadaniel.re.ordermanager.vo.OrderVO;
import net.vargadaniel.re.ordermanager.vo.ProductVO;

@RestController
public class OrderRestController {
	
	static Logger log = LoggerFactory.getLogger(OrderRestController.class);
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ReportEngine reportEngine;
	
	@Autowired
	OrderValidator validator;
	
	@RequestMapping(value="/orders", method=RequestMethod.GET)
	Iterable<OrderVO> findAllOrders() {
		return orderRepository.findAll().stream().map(Converter::convertOrder).collect(Collectors.toList());
	}
	
	@RequestMapping(value="/products", method=RequestMethod.GET)
	Iterable<ProductVO> findAllProducts() {
		List<Product> products = productRepository.findAll();
		return products.stream().map(Converter::converProduct).collect(Collectors.toList());
	}
	
	@RequestMapping(value="/orders", method=RequestMethod.POST)
	OrderVO createOrder(@RequestBody OrderVO orderVO) throws OrderExistsException {
		Order order = Converter.convertOrder(orderVO);
		log.info("Processing order : " + order);
		if (order.getId() != null && orderRepository.exists(order.getId())) {
			throw new OrderExistsException();
		} else {
			order.setTimestamp(System.currentTimeMillis());
			order.getStatuses().add(new OrderStatus(System.currentTimeMillis(), "order received"));
			if (orderVO.getProduct() != null) {
				Product product = productRepository.findOne(orderVO.getProduct().getId());
				order.setProduct(product);
			}
			validator.validate(order);
			if (order.getProperties() != null) {
				JSONObject rawSchema = new JSONObject(order.getProduct().getPropertiesSchema().toString());
				Schema schema = SchemaLoader.load(rawSchema);
				JSONObject propsJson = new JSONObject(order.getProperties().toString());
				schema.validate(propsJson);
			}
			order = orderRepository.save(order);
			if (order.getProduct().getName() == null) {
				throw new NullPointerException("product.name cannot be null!");
			}
			log.info("order.product.name : " + order.getProduct().getName());
			reportEngine.orders().send(MessageBuilder.withPayload(order).setHeader("productName", order.getProduct().getName()).build());
			return Converter.convertOrder(order);
		}
	}

}
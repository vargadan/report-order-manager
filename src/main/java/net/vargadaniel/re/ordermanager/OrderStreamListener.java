package net.vargadaniel.re.ordermanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

import net.vargadaniel.re.ordermanager.model.Order;
import net.vargadaniel.re.ordermanager.model.OrderStatus;
import net.vargadaniel.re.ordermanager.model.Product;
import net.vargadaniel.re.ordermanager.vo.Converter;
import net.vargadaniel.re.ordermanager.vo.ProductVO;
import net.vargadaniel.re.ordermanager.vo.StatusUpdateVO;

@EnableBinding(ReportEngine.class)
public class OrderStreamListener {
	
	static Logger log = LoggerFactory.getLogger(OrderStreamListener.class);
	
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ReportEngine reportEngine;	
	
	@Autowired
	OrderValidator validator;
	
	@StreamListener(ReportEngine.PRODUCTS)
	public void registerProduct(ProductVO productVO) {
		if (productRepository.findByName(productVO.getName()) == null) {
			Product product = Converter.converProduct(productVO);
			validator.validate(product);
			productRepository.save(product);
		}
	}
	
	@StreamListener(ReportEngine.STATUS_UPDATES)
	public void updateStatus(Message<StatusUpdateVO> statusUpdateMsg) {
		StatusUpdateVO statusUpdate = statusUpdateMsg.getPayload();
		log.info("statusUpdate : " + statusUpdate);
		Order order = orderRepository.findOne(statusUpdate.getOrderId());
		Long timestamp = statusUpdateMsg.getHeaders().getTimestamp();
		order.getStatuses().add(new OrderStatus(timestamp, statusUpdate.getStatus()));
		orderRepository.save(order);
	}

}

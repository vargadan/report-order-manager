package net.vargadaniel.re.ordermanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CleanupManager {
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private OrderRepository orderRepo;	
	
	public void cleanUpAll() {
		orderRepo.deleteAll();
		productRepo.deleteAll();
	}
	
}

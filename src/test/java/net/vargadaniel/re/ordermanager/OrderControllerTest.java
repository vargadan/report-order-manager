package net.vargadaniel.re.ordermanager;

import javax.validation.ConstraintViolationException;

import org.everit.json.schema.ValidationException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import net.vargadaniel.re.ordermanager.OrderManagerApp;
import net.vargadaniel.re.ordermanager.OrderRestController;
import net.vargadaniel.re.ordermanager.OrderStreamListener;
import net.vargadaniel.re.ordermanager.ex.OrderExistsException;
import net.vargadaniel.re.ordermanager.vo.Converter;
import net.vargadaniel.re.ordermanager.vo.OrderVO;
import net.vargadaniel.re.ordermanager.vo.ProductVO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderManagerApp.class)
@WebAppConfiguration
public class OrderControllerTest {
	
	@Autowired
	OrderRestController orderController;
	
	@Autowired
	OrderStreamListener orderListener;
	
	ProductVO product;
	
	@Before
	public void init() throws Exception {
		product = TestDataFactory.createDummyReportProduct();
		orderListener.registerProduct(product);
		product = orderController.findAllProducts().iterator().next();
	}
	
	@Test
	public void testCreate() throws Exception {
		OrderVO order = new OrderVO(System.currentTimeMillis(), product);
		order.setProperties(Converter.toJsonNode("{\"from\":\"01/01/2016\",\"to\":\"31/12/2016\",\"cif\":\"1234567890\"}"));
		order = orderController.createOrder(order);
		Assert.assertNotNull(order);
		Assert.assertEquals(product.getId(), order.getProduct().getId());
		Assert.assertEquals("dummyReport", order.getProduct().getName());
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void testNoProduct() throws OrderExistsException {
		orderController.createOrder(new OrderVO(null, null));
	}
	
	@Test(expected=ValidationException.class)
	public void testInvalidProperties() throws Exception {
		OrderVO order = new OrderVO(System.currentTimeMillis(), product);
		order.setProperties(Converter.toJsonNode("{\"x\":\"01/01/2016\",\"y\":\"31/12/2016\",\"z\":\"1234567890\"}"));
		order = orderController.createOrder(order);
	}
	
	@Autowired
	private CleanupManager cleanupManager; 
    
	@After
	public void cleanUp() {
		cleanupManager.cleanUpAll();
	}

}

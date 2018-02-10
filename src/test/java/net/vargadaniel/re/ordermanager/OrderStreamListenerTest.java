package net.vargadaniel.re.ordermanager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import net.vargadaniel.re.ordermanager.vo.ProductVO;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderManagerApp.class)
@WebAppConfiguration
public class OrderStreamListenerTest {
	
	@Autowired
	private OrderStreamListener orderListener;
	
	@Autowired
	private OrderRestController orderController;

	@Test
	public void testSaveProduct() throws Exception {
		{
			ProductVO productVO = TestDataFactory.createDummyReportProduct();
			orderListener.registerProduct(productVO);
		}
		ProductVO product = orderController.findAllProducts().iterator().next();
		Assert.assertNotNull(product);
		Assert.assertNotNull(product.getId());
		Assert.assertNotNull(product.getName());
		Assert.assertNotNull(product.getPropertiesSchema());
		Assert.assertEquals("dummyReport", product.getName());
		Assert.assertEquals("object", product.getPropertiesSchema().get("type").asText());
	}
	
	@Autowired
	private CleanupManager cleanupManager; 
    
	@After
	public void cleanUp() {
		cleanupManager.cleanUpAll();
	}
	

}

package net.vargadaniel.re.ordermanager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.mongodb.util.JSON;

import net.vargadaniel.re.ordermanager.OrderManagerApp;
import net.vargadaniel.re.ordermanager.OrderRestController;
import net.vargadaniel.re.ordermanager.OrderStreamListener;
import net.vargadaniel.re.ordermanager.vo.Converter;
import net.vargadaniel.re.ordermanager.vo.OrderVO;
import net.vargadaniel.re.ordermanager.vo.ProductVO;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderManagerApp.class)
@WebAppConfiguration
public class OrderControllerRestTest {

	MockMvc mockMvc;
	
	@Autowired
	private OrderRestController orderController;
	
	@Autowired
	private OrderStreamListener orderListener;
	
	ProductVO product;
	
	@Before
	public void init() throws Exception {
		product = TestDataFactory.createDummyReportProduct();
		orderListener.registerProduct(product);
		product = orderController.findAllProducts().iterator().next();
		mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
	}
	
	@Test
	public void testOrderCreate() throws Exception {
		OrderVO order = new OrderVO(null, product);
		order.setProperties(Converter.toJsonNode("{\"from\":\"01/01/2016\",\"to\":\"31/12/2016\",\"cif\":\"1234567890\"}"));
		String orderJson = asJsonString(order);
		MvcResult result = mockMvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON).content(orderJson)).andReturn();
		OrderVO resultOrder = asObject(result.getResponse().getContentAsByteArray(), OrderVO.class);
		Assert.assertNotNull(resultOrder);
		Assert.assertNotNull(resultOrder.getTimestamp());
		Assert.assertNotNull(resultOrder.getStatuses());
		Assert.assertNotNull(resultOrder.getProduct());
		Assert.assertEquals(product.getId(), resultOrder.getProduct().getId());
		Assert.assertEquals(product.getName(), resultOrder.getProduct().getName());
	}
	
    /*
     * converts a Java object into JSON representation
     */
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static <T> T asObject(final byte[] json, Class<T> clazz) {
        try {
            return new ObjectMapper().readerFor(clazz).readValue(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	@Autowired
	private CleanupManager cleanupManager; 
    
	@After
	public void cleanUp() {
		cleanupManager.cleanUpAll();
	}
	
}

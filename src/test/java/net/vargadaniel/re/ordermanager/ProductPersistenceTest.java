package net.vargadaniel.re.ordermanager;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import net.vargadaniel.re.ordermanager.model.Product;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderManagerApp.class)
@WebAppConfiguration
public class ProductPersistenceTest {
	
	@Autowired
	ProductRepository repo;
	
	@Test
	public void testSaveProduct() throws Exception {
		String json =
			        "{" +
			        "   \"title\": \"High-Performance Java Persistence\"," +
			        "   \"author\": \"Vlad Mihalcea\"," +
			        "   \"publisher\": \"Amazon\"," +
			        "   \"price\": 44.99," +
			        "   \"url\": \"https://www.amazon.com/High-Performance-Java-Persistence-Vlad-Mihalcea/dp/973022823X/\"" +
			        "}";
		
		DBObject props = (DBObject) JSON.parse(json);
		
		Product product1 = new Product("hello");
		product1.setPropertiesSchema(props);
		
		repo.save(product1);
		
		Product product2 = repo.findByName("hello");
		
		Assert.assertNotNull(product2);
		Assert.assertNotNull(product2.getPropertiesSchema());
	}
	
	@After
	public void cleanUp() {
		repo.deleteAll();
	}
	

}

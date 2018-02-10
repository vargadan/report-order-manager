package net.vargadaniel.re.ordermanager;

import java.io.InputStream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.vargadaniel.re.ordermanager.vo.ProductVO;

public class TestDataFactory {
	
	static public ProductVO createDummyReportProduct() throws Exception {
		ProductVO product = new ProductVO("dummyReport");
		try (InputStream inputStream = TestDataFactory.class.getResourceAsStream("/DummyReportSchema.json")) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode properties = mapper.readTree(inputStream);
			product.setPropertiesSchema(properties);
		}
		return product;
	}

}

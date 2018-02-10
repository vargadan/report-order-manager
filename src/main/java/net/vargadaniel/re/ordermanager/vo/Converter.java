package net.vargadaniel.re.ordermanager.vo;

import java.io.IOException;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import net.vargadaniel.re.ordermanager.model.Order;
import net.vargadaniel.re.ordermanager.model.Product;
import net.vargadaniel.re.ordermanager.vo.OrderVO.StatusVO;

public class Converter {
	
	public static JsonNode toJsonNode(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readTree(json);
	}
	
	public static Product converProduct(ProductVO productVO) {
		if (productVO == null) {
			return null;
		}
		Product product = new Product();
		product.setId(productVO.getId());
		product.setName(productVO.getName());
		if (productVO.getPropertiesSchema() != null) {
			DBObject properties = (DBObject) JSON.parse(productVO.getPropertiesSchema().toString());
			if (properties != null && properties.containsField("$schema")) {
				properties.removeField("$schema");
				properties.put("_$schema", productVO.getPropertiesSchema().findValuesAsText("$schema"));				
			}
			product.setPropertiesSchema(properties);
		};
		return product;
	}
	
	public static ProductVO converProduct(Product product) {
		if (product == null) {
			return null;
		}
		ProductVO vo = new ProductVO(); 
		vo.setId(product.getId());
		vo.setName(product.getName());
		ObjectMapper mapper = new ObjectMapper();
		DBObject props = product.getPropertiesSchema();
		if (props != null) {
			if (props.containsField("_$schema")) {
				Object schemaVal = props.get("_$schema");
				props.removeField("_$schema");
				props.put("$schema", schemaVal);
			}
			try {
				vo.setPropertiesSchema(mapper.readTree(props.toString()));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return vo;
	}
	
	public static Order convertOrder(OrderVO orderVO) {
		Order order = new Order();
		order.setId(orderVO.getId());
		order.setProduct(converProduct(orderVO.getProduct()));
		if (orderVO.getProperties() != null) {
			DBObject properties = (DBObject) JSON.parse(orderVO.getProperties().toString());
			order.setProperties(properties);
		}
		return order;
	}
	
	public static OrderVO convertOrder(Order order) {
		if (order == null) {
			return null;
		}
		OrderVO vo = new OrderVO();
		vo.setId(order.getId());
		vo.setTimestamp(order.getTimestamp());
		vo.setProduct(converProduct(order.getProduct()));
		ObjectMapper mapper = new ObjectMapper();
		DBObject props = order.getProperties();
		if (props != null) {
			try {
				vo.setProperties(mapper.readTree(props.toString()));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		vo.setStatuses(order.getStatuses().stream().map(status -> {
			StatusVO statusVO = new StatusVO();
			statusVO.setStatus(status.getStatus());
			return statusVO;
		}).collect(Collectors.toList()));
		return vo;
	}

}

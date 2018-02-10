package net.vargadaniel.re.ordermanager.vo;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.databind.JsonNode;

public class OrderVO {
	
	public static class StatusVO {
		
		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
		}
		
		private String status;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
		
	}
	
	public OrderVO() {
	}
	
	public OrderVO(Long timestamp, ProductVO product) {
		this();
		this.product = product; 
	}

	private String id;
	
	private Long timestamp;
	
	private ProductVO product;
	
	private JsonNode properties;

	private List<StatusVO> statuses;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public ProductVO getProduct() {
		return product;
	}

	public void setProduct(ProductVO product) {
		this.product = product;
	}

	public JsonNode getProperties() {
		return properties;
	}

	public void setProperties(JsonNode properties) {
		this.properties = properties;
	}

	public Iterable<StatusVO> getStatuses() {
		return statuses;
	}

	protected void setStatuses(List<StatusVO> statuses) {
		this.statuses = statuses;
	}
}

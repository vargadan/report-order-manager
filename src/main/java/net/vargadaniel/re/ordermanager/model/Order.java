package net.vargadaniel.re.ordermanager.model;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Id;

import com.mongodb.DBObject;

public class Order {
	
	public Order() {
	}

	public Order(Long timestamp, Product product) {
		this.timestamp = timestamp;
		this.product = product;
	}

	String id;
	
	Long timestamp;
	
	Product product;
	
	DBObject properties;

	private SortedSet<OrderStatus> statuses = new TreeSet<>();

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	@NotNull
	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@NotNull
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

	@NotNull
	public DBObject getProperties() {
		return properties;
	}

	public void setProperties(DBObject properties) {
		this.properties = properties;
	}

	public SortedSet<OrderStatus> getStatuses() {
		return statuses;
	}

	public void setStatuses(SortedSet<OrderStatus> statuses) {
		this.statuses = statuses;
	}

}

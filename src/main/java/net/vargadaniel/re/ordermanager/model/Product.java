package net.vargadaniel.re.ordermanager.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Id;

import com.mongodb.DBObject;

public class Product {
	
	public Product(String name) {
		this.name = name;
	}

	public Product() {
	}

	@Id
	private String id;

	private String name;
	
    private DBObject propertiesSchema;
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

	public DBObject getPropertiesSchema() {
		return propertiesSchema;
	}

	public void setPropertiesSchema(DBObject propertiesSchema) {
		this.propertiesSchema = propertiesSchema;
	}
	
}

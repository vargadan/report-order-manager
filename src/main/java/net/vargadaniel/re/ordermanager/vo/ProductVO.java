package net.vargadaniel.re.ordermanager.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.databind.JsonNode;

public class ProductVO {
	
	public ProductVO(String name) {
		this.name = name;
	}

	public ProductVO() {
	}
	
	private String id;

	private String name;
	
    private JsonNode propertiesSchema;
    
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

	public JsonNode getPropertiesSchema() {
		return propertiesSchema;
	}

	public void setPropertiesSchema(JsonNode propertiesSchema) {
		this.propertiesSchema = propertiesSchema;
	}
	
}

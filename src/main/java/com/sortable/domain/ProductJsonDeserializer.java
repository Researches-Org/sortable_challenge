package com.sortable.domain;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class ProductJsonDeserializer implements JsonDeserializer<Product> {

	@Override
	public Product deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {

		JsonObject jsonObject = json.getAsJsonObject();

		String productName = jsonObject.get("product_name").getAsString();
		String manufacturer = jsonObject.get("manufacturer").getAsString();

		JsonElement jsonElementFamily = json.getAsJsonObject().get("family");

		String family = jsonElementFamily != null ? jsonElementFamily
				.getAsString() : null;

		String model = jsonObject.get("model").getAsString();

		String announcedDate = jsonObject.get("announced-date").getAsString();

		return new Product(productName, manufacturer, family, model,
				announcedDate);
	}
}

package com.awesomeaem.geo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.awesomeaem.geo.services.impl.JsonLdSchemaServiceImpl;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@DisplayName("JsonLdSchemaServiceImpl - Unit Tests")
class JsonLdSchemaServiceImplTest {

    private final JsonLdSchemaServiceImpl service = new JsonLdSchemaServiceImpl();

    @Test
    @DisplayName("FAQPage schema should use question/answer from faqItems JSON")
    void faqPageUsesFaqItems() {
        Map<String, Object> map = new HashMap<>();
        map.put("schemaType", "FAQPage");
        map.put("faqItems", "[{\"question\":\"Q1\",\"answer\":\"A1\"}]");
        ValueMap properties = new ValueMapDecorator(map);

        String json = service.generateSchemaFromProperties(properties);
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

        assertEquals("FAQPage", obj.get("@type").getAsString());
        JsonArray mainEntity = obj.getAsJsonArray("mainEntity");
        assertNotNull(mainEntity);
        assertEquals(1, mainEntity.size());
        JsonObject q = mainEntity.get(0).getAsJsonObject();
        assertEquals("Q1", q.get("name").getAsString());
        assertEquals("A1", q.getAsJsonObject("acceptedAnswer").get("text").getAsString());
    }

    @Test
    @DisplayName("HowTo schema should use howToSteps JSON")
    void howToUsesSteps() {
        Map<String, Object> map = new HashMap<>();
        map.put("schemaType", "HowTo");
        map.put("jcr:title", "How to test");
        map.put("howToSteps", "[{\"name\":\"Step A\",\"text\":\"Do it\"}]");
        ValueMap properties = new ValueMapDecorator(map);

        String json = service.generateSchemaFromProperties(properties);
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

        assertEquals("HowTo", obj.get("@type").getAsString());
        JsonArray steps = obj.getAsJsonArray("step");
        assertNotNull(steps);
        assertEquals(1, steps.size());
        assertEquals("Do it", steps.get(0).getAsJsonObject().get("text").getAsString());
    }

    @Test
    @DisplayName("BreadcrumbList schema should use breadcrumbItems JSON")
    void breadcrumbUsesItems() {
        Map<String, Object> map = new HashMap<>();
        map.put("schemaType", "BreadcrumbList");
        map.put("breadcrumbItems", "[{\"name\":\"Home\",\"item\":\"/content/home.html\"}]");
        ValueMap properties = new ValueMapDecorator(map);

        String json = service.generateSchemaFromProperties(properties);
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

        assertEquals("BreadcrumbList", obj.get("@type").getAsString());
        JsonArray items = obj.getAsJsonArray("itemListElement");
        assertNotNull(items);
        assertEquals(1, items.size());
    }

    @Test
    @DisplayName("Product schema should build offers from price/currency")
    void productOffersFromPriceCurrency() {
        Map<String, Object> map = new HashMap<>();
        map.put("schemaType", "Product");
        map.put("productName", "Widget");
        map.put("offerPrice", "9.99");
        map.put("offerCurrency", "USD");
        ValueMap properties = new ValueMapDecorator(map);

        String json = service.generateSchemaFromProperties(properties);
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

        assertEquals("Product", obj.get("@type").getAsString());
        JsonObject offers = obj.getAsJsonObject("offers");
        assertNotNull(offers);
        assertEquals("Offer", offers.get("@type").getAsString());
        assertEquals("9.99", offers.get("price").getAsString());
    }

    @Test
    @DisplayName("Schema generation should return empty when required fields are missing")
    void schemaGenerationSkipsWhenRequiredMissing() {
        Map<String, Object> map = new HashMap<>();
        map.put("schemaType", "FAQPage");
        map.put("faqItems", "[]");
        ValueMap properties = new ValueMapDecorator(map);

        String json = service.generateSchemaFromProperties(properties);
        assertTrue(json.isEmpty());
    }

    @Test
    @DisplayName("Validation should fail for Product missing name and offers")
    void productValidationFailsWithoutRequiredFields() {
        Map<String, Object> map = new HashMap<>();
        ValueMap properties = new ValueMapDecorator(map);

        assertFalse(service.validateSchema("Product", properties));
    }

    @Test
    @DisplayName("Validation should pass for Product with name and offer")
    void productValidationPassesWithRequiredFields() {
        Map<String, Object> map = new HashMap<>();
        map.put("productName", "Widget");
        map.put("offerPrice", "9.99");
        map.put("offerCurrency", "USD");
        ValueMap properties = new ValueMapDecorator(map);

        assertTrue(service.validateSchema("Product", properties));
    }
}

package com.awesomeaem.geo.services.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;

import com.awesomeaem.geo.services.JsonLdSchemaService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of JSON-LD Schema Service
 * 
 * Generates Schema.org JSON-LD markup for various content types.
 */
@Slf4j
@Component(service = JsonLdSchemaService.class)
public class JsonLdSchemaServiceImpl implements JsonLdSchemaService {

    private static final String SCHEMA_CONTEXT = "https://schema.org";
    
    private static final Set<String> SUPPORTED_TYPES = new HashSet<>(Arrays.asList(
        "Article", "NewsArticle", "BlogPosting", 
        "FAQPage", "HowTo", "Product", 
        "Organization", "BreadcrumbList", "Person", "WebPage"
    ));

    private static final Map<String, Set<String>> REQUIRED_FIELDS = new HashMap<>();
    static {
        REQUIRED_FIELDS.put("Article", Set.of("headline", "author", "datePublished"));
        REQUIRED_FIELDS.put("NewsArticle", Set.of("headline", "author", "datePublished"));
        REQUIRED_FIELDS.put("BlogPosting", Set.of("headline", "author", "datePublished"));
        REQUIRED_FIELDS.put("FAQPage", Set.of("mainEntity"));
        REQUIRED_FIELDS.put("HowTo", Set.of("name", "step"));
        REQUIRED_FIELDS.put("Product", Set.of("name", "offers"));
        REQUIRED_FIELDS.put("Organization", Set.of("name"));
        REQUIRED_FIELDS.put("BreadcrumbList", Set.of("itemListElement"));
    }

    @Override
    public String generateSchema(com.adobe.cq.wcm.core.components.models.Page page, Resource content) {
        if (page == null && content == null) {
            return "";
        }

        ValueMap properties = null;
        if (content != null) {
            properties = content.getValueMap();
        } else if (page != null) {
            JsonObject schema = new JsonObject();
            schema.addProperty("@context", SCHEMA_CONTEXT);
            schema.addProperty("@type", "WebPage");
            if (StringUtils.isNotBlank(page.getTitle())) {
                schema.addProperty("name", page.getTitle());
            }
            if (StringUtils.isNotBlank(page.getDescription())) {
                schema.addProperty("description", page.getDescription());
            }
            if (page.getCanonicalLink() != null) {
                schema.addProperty("url", page.getCanonicalLink());
            }
            return schema.toString();
        }

        return generateSchemaFromProperties(properties);
    }

    @Override
    public String generateSchemaFromProperties(ValueMap properties) {
        if (properties == null) {
            return "";
        }

        String schemaType = properties.get("schemaType", String.class);
        if (StringUtils.isBlank(schemaType)) {
            return "";
        }

        if (!isSupported(schemaType)) {
            schemaType = "WebPage";
        }

        JsonObject schema = new JsonObject();
        schema.addProperty("@context", SCHEMA_CONTEXT);
        schema.addProperty("@type", schemaType);

        switch (schemaType) {
            case "Article":
            case "NewsArticle":
            case "BlogPosting":
                addArticleFields(schema, properties, schemaType);
                break;
            case "FAQPage":
                addFaqPageFields(schema, properties);
                break;
            case "HowTo":
                addHowToFields(schema, properties);
                break;
            case "Product":
                addProductFields(schema, properties);
                break;
            case "Organization":
                addOrganizationFields(schema, properties);
                break;
            case "BreadcrumbList":
                addBreadcrumbFields(schema, properties);
                break;
            case "WebPage":
            default:
                addWebPageFields(schema, properties);
                break;
        }

        if (!validateSchemaOutput(schemaType, schema)) {
            return "";
        }

        return schema.toString();
    }

    private void addArticleFields(JsonObject schema, ValueMap properties, String type) {
        String headline = properties.get("jcr:title", String.class);
        if (StringUtils.isNotBlank(headline)) {
            schema.addProperty("headline", headline);
        }

        String description = properties.get("jcr:description", String.class);
        if (StringUtils.isNotBlank(description)) {
            schema.addProperty("description", description);
        }

        String author = properties.get("authorName", String.class);
        if (StringUtils.isNotBlank(author)) {
            JsonObject authorObj = new JsonObject();
            authorObj.addProperty("@type", "Person");
            authorObj.addProperty("name", author);
            schema.add("author", authorObj);
        }

        Object publishDate = properties.get("publishDate");
        if (publishDate != null) {
            schema.addProperty("datePublished", formatDate(publishDate));
        }

        Object lastModified = properties.get("jcr:lastModified");
        if (lastModified != null) {
            schema.addProperty("dateModified", formatDate(lastModified));
        }
    }

    private void addFaqPageFields(JsonObject schema, ValueMap properties) {
        JsonArray faqItems = new JsonArray();
        
        String faqData = properties.get("faqItems", String.class);
        JsonArray input = parseJsonArray(faqData);
        for (JsonElement element : input) {
            if (!element.isJsonObject()) {
                continue;
            }
            JsonObject obj = element.getAsJsonObject();
            String questionText = getString(obj, "question", "name");
            String answerText = getString(obj, "answer", "acceptedAnswer", "text");
            if (StringUtils.isBlank(questionText) || StringUtils.isBlank(answerText)) {
                continue;
            }
            JsonObject question = new JsonObject();
            question.addProperty("@type", "Question");
            question.addProperty("name", questionText);
            JsonObject answer = new JsonObject();
            answer.addProperty("@type", "Answer");
            answer.addProperty("text", answerText);
            question.add("acceptedAnswer", answer);
            faqItems.add(question);
        }
        
        if (faqItems.size() > 0) {
            schema.add("mainEntity", faqItems);
        }
    }

    private void addHowToFields(JsonObject schema, ValueMap properties) {
        String name = properties.get("jcr:title", String.class);
        if (StringUtils.isNotBlank(name)) {
            schema.addProperty("name", name);
        }

        String description = properties.get("jcr:description", String.class);
        if (StringUtils.isNotBlank(description)) {
            schema.addProperty("description", description);
        }

        JsonArray steps = new JsonArray();
        String stepsJson = properties.get("howToSteps", String.class);
        JsonArray input = parseJsonArray(stepsJson);
        for (JsonElement element : input) {
            if (!element.isJsonObject()) {
                continue;
            }
            JsonObject obj = element.getAsJsonObject();
            String stepText = getString(obj, "text", "description");
            if (StringUtils.isBlank(stepText)) {
                continue;
            }
            JsonObject step = new JsonObject();
            step.addProperty("@type", "HowToStep");
            String stepName = getString(obj, "name", "title");
            if (StringUtils.isNotBlank(stepName)) {
                step.addProperty("name", stepName);
            }
            step.addProperty("text", stepText);
            steps.add(step);
        }
        
        if (steps.size() > 0) {
            schema.add("step", steps);
        }
    }

    private void addProductFields(JsonObject schema, ValueMap properties) {
        String name = properties.get("productName", properties.get("jcr:title", String.class));
        if (StringUtils.isNotBlank(name)) {
            schema.addProperty("name", name);
        }

        String description = properties.get("jcr:description", String.class);
        if (StringUtils.isNotBlank(description)) {
            schema.addProperty("description", description);
        }

        JsonObject offers = buildOffers(properties);
        if (offers != null) {
            schema.add("offers", offers);
        }
    }

    private void addOrganizationFields(JsonObject schema, ValueMap properties) {
        String name = properties.get("orgName", String.class);
        if (StringUtils.isNotBlank(name)) {
            schema.addProperty("name", name);
        }

        String url = properties.get("orgUrl", String.class);
        if (StringUtils.isNotBlank(url)) {
            schema.addProperty("url", url);
        }

        String logo = properties.get("orgLogo", String.class);
        if (StringUtils.isNotBlank(logo)) {
            schema.addProperty("logo", logo);
        }
    }

    private void addBreadcrumbFields(JsonObject schema, ValueMap properties) {
        JsonArray items = new JsonArray();
        
        String breadcrumbJson = properties.get("breadcrumbItems", String.class);
        JsonArray input = parseJsonArray(breadcrumbJson);
        int position = 1;
        for (JsonElement element : input) {
            if (!element.isJsonObject()) {
                continue;
            }
            JsonObject obj = element.getAsJsonObject();
            String name = getString(obj, "name", "title");
            String itemUrl = getString(obj, "item", "url");
            if (StringUtils.isBlank(name) || StringUtils.isBlank(itemUrl)) {
                continue;
            }
            JsonObject item = new JsonObject();
            item.addProperty("@type", "ListItem");
            item.addProperty("position", position++);
            item.addProperty("name", name);
            item.addProperty("item", itemUrl);
            items.add(item);
        }
        
        if (items.size() > 0) {
            schema.add("itemListElement", items);
        }
    }

    private void addWebPageFields(JsonObject schema, ValueMap properties) {
        String name = properties.get("jcr:title", String.class);
        if (StringUtils.isNotBlank(name)) {
            schema.addProperty("name", name);
        }

        String description = properties.get("jcr:description", String.class);
        if (StringUtils.isNotBlank(description)) {
            schema.addProperty("description", description);
        }
    }

    private String formatDate(Object date) {
        if (date instanceof org.joda.time.DateTime) {
            return ((org.joda.time.DateTime) date).toString();
        }
        if (date instanceof java.util.Calendar) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
            return sdf.format(((java.util.Calendar) date).getTime());
        }
        return date != null ? date.toString() : "";
    }

    private boolean validateSchemaOutput(String schemaType, JsonObject schema) {
        if (StringUtils.isBlank(schemaType) || schema == null) {
            return false;
        }
        Set<String> required = REQUIRED_FIELDS.get(schemaType);
        if (required == null || required.isEmpty()) {
            return true;
        }
        for (String field : required) {
            if (!hasRequiredSchemaField(schemaType, field, schema)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasRequiredSchemaField(String schemaType, String field, JsonObject schema) {
        if (schema.has(field) && !schema.get(field).isJsonNull()) {
            if (schema.get(field).isJsonArray()) {
                return schema.getAsJsonArray(field).size() > 0;
            }
            return true;
        }
        if ("Product".equals(schemaType) && "name".equals(field)) {
            return schema.has("name");
        }
        if ("Product".equals(schemaType) && "offers".equals(field)) {
            return schema.has("offers");
        }
        return false;
    }

    @Override
    public boolean validateSchema(String schemaType, ValueMap properties) {
        if (StringUtils.isBlank(schemaType) || properties == null) {
            return false;
        }

        Set<String> required = REQUIRED_FIELDS.get(schemaType);
        if (required == null || required.isEmpty()) {
            return true;
        }

        for (String field : required) {
            if (!hasRequiredField(schemaType, field, properties)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Set<String> getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public boolean isSupported(String schemaType) {
        return StringUtils.isNotBlank(schemaType) && SUPPORTED_TYPES.contains(schemaType);
    }

    private boolean hasRequiredField(String schemaType, String field, ValueMap properties) {
        if (properties.containsKey(field) && properties.get(field) != null) {
            return true;
        }
        if ("Product".equals(schemaType) && "name".equals(field)) {
            return StringUtils.isNotBlank(properties.get("productName", String.class))
                || StringUtils.isNotBlank(properties.get("jcr:title", String.class));
        }
        return switch (schemaType) {
            case "FAQPage" -> StringUtils.isNotBlank(properties.get("faqItems", String.class));
            case "HowTo" -> StringUtils.isNotBlank(properties.get("howToSteps", String.class))
                && StringUtils.isNotBlank(properties.get("jcr:title", String.class));
            case "BreadcrumbList" -> StringUtils.isNotBlank(properties.get("breadcrumbItems", String.class));
            case "Product" -> StringUtils.isNotBlank(properties.get("offerPrice", String.class))
                || StringUtils.isNotBlank(properties.get("offers", String.class));
            default -> false;
        };
    }

    private JsonArray parseJsonArray(String json) {
        if (StringUtils.isBlank(json)) {
            return new JsonArray();
        }
        try {
            JsonElement parsed = JsonParser.parseString(json);
            if (parsed.isJsonArray()) {
                return parsed.getAsJsonArray();
            }
        } catch (Exception e) {
            log.debug("Invalid JSON array for schema data", e);
        }
        return new JsonArray();
    }

    private String getString(JsonObject obj, String... keys) {
        for (String key : keys) {
            if (obj.has(key) && obj.get(key).isJsonPrimitive()) {
                String value = obj.get(key).getAsString();
                if (StringUtils.isNotBlank(value)) {
                    return value;
                }
            }
        }
        return null;
    }

    private JsonObject buildOffers(ValueMap properties) {
        String offersJson = properties.get("offers", String.class);
        if (StringUtils.isNotBlank(offersJson)) {
            try {
                JsonElement parsed = JsonParser.parseString(offersJson);
                if (parsed.isJsonObject()) {
                    JsonObject offers = parsed.getAsJsonObject();
                    if (!offers.has("@type")) {
                        offers.addProperty("@type", "Offer");
                    }
                    return offers;
                }
            } catch (Exception e) {
                log.debug("Invalid offers JSON", e);
            }
        }

        String price = properties.get("offerPrice", String.class);
        String currency = properties.get("offerCurrency", String.class);
        if (StringUtils.isBlank(price) || StringUtils.isBlank(currency)) {
            return null;
        }

        JsonObject offers = new JsonObject();
        offers.addProperty("@type", "Offer");
        offers.addProperty("price", price);
        offers.addProperty("priceCurrency", currency);
        String availability = properties.get("offerAvailability", String.class);
        if (StringUtils.isNotBlank(availability)) {
            offers.addProperty("availability", availability);
        }
        String url = properties.get("offerUrl", String.class);
        if (StringUtils.isNotBlank(url)) {
            offers.addProperty("url", url);
        }
        return offers;
    }
}

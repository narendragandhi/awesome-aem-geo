package com.awesomeaem.geo.models;

import org.apache.sling.api.SlingHttpServletRequest;

import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

import com.awesomeaem.geo.services.ImageSeoService;

import lombok.Getter;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class})
public class ImageSeoModel {

    @Self
    private Resource resource;

    @OSGiService
    private ImageSeoService imageSeoService;

    @Getter
    private String imageUrl;

    @Getter
    private String altText;

    @Getter
    private String title;

    @Getter
    private String description;

    @Getter
    private String caption;

    @Getter
    private String width;

    @Getter
    private String height;

    @Getter
    private boolean hasValidAltText = false;

    @Getter
    private String imageSchema;

    @PostConstruct
    protected void init() {
        if (resource == null) {
            return;
        }

        ValueMap properties = resource.getValueMap();
        if (properties == null) {
            return;
        }

        this.imageUrl = properties.get("fileReference", String.class);
        this.altText = properties.get("alt", String.class);
        this.title = properties.get("jcr:title", properties.get("title", String.class));
        this.description = properties.get("jcr:description", properties.get("description", String.class));
        this.caption = properties.get("caption", String.class);

        Object widthObj = properties.get("width");
        if (widthObj != null) {
            this.width = widthObj.toString();
        }

        Object heightObj = properties.get("height");
        if (heightObj != null) {
            this.height = heightObj.toString();
        }

        this.hasValidAltText = altText != null && !altText.trim().isEmpty();

        if (imageSeoService != null) {
            this.imageSchema = imageSeoService.generateImageSchema(resource);
        }
    }

    public void setImageSeoService(ImageSeoService imageSeoService) {
        this.imageSeoService = imageSeoService;
    }
}

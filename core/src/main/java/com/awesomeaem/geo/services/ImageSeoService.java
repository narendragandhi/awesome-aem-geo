package com.awesomeaem.geo.services;

import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.Resource;

public interface ImageSeoService {

    String generateImageSchema(Resource imageResource);

    Map<String, Object> extractImageMetadata(Resource imageResource);

    boolean validateAltText(Resource imageResource);

    List<String> getImageSitemapEntries(String rootPath);
}

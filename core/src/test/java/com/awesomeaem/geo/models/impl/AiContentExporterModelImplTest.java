package com.awesomeaem.geo.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.awesomeaem.geo.models.AiContentExporterModel;

@DisplayName("AiContentExporterModelImpl - Unit Tests")
class AiContentExporterModelImplTest {

    @Test
    @DisplayName("Should return AuthorInfo when authorName is set")
    void shouldReturnAuthorInfo() throws Exception {
        AiContentExporterModelImpl model = new AiContentExporterModelImpl();

        Field nameField = AiContentExporterModelImpl.class.getDeclaredField("authorName");
        nameField.setAccessible(true);
        nameField.set(model, "Jane Author");

        Field urlField = AiContentExporterModelImpl.class.getDeclaredField("authorUrl");
        urlField.setAccessible(true);
        urlField.set(model, "https://example.com/jane");

        AiContentExporterModel.AuthorInfo author = model.getAuthor();
        assertNotNull(author);
        assertEquals("Jane Author", author.name());
        assertEquals("https://example.com/jane", author.url());
    }
}

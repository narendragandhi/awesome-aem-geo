package com.awesomeaem.geo.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@DisplayName("EEATSignalsModelImpl - Unit Tests")
class EEATSignalsModelImplTest {

    @Test
    @DisplayName("Should extract string values from child resources")
    void shouldExtractStringsFromResources() throws Exception {
        EEATSignalsModelImpl model = new EEATSignalsModelImpl();
        Resource resource = Mockito.mock(Resource.class);
        ValueMap vm = Mockito.mock(ValueMap.class);
        Mockito.when(resource.getValueMap()).thenReturn(vm);
        Mockito.when(vm.get("value", String.class)).thenReturn("PhD");

        Field field = EEATSignalsModelImpl.class.getDeclaredField("authorCredentials");
        field.setAccessible(true);
        field.set(model, List.of(resource));

        List<String> values = model.getAuthorCredentials();
        assertEquals(1, values.size());
        assertEquals("PhD", values.get(0));
    }

    @Test
    @DisplayName("Should convert Calendar to Instant for published date")
    void shouldConvertCalendarToInstant() throws Exception {
        EEATSignalsModelImpl model = new EEATSignalsModelImpl();
        Calendar cal = Calendar.getInstance();

        Field field = EEATSignalsModelImpl.class.getDeclaredField("publishedDate");
        field.setAccessible(true);
        field.set(model, cal);

        assertNotNull(model.getPublishedDate());
    }
}

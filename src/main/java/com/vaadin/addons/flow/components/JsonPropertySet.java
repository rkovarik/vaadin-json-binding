package com.vaadin.addons.flow.components;

import java.util.Optional;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.binder.PropertySet;

/**
 * Field holding {@link java.util.Collection} of values, which consist with multiple fields which can be added or deleted.
 *
 * @param <T> field value type
 */
public class JsonPropertySet implements PropertySet<JsonNode> {


    @Override
    public Stream<PropertyDefinition<JsonNode, ?>> getProperties() {
        return Stream.empty();
    }

    @Override
    public Optional<PropertyDefinition<JsonNode, ?>> getProperty(String name) {
        return Optional.empty();
    }
}

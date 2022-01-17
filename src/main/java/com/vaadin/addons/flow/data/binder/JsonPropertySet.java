package com.vaadin.addons.flow.data.binder;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.binder.PropertySet;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;

public class JsonPropertySet implements PropertySet<JsonNode> {

    @Override
    public Stream<PropertyDefinition<JsonNode, ?>> getProperties() {
        return Stream.empty();
    }

    @Override
    public Optional<PropertyDefinition<JsonNode, ?>> getProperty(String name) {
        return Optional.of(new PropertyDefinition<JsonNode, Object>() {
            @Override
            public ValueProvider<JsonNode, Object> getGetter() {
                return new ValueProvider<JsonNode, Object>() {
                    @Override
                    public Object apply(JsonNode jsonNode) {
                        final JsonNode node = jsonNode.path(name);
                        return convert(node);
                    }

                    private Object convert(JsonNode node) {
                        switch (node.getNodeType()) {
                        case STRING:
                            return node.asText();
                        case NUMBER:
                            if (node instanceof IntNode) {
                                return node.asInt();
                            } else if (node instanceof DoubleNode) {
                                return node.asDouble();
                            }
                            //TODO more number types
                        case BOOLEAN:
                            return node.asBoolean();
                        case MISSING: case NULL:
                            return null;
                        case ARRAY:
                            return StreamSupport.stream(node.spliterator(), false)
                                    .map(this::convert)
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toSet());
                        case OBJECT:
                            return node;
                        default:
                            throw new IllegalArgumentException(String.format("Getting value of %s is not implemented", node));
                        }
                    }
                };
            }

            @Override
            public Optional<Setter<JsonNode, Object>> getSetter() {
                return Optional.of((jsonNode, value) -> {
                    if (jsonNode instanceof ObjectNode) {
                        if (value == null) {
                            ((ObjectNode) jsonNode).remove(name);
                        } else if (value instanceof String) {
                            ((ObjectNode) jsonNode).put(name, (String) value);
                        } else if (value instanceof Integer) {
                            ((ObjectNode) jsonNode).put(name, (Integer) value);
                        } else if (value instanceof Double) {
                            ((ObjectNode) jsonNode).put(name, (Double) value);
                            //TODO more number types
                        } else if (value instanceof Boolean) {
                            ((ObjectNode) jsonNode).put(name, (Boolean) value);
                        } else if (value instanceof JsonNode) {
                            ((ObjectNode) jsonNode).set(name, (JsonNode) value);
                        } else if (value instanceof Set) {
                            final ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode(((Collection<?>) value).size());
                            ((Set<?>) value).stream()
                                    .filter(Objects::nonNull)
                                    .forEach(o -> arrayNode.add((Integer) o));
                            ((ObjectNode) jsonNode).set(name, arrayNode);
//                    } else if (value instanceof LocalDateTime) { TODO
//                        jsonNode.put(name, ((LocalDateTime) value).toString());
                        } else {
                            ((ObjectNode) jsonNode).put(name, String.valueOf(value));
                        }
                    } else {
                        throw new IllegalArgumentException("Only ObjectNode is supported");
                    }
                });
            }

            @Override
            public Class<Object> getType() {
                return Object.class;
            }

            @Override
            public Class<?> getPropertyHolderType() {
                return JsonNode.class;
            }

            @Override
            public String getName() {
                return name;
            }

            @Override
            public String getCaption() {
                return name;
            }

            @Override
            public PropertySet<JsonNode> getPropertySet() {
                return JsonPropertySet.this;
            }

            @Override
            public PropertyDefinition<JsonNode, ?> getParent() {
                return null;
            }
        });
    }
}

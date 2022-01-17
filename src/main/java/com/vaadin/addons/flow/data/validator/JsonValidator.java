package com.vaadin.addons.flow.data.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.ValidationMessage;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.AbstractValidator;

public class JsonValidator extends AbstractValidator<JsonNode> {

    private final JsonSchema jsonSchema;

    public JsonValidator(JsonSchema schema) {
        super("Schema validation error");
        jsonSchema = schema;
    }

    @Override
    public ValidationResult apply(JsonNode value, ValueContext context) {
        return jsonSchema.validate(value).stream()
                .map(ValidationMessage::getMessage)
                .findFirst()
                .map(ValidationResult::error)
                .orElseGet(ValidationResult::ok);
    }
}

package com.vaadin.addons.flow.data.binder;

import com.fasterxml.jackson.databind.JsonNode;
import com.vaadin.flow.data.binder.Binder;

public class JsonBinder extends Binder<JsonNode> {

    public JsonBinder() {
        super(new JsonPropertySet());
    }

}

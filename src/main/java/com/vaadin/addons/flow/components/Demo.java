package com.vaadin.addons.flow.components;

import java.util.Arrays;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.vaadin.addons.flow.data.binder.JsonBinder;
import com.vaadin.addons.flow.data.validator.JsonValidator;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Route("")
public class Demo extends HorizontalLayout {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Demo() throws JsonProcessingException {
        setSizeFull();

        final TextField text = new TextField("string");
        final NumberField number = new NumberField("number");
        Arrays.asList(text, number).forEach(hasValueChangeMode -> hasValueChangeMode.setValueChangeMode(ValueChangeMode.EAGER));
        final Checkbox checkbox = new Checkbox("boolean");
        final ComboBox<JsonNode> comboBox = new ComboBox<>("jsonSubNode", JsonNodeFactory.instance.arrayNode(), JsonNodeFactory.instance.objectNode(), JsonNodeFactory.instance.textNode("text"));
        final DateTimePicker date = new DateTimePicker("date");
        final CheckboxGroup<Integer> checkboxGroup = new CheckboxGroup<>();
        checkboxGroup.setLabel("array");
        checkboxGroup.setItems(1, 2);
        final Label statusLabel = new Label();

        final FormLayout formLayout = new FormLayout(text, number, checkbox, checkboxGroup, comboBox, statusLabel);
        formLayout.setWidth(25, Unit.PERCENTAGE);
        TextArea textArea = new TextArea();
        textArea.setValueChangeMode(ValueChangeMode.TIMEOUT);
        textArea.setWidth(100, Unit.PERCENTAGE);
        add(formLayout, textArea);

        final JsonBinder binder = new JsonBinder();
        binder.withValidator(new JsonValidator(
                JsonSchemaFactory.builder(JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7))
                        .build()
                        .getSchema("{\"required\": [\"number\"]}"))
        );
        binder.forField(text).bind(text.getLabel());
        binder.forField(number).bind(number.getLabel());
        binder.forField(checkbox).bind(checkbox.getLabel());
        binder.forField(comboBox).bind(comboBox.getLabel());
        binder.forField(checkboxGroup).bind(checkboxGroup.getLabel());
        binder.forField(date).bind(date.getLabel());
        binder.addStatusChangeListener(event -> textArea.setValue(binder.getBean().toPrettyString()));
        binder.setStatusLabel(statusLabel);
        statusLabel.setVisible(true);


        textArea.addValueChangeListener(event -> {
            try {
                binder.setBean(new ObjectMapper().readTree(event.getValue()));
            } catch (JsonProcessingException e) {
                Notification.show(e.getMessage(), 1000, Notification.Position.TOP_END);
            }
        });

        binder.setBean(objectMapper.readTree("{\n" +
                "  \"string\" : \"aString\",\n" +
                "  \"boolean\" : true,\n" +
                "  \"array\" : [1]\n" +
                "}"));
    }
}

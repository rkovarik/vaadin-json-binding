package com.vaadin.addons.flow.components;

import java.util.Arrays;
import java.util.Collection;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;

@Route("")
public class Demo extends FlexLayout { //TODO move to test

    public Demo() {
        setSizeFull();

        final EmailField emails = new EmailField();
        emails.setLabel("Emails2");
        final Label statusLabel = new Label("");

        final Binder<Person> binder = new BeanValidationBinder<>(Person.class);

        binder.forField(emails)
                .asRequired("Add at least one item")
                .withStatusLabel(statusLabel)
                .bind("emails");

        final Person person = new Person();
        person.setEmails(Arrays.asList("persona@company.com", null));
        person.setIcons(Arrays.asList(VaadinIcon.PLUS_SQUARE_O, null, VaadinIcon.ALT_A));
        binder.readBean(person);

        final FormLayout formLayout = new FormLayout(new VerticalLayout(emails, statusLabel));
        formLayout.setSizeFull();
        add(formLayout);
    }

    public static class Person {

        private Collection<String> emails;
        private Collection<VaadinIcon> icons;
        private Collection<Object> mixed;

        public Collection<String> getEmails() {
            return emails;
        }

        public void setEmails(Collection<String> emails) {
            this.emails = emails;
        }

        public Collection<VaadinIcon> getIcons() {
            return icons;
        }

        public void setIcons(Collection<VaadinIcon> icons) {
            this.icons = icons;
        }

        public Collection<Object> getMixed() {
            return mixed;
        }

        public void setMixed(Collection<Object> mixed) {
            this.mixed = mixed;
        }
    }
}

package com.vaadin.demo.ui;

import com.vaadin.data.Property;
import com.vaadin.demo.entities.Gender;
import com.vaadin.demo.entities.Patient;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;

/**
 * Created by mstahv
 */
@SpringComponent
@UIScope
public class PatientFormImpl extends AbstractForm<Patient> {

    private TextField firstName = new MTextField("First name");
    private TextField middleName = new MTextField("Middle name");
    private TextField lastName = new MTextField("Last name");
    private TypedSelect<String> greeting = new TypedSelect<>("Greeting");
    private TypedSelect<Gender> gender = new TypedSelect<>("Gender");
    private DateField birthDate = new DateField("Date of birth");
    private TextField ssn = new MTextField("SSN");
    private TextField medicalRecord = new MTextField("Patient ID");

    public PatientFormImpl() {

        gender.setOptions(Gender.values());
        
        medicalRecord.setReadOnly(true);

        Property.ValueChangeListener updateGreetings = e -> {
            greeting.setOptions(firstName.getValue(), middleName.getValue());
        };
        firstName.addValueChangeListener(updateGreetings);
        middleName.addValueChangeListener(updateGreetings);

    }

    @Override
    protected void adjustSaveButtonState() {
        getResetButton().setEnabled(true);
    }

    @Override
    protected Component createContent() {
        return new MFormLayout(firstName, middleName, lastName, greeting, gender, birthDate, ssn, medicalRecord, getToolbar());
    }


}

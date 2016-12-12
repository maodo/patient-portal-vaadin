package com.vaadin.demo.ui;

import com.vaadin.demo.entities.Doctor;
import com.vaadin.demo.entities.JournalEntry;
import com.vaadin.demo.repositories.DoctorRepository;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.fields.EnumSelect;
import org.vaadin.viritin.fields.MTextArea;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * Created by mstahv
 */
@SpringComponent
@UIScope
public class JournalEntryFormImpl extends AbstractForm<JournalEntry> {

    @Autowired
    DoctorRepository doctorRepository;

    private Label patient = new Label();
    private DateField date = new DateField("Date");
    private EnumSelect appointmentType = new EnumSelect<>().withCaption("Appointment");
    private TypedSelect<Doctor> doctor = new TypedSelect<>(Doctor.class).withCaption("Doctor");
    private TextArea entry = new MTextArea();

    @Override
    protected Component createContent() {
        return new MVerticalLayout(
                new MFormLayout(patient, date, appointmentType, doctor),
                new MVerticalLayout(new MLabel("NOTES").withWidthUndefined(), entry).alignAll(Alignment.TOP_CENTER).withMargin(false),
                getToolbar()
        ).alignAll(Alignment.TOP_CENTER);
    }

    @Override
    public MBeanFieldGroup<JournalEntry> setEntity(JournalEntry entity) {
        patient.setValue(entity.getPatient().toString());
        doctor.setOptions(doctorRepository.findAll());
        return super.setEntity(entity);
    }

    @Override
    protected void adjustResetButtonState() {
        getResetButton().setEnabled(true);
    }
}

package com.vaadin.demo.ui;

import com.vaadin.demo.entities.Patient;
import com.vaadin.demo.repositories.PatientRepository;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.button.DeleteButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;


/**
 * Created by mstahv
 */
@SpringComponent
@UIScope
public class PatientView extends MainView {

    public static final String ACTIONS_COLUMN_ID = "actions";
    private final PatientRepository repo;
    private final PatientDetails patientDetails;

    private MTable<Patient> patients;
    private Button newPatientBtn;


    public PatientView(PatientRepository repository, PatientDetails patientDetails) {
        this.repo = repository;
        this.patientDetails = patientDetails;
    }

    @PostConstruct
    void init() {
        patients = new MTable<>(Patient.class)
        .withProperties("name","id", "medicalRecord", "doctor", "lastVisit")
                .withGeneratedColumn("name", p->p.toString())
                .withGeneratedColumn("", p -> {
                    Button edit = new MButton(FontAwesome.PENCIL, e->focusPatient(p));
                    Button cancel = new DeleteButton(FontAwesome.TRASH, "", "Are you sure you want to delete the patient?", e->deletePatient(p));
                    return new MHorizontalLayout(edit, cancel);
                });

        patients.setSizeFull();

        newPatientBtn = new Button("Add patient");
        newPatientBtn.setIcon(FontAwesome.PLUS);
        newPatientBtn.addClickListener(e -> {
            Patient patient = new Patient();
            patientDetails.showPatient(patient);
            patientDetails.edit();
        });

        add(newPatientBtn).setComponentAlignment(newPatientBtn, Alignment.TOP_RIGHT);
        expand(patients);

        listPatients();
    }

    public void listPatients() {
        patients.setRows(repo.findAll());
    }

    private void focusPatient(Patient p) {
        patientDetails.showPatient(p);
    }

    private void deletePatient(Patient patient) {
        repo.delete(patient);
        listPatients();
    }

}

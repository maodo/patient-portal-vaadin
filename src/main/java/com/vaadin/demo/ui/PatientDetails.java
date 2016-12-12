package com.vaadin.demo.ui;

import com.vaadin.demo.entities.JournalEntry;
import com.vaadin.demo.entities.Patient;
import com.vaadin.demo.repositories.PatientRepository;
import com.vaadin.demo.service.PatientService;
import com.vaadin.event.SelectionEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.ui.renderers.TextRenderer;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.label.MLabel;

/**
 * Created by mstahv
 */
@SpringComponent
@UIScope
public class PatientDetails extends SubView {

    @Autowired
    PatientRepository repo;

    @Autowired
    PatientService patientService;

    VerticalLayout profile = new VerticalLayout();
    VerticalLayout journal = new VerticalLayout();

    @Autowired
    PatientForm form = new PatientForm();

    @Autowired
    JournalEntryForm journalEntryForm;

    Button editBtn = new Button("EDIT");
    Button addJournalBtn = new Button("ADD");
    Button back = new Button("ALL PATIENTS", FontAwesome.ARROW_LEFT);
    private Patient patient;

    public PatientDetails() {
        profile.setCaption("Profile");
        profile.setSpacing(true);
        profile.setMargin(true);
        addTab(profile);

        journal.setCaption("Journal");
        journal.setSpacing(true);
        journal.setMargin(true);
        addTab(journal);

        editBtn.addClickListener(e -> edit());
        addJournalBtn.addClickListener(e -> addJournal());
        setTopRightComponent(editBtn);
        getTabsheet().addSelectedTabChangeListener(e -> {
            if (e.getTabSheet().getSelectedTab() == journal) {
                setTopRightComponent(addJournalBtn);
            } else {
                setTopRightComponent(editBtn);
            }

        });

        back.addClickListener(e -> close());
        setTopLeftComponent(back);

    }

    void showPatient(Patient p) {
        if(p.isPersistent()) {
            p = patientService.findAttached(p); // fetch with joins to history etc
        }
        patient = p;

        addJournalBtn.setEnabled(p.isPersistent());

        profile.removeAllComponents();

        Label fn = new Label(p.getFirstName());
        fn.setCaption("FIRST NAME");
        fn.setStyleName(ValoTheme.LABEL_H3);
        Label mn = new Label(p.getMiddleName());
        mn.setCaption("MIDDLE NAME");
        mn.setStyleName(ValoTheme.LABEL_H3);
        Label ln = new Label(p.getLastName());
        ln.setCaption("LAST NAME");
        ln.setStyleName(ValoTheme.LABEL_H3);
        HorizontalLayout nameLayout = new HorizontalLayout(fn, mn, ln);
        nameLayout.setSpacing(true);
        profile.addComponent(nameLayout);

        // TODO BIND data, manually is the only option for read only?
        FormLayout fl = new FormLayout();
        fl.addComponent(label("Gender", p.getGender()));
        fl.addComponent(label("Date of birth", p.getBirthDate()));
        fl.addComponent(label("Snn", p.getSsn()));
        fl.addComponent(label("Patient ID", p.getId()));
        fl.addComponent(label("Doctor", p.getDoctor()));
        fl.addComponent(label("Medical record", p.getMedicalRecord()));
        fl.addComponent(label("Last visit", p.getLastVisit()));

        profile.addComponent(fl);


        journal.removeAllComponents();


        MGrid<JournalEntry> grid = new MGrid<>(JournalEntry.class)
                .withGeneratedColumn("date2", j -> SimpleDateFormat.getDateInstance().format(j.getDate()))
                .withProperties("date2", "appointmentType", "doctor", "entry")
                .setRows(patient.getJournalEntries())
                .withFullWidth();
        grid.getColumn("entry").setWidth(200);

        grid.setDetailsGenerator(new Grid.DetailsGenerator() {
            @Override
            public Component getDetails(Grid.RowReference rowReference) {
                JournalEntry je = (JournalEntry) rowReference.getItemId();
                return new MLabel(je.getEntry());
            }
        });

        grid.addSelectionListener(e -> {
            if(!e.getRemoved().isEmpty()) {
                grid.setDetailsVisible(e.getRemoved().iterator().next(), false);
            }
            if(!e.getAdded().isEmpty()) {
                grid.setDetailsVisible(grid.getSelectedRow(), true);
            }
        });

        journal.addComponent(grid);


        show();
    }

    private Label label(String caption, Object value) {
        return new MLabel(value == null ? "" : value.toString()).withCaption(caption);
    }

    private void addJournal() {
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setDate(new Date());
        journalEntry.setPatient(patient);
        patient.getJournalEntries().add(0, journalEntry);
        journalEntryForm.editJournal(journalEntry);
    }

    public void edit() {
        form.editPatient(patient);
    }
}

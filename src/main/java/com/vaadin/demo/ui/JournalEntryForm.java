package com.vaadin.demo.ui;

import com.vaadin.demo.entities.JournalEntry;
import com.vaadin.demo.repositories.PatientRepository;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.annotation.PostConstruct;

/**
 * Created by mstahv
 */
@SpringComponent
@UIScope
public class JournalEntryForm extends SubView {

    @Autowired
    PatientRepository repo;

    @Autowired
    @Lazy
    PatientDetails patientDetails;

    @Autowired
    @Lazy
    PatientView patientView;

    @Autowired
    JournalEntryFormImpl journalEntryFormImpl;

    private Button secondaryCancel = new Button(FontAwesome.TIMES);

    @PostConstruct
    public void init() {
        VerticalLayout layout = new MVerticalLayout(journalEntryFormImpl)
                .withFullHeight().withCaption("Edit Patient");

        journalEntryFormImpl.setSavedHandler(p -> {
            repo.save(p.getPatient());
            close();
            // For better separation of concern, one should consider using
            // events to communicate between ui components
            patientDetails.showPatient(p.getPatient());
            patientView.listPatients();

        });

        journalEntryFormImpl.setResetHandler(p -> close());
        secondaryCancel.addClickListener(p -> close());
        setTopRightComponent(secondaryCancel);

        addTab(layout);
    }

    void editJournal(JournalEntry j) {
        journalEntryFormImpl.setEntity(j);
        show();
    }


}

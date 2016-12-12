package com.vaadin.demo.ui;

import com.vaadin.demo.entities.Patient;
import com.vaadin.demo.repositories.PatientRepository;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;

/**
 * Created by mstahv
 */
@SpringComponent
@UIScope
public class PatientForm extends SubView {
    
    @Autowired
    PatientRepository repo;

    @Autowired
    @Lazy
    PatientDetails patientDetails;

    @Autowired
    @Lazy
    PatientView patientView;

    @Autowired
    PatientFormImpl patientFormImpl;

    private Button secondaryCancel = new Button(FontAwesome.TIMES);

    @PostConstruct
    public void init() {
        CssLayout layout = new CssLayout();
        layout.setCaption("Edit Patient");
        layout.setSizeFull();

        layout.addComponent(patientFormImpl);

        addTab(layout);

        patientFormImpl.setResetHandler(e->close());
        secondaryCancel.addClickListener(e-> close());
        setTopRightComponent(secondaryCancel);

        patientFormImpl.setSavedHandler(this::save);

    }

    private void save(Patient p) {
        repo.save(p);
        close();
        // For better separation of concern, one should consider using
        // events to communicate between ui components
        patientDetails.showPatient(p);
        patientView.listPatients();

    }

    
    void editPatient(Patient p) {
        if(p.isPersistent()) {
            p = repo.findOne(p.getId());
        }
        patientFormImpl.setEntity(p);
        show();
    }
    
}

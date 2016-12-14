package com.vaadin.demo.ui;

import com.vaadin.demo.entities.Patient;
import com.vaadin.demo.repositories.PatientRepository;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

/**
 * Created by mstahv
 */
@SpringComponent
@UIScope
public class MobilePatientListing extends MobileListing<Patient> {

    private static final int PAGE_LENGTH = 10;

    @Autowired
    PatientRepository repository;

    @Autowired
    @Lazy
    PatientView patientView;

    protected MobilePatientListing() {
        super(Patient.class);
        setSortProperties("lastName", "doctor.lastName");
    }

    @Override
    protected void loadMore() {
        listPage(count()/PAGE_LENGTH + 1);
    }

    @Override
    public void list() {
        clear();
        listPage(0);
    }

    private void listPage(int page) {
        Page<Patient> list = repository.findAll(
                new PageRequest(page, PAGE_LENGTH,
                        Sort.Direction.ASC, getSortProperty()
                )
        );
        for (Patient p : list.getContent()) {
            MobilePatientRow r = new MobilePatientRow(p, this);
            r.getSelectBtn().addClickListener(e->focusPatient(p));
            if(getSortProperty().startsWith("doctor")) {
                r.showDetail("Doctor", p.getDoctor().toString());
            }
            addRow(r);
        }
        setLoadMoreEnabled(list.hasNext());

    }

    void focusPatient(Patient p) {
        patientView.focusPatient(p);
    }

    @Override
    protected void onAdd() {

    }
}

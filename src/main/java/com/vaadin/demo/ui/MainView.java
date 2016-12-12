package com.vaadin.demo.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * Created by mstahv
 */
public abstract class MainView extends MVerticalLayout implements View {

    public MainView() {
        setCaption(getClass().getSimpleName().replaceAll("View",""));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        // NOP, not needed
    }

}

package com.vaadin.demo.ui;

import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by mstahv
 */
public abstract class SubView extends AbsoluteLayout {

    private final TabSheet tabSheet = new TabSheet();
    private Component topRightComponent;

    public SubView() {
        setSizeFull();
        tabSheet.setSizeFull();
        tabSheet.addStyleName(ValoTheme.TABSHEET_CENTERED_TABS);
        CssLayout cssLayout = new CssLayout(tabSheet);
        cssLayout.setStyleName(ValoTheme.LAYOUT_CARD);
        cssLayout.setSizeFull();
        addComponent(cssLayout);
    }

    public void addTab(Component c) {
        if (c.getCaption() == null) {
            c.setCaption(getClass().getSimpleName());
        }
        tabSheet.addComponent(c);
    }

    protected TabSheet getTabsheet() {
        return tabSheet;
    }

    public void setTopRightComponent(Component c) {
        configureButton(c);
        if(topRightComponent != null) {
            removeComponent(topRightComponent);
        }
        addComponent(c, "top:0;right:0");
        topRightComponent = c;
    }

    private void configureButton(Component c) {
        if (c instanceof Button) {
            Button button = (Button) c;
            button.setStyleName(ValoTheme.BUTTON_BORDERLESS);
        }
    }

    public void setTopLeftComponent(Component c) {
        configureButton(c);
        addComponent(c, "top:0;left:0");
    }

    public void setToolbar(Component c) {
        addComponent(c, "bottom:0;left:0right:0;");
        ComponentPosition componentPosition = new ComponentPosition();
        componentPosition.setCSSString("top:0;bottom:50px;right:0;left:0;");
        setPosition(tabSheet, componentPosition);
    }

    /**
     * Shows this sub view.
     */
    public void show() {
        ((VaadinUI) UI.getCurrent()).showSubView(this);
    }

    /**
     * Shows this sub view.
     */
    public void showAndCloseExisting() {
        ((VaadinUI) UI.getCurrent()).closeAllSubViews();
        ((VaadinUI) UI.getCurrent()).showSubView(this);
    }

    /**
     * Closes this subview.
     */
    public void close() {
        ((VaadinUI) getUI()).closeSubView(this);
    }

}

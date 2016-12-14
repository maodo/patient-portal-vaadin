package com.vaadin.demo.ui;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * Created by mstahv
 */
abstract class MobileRow extends MVerticalLayout {
    private final MHorizontalLayout header;
    private Button selectBtn = new MButton(FontAwesome.ARROW_RIGHT).withStyleName(ValoTheme.BUTTON_BORDERLESS, ValoTheme.BUTTON_ICON_ONLY);
    private Label title = new Label();
    private Label desc = new MLabel().withWidthUndefined();
    private FormLayout details = new FormLayout();
    private boolean detailsExpanded = false;

    public MobileRow(String titleStr, String descStr, String... props) {
        super();
        setMargin(false);
        header = new MHorizontalLayout(title,desc, selectBtn).alignAll(Alignment.MIDDLE_LEFT).expand(title);
        add(header);
        title.setContentMode(ContentMode.HTML);
        title.setValue(FontAwesome.CARET_RIGHT.getHtml() + " " + titleStr);
        desc.setValue(descStr);
        add(details);
        details.setMargin(false);

        header.addLayoutClickListener(e->{
            showDetails(detailsExpanded = !detailsExpanded);
            setStyleName("open", detailsExpanded);
            FontAwesome icon = detailsExpanded ? FontAwesome.CARET_DOWN : FontAwesome.CARET_RIGHT;
            title.setValue(icon.getHtml() + " " + titleStr);
        });
    }

    public Button getSelectBtn() {
        return selectBtn;
    }

    public MHorizontalLayout getHeader() {
        return header;
    }

    protected void showDetail(String name, Object value) {
        details.addComponent(new MLabel(value.toString()).withCaption(name.toUpperCase()));
    }

    protected abstract void showDetails(boolean expanded);

    protected void clear() {
        details.removeAllComponents();
    }

}
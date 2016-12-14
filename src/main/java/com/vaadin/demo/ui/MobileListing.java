package com.vaadin.demo.ui;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.WrapDynaBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * Created by mstahv
 */
public abstract class MobileListing<T> extends MVerticalLayout {
    private Label sortTitle = new MLabel("SORT BY:").withWidthUndefined();
    private TypedSelect<String> sortPropertySelect = new TypedSelect<>(String.class);
    private MButton addBtn = new MButton(FontAwesome.PLUS, e->onAdd()).withStyleName(ValoTheme.BUTTON_BORDERLESS);
    private Button loadMoreBtn = new MButton("Load more...", e->loadMore());

    protected abstract void loadMore();

    public abstract void list();

    protected abstract void onAdd();

    private CssLayout results = new CssLayout();

    protected MobileListing(Class<T> type) {
        sortPropertySelect.setNullSelectionAllowed(false);
        add(new MHorizontalLayout(sortTitle, sortPropertySelect, addBtn)
                .alignAll(Alignment.MIDDLE_LEFT)
                .expand(sortPropertySelect)
                .withStyleName("search-bar"),
                results,
                loadMoreBtn
        );
        loadMoreBtn.setEnabled(false);
    }

    protected int count() {
        return results.getComponentCount();
    }

    protected void clear() {
        results.removeAllComponents();
    }

    protected void addRow(MobileRow r) {
        results.addComponent(r);
    }

    protected void setSortProperties(String... props) {
        sortPropertySelect.setOptions(props);
        sortPropertySelect.setValue(props[0]);
        sortPropertySelect.setCaptionGenerator(s-> {
            String str = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(s), " ");
            if(str.contains(".")) {
                str = str.substring(0, str.indexOf("."));
            }
            return StringUtils.capitalize(str);
        });
        sortPropertySelect.addMValueChangeListener(e->list());
    }

    protected String getSortProperty() {
        return sortPropertySelect.getValue();
    }

    protected void setLoadMoreEnabled(boolean b) {
        loadMoreBtn.setEnabled(b);
    }


}

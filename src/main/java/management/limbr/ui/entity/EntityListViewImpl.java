/*
 * Copyright (c) 2016 Tyrel Haveman and contributors.
 *
 * This file is part of Limbr.
 *
 * Limbr is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Limbr is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Limbr.  If not, see <http://www.gnu.org/licenses/>.
 */

package management.limbr.ui.entity;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.GeneratedPropertyContainer;
import com.vaadin.data.util.PropertyValueGenerator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.ButtonRenderer;
import management.limbr.data.model.BaseEntity;
import management.limbr.data.model.ListColumn;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;

import javax.annotation.PostConstruct;
import java.util.Collection;

public abstract class EntityListViewImpl<T extends BaseEntity> extends VerticalLayout implements View, EntityListView {
    public static final String EDIT_PROPERTY_ID = "_edit_";

    private transient Collection<EntityListView.Listener<T>> listeners;
    private transient I18N messages;
    private Grid grid;
    private Class<T> entityClass;

    @Autowired
    public EntityListViewImpl(Class<T> entityClass, Collection<EntityListView.Listener<T>> listeners, I18N messages) {
        this.entityClass = entityClass;
        this.listeners = listeners;
        this.messages = messages;
    }

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void init() {
        grid = new Grid();
        grid.setEditorEnabled(true);
        grid.setSizeFull();
        TextField filter = new TextField();
        filter.setInputPrompt(messages.get("filterLabel"));
        filter.addTextChangeListener(event -> listEntities(event.getText()));

        Button addNewButton = new Button(messages.get("newButtonLabel"), FontAwesome.PLUS);
        HorizontalLayout actions = new HorizontalLayout(filter, addNewButton);
        actions.setSpacing(true);
        grid.removeAllColumns();

        java.lang.reflect.Field[] fields = entityClass.getDeclaredFields();
        for (java.lang.reflect.Field field : fields) {
            if (null != field.getAnnotation(ListColumn.class)) {
                grid.addColumn(field.getName());
            }
        }

        Grid.Column editColumn = grid.addColumn(EDIT_PROPERTY_ID);
        editColumn.setMaximumWidth(50.0);
        editColumn.setHeaderCaption("");
        editColumn.setRenderer(new ButtonRenderer(event ->
                listeners.forEach(listener ->
                        listener.editItemClicked((T)event.getItemId()))));

        addNewButton.addClickListener(event -> listeners.forEach(EntityListView.Listener::addNewClicked));

        addComponent(actions);
        addComponent(grid);

        listeners.forEach(listener -> listener.viewInitialized(this));

        refresh();
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // nothing needs to be done here (See init())
    }

    @Override
    public void refresh() {
        listEntities(null);
    }

    private void listEntities(String filter) {
        listeners.forEach(listener -> {
            BeanItemContainer<T> items = listener.listEntities(filter);
            GeneratedPropertyContainer gpc = new GeneratedPropertyContainer(items);
            gpc.addGeneratedProperty(EDIT_PROPERTY_ID, new PropertyValueGenerator<String>() {
                @Override
                public String getValue(Item item, Object itemId, Object propertyId) {
                    return messages.get("editButtonLabel");
                }

                @Override
                public Class<String> getType() {
                    return String.class;
                }
            });

            grid.setContainerDataSource(gpc);
        });
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EntityListViewImpl) {
            EntityListViewImpl view = (EntityListViewImpl)obj;
            return (grid == view.grid) && super.equals(obj);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 47).append(grid).appendSuper(super.hashCode()).toHashCode();
    }
}

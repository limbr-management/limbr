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

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import management.limbr.data.model.BaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class EntityEditorViewImpl<T extends BaseEntity> extends Window implements EntityEditorView {
    private transient Collection<EntityEditorView.Listener> listeners;
    private transient I18N messages;

    private Class<T> entityClass;

    private Map<String, Field> fieldMap = new HashMap<>();

    private Button save;
    private Button cancel;

    @Autowired
    public EntityEditorViewImpl(Class<T> entityClass, Collection<EntityEditorView.Listener> listeners, I18N messages) {
        this.entityClass = entityClass;
        this.listeners = listeners;
        this.messages = messages;
    }

    @PostConstruct
    public void init() {
        VerticalLayout content = new VerticalLayout();

        for (java.lang.reflect.Field field : entityClass.getDeclaredFields()) {
            Field uiField = new TextField(messages.get(field.getName() + "FieldLabel"));
            fieldMap.put(field.getName(), uiField);
            content.addComponent(uiField);
        }

        save = new Button(messages.get("saveButtonLabel"), FontAwesome.SAVE);
        cancel = new Button(messages.get("cancelButtonLabel"));
        Button delete = new Button(messages.get("deleteButtonLabel"), FontAwesome.TRASH_O);

        CssLayout actions = new CssLayout(save, cancel, delete);
        content.addComponent(actions);

        content.setMargin(true);
        setContent(content);
        setClosable(true);
        setResizable(false);
        setModal(true);

        listeners.forEach(listener -> listener.viewInitialized(this));

        save.addClickListener(event -> listeners.forEach(EntityEditorView.Listener::save));
        delete.addClickListener(event -> listeners.forEach(EntityEditorView.Listener::delete));
        cancel.addClickListener(event -> listeners.forEach(EntityEditorView.Listener::cancel));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> void setFieldValue(String name, U value) {
        Field<U> field = fieldMap.get(name);
        if (field != null) {
            field.setValue(value);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> U getFieldValue(Class<U> valueClass, String name) {
        Field<U> field = fieldMap.get(name);
        if (field != null) {
            return field.getValue();
        }
        return null;
    }

    @Override
    public void setCancelVisible(boolean visible) {
        cancel.setVisible(visible);
    }

    @Override
    public void show() {
        center();
        UI.getCurrent().addWindow(this);
        save.focus();
    }

    @Override
    public void hide() {
        close();
    }
}

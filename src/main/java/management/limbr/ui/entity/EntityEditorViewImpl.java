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
import management.limbr.data.model.Password;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class EntityEditorViewImpl<T extends BaseEntity> extends Window implements EntityEditorView<T> {
    private transient Collection<EntityEditorView.Listener<T>> listeners;
    private transient I18N messages;

    private Class<T> entityClass;

    private Map<String, Field> fieldMap = new HashMap<>();

    private Button save;
    private Button delete;

    @Autowired
    public EntityEditorViewImpl(Class<T> entityClass, Collection<EntityEditorView.Listener<T>> listeners, I18N messages) {
        this.entityClass = entityClass;
        this.listeners = listeners;
        this.messages = messages;
    }

    @PostConstruct
    public void init() {
        VerticalLayout content = new VerticalLayout();

        for (java.lang.reflect.Field field : entityClass.getDeclaredFields()) {
            if ("id".equals(field.getName())) {
                continue;
            }
            Field uiField = getUIField(field);
            fieldMap.put(field.getName(), uiField);
            content.addComponent(uiField);
        }

        save = new Button(messages.get("saveButtonLabel"), FontAwesome.SAVE);
        Button cancel = new Button(messages.get("cancelButtonLabel"));
        delete = new Button(messages.get("deleteButtonLabel"), FontAwesome.TRASH_O);

        CssLayout actions = new CssLayout(save, cancel, delete);
        content.addComponent(actions);

        content.setMargin(true);
        setContent(content);
        setClosable(true);
        setResizable(false);
        setModal(true);

        save.addClickListener(event -> listeners.forEach(EntityEditorView.Listener::save));
        delete.addClickListener(event -> listeners.forEach(EntityEditorView.Listener::delete));
        cancel.addClickListener(event -> listeners.forEach(EntityEditorView.Listener::cancel));
    }

    private Field<String> getUIField(java.lang.reflect.Field field) {
        String label = messages.get(field.getName() + "FieldLabel");
        Password passwordAnnotation = field.getAnnotation(Password.class);
        if (passwordAnnotation != null) {
            return new PasswordField(label);
        }
        return new TextField(label);
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
    public void setDeleteVisible(boolean visible) {
        delete.setVisible(visible);
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EntityEditorViewImpl) {
            EntityEditorViewImpl view = (EntityEditorViewImpl)obj;
            return view.entityClass.equals(entityClass) && view.fieldMap.equals(fieldMap) &&
                view.save.equals(save) && view.delete.equals(delete) && super.equals(obj);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(3, 37)
                .append(entityClass)
                .append(fieldMap)
                .append(save)
                .append(delete)
                .appendSuper(super.hashCode())
                .toHashCode();
    }
}

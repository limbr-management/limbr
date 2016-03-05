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

import management.limbr.data.model.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class EntityEditorPresenter<T extends BaseEntity> implements EntityEditorView.Listener, Serializable, ApplicationContextAware {
    private transient JpaRepository<T, Long> repository;
    private T entity;
    private transient EntityEditorView<T> view;
    private EntityChangeHandler entityChangeHandler;
    private ApplicationContext applicationContext;

    private static final Logger LOG = LoggerFactory.getLogger(EntityEditorPresenter.class);

    @Autowired
    public EntityEditorPresenter(JpaRepository<T, Long> repository) {
        this.repository = repository;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected abstract Class<? extends EntityEditorView<T>> getViewBeanClass();

    public void hide() {
        getView().hide();
    }

    public void edit(T entity) {
        final boolean persisted = entity.getId() != null;

        if (persisted) {
            this.entity = repository.findOne(entity.getId());
        } else {
            this.entity = entity;
        }

        getView().setDeleteVisible(persisted);

        for (java.lang.reflect.Field field : entity.getClass().getDeclaredFields()) {
            if ("id".equals(field.getName())) {
                continue;
            }
            Object value = callGetter(entity, field.getName());
            if (value == null && field.getType().equals(String.class)) {
                value = "";
            }
            getView().setFieldValue(field.getName(), value);
        }

        getView().show();
    }

    @Override
    public void save() {
        for (java.lang.reflect.Field field : entity.getClass().getDeclaredFields()) {
            if ("id".equals(field.getName()) || "this$0".equals(field.getName())) {
                continue;
            }
            callSetter(entity, field.getName(), getView().getFieldValue(field.getType(), field.getName()));
        }

        repository.save(entity);

        if (entityChangeHandler != null) {
            entityChangeHandler.onEntityChanged();
        }

        getView().hide();
    }

    @Override
    public void delete() {
        // TODO: ask if they're sure

        repository.delete(entity);

        if (entityChangeHandler != null) {
            entityChangeHandler.onEntityChanged();
        }

        getView().hide();
    }

    @Override
    public void cancel() {
        getView().hide();
    }

    @FunctionalInterface
    public interface EntityChangeHandler {
        void onEntityChanged();
    }

    public void setEntityChangeHandler(EntityChangeHandler entityChangeHandler) {
        this.entityChangeHandler = entityChangeHandler;
    }

    private EntityEditorView<T> getView() {
        if (view == null) {
            view = applicationContext.getBean(getViewBeanClass());
        }

        return view;
    }

    private Object callGetter(T entity, String name) {
        Method getter;
        try {
            getter = entity.getClass().getDeclaredMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
        } catch (NoSuchMethodException ex1) {
            try {
                getter = entity.getClass().getDeclaredMethod("is" + name.substring(0, 1).toUpperCase() + name.substring(1));
            } catch (NoSuchMethodException ex2) {
                return null;
            }
        }

        try {
            return getter.invoke(entity);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            return entity;
        }
    }

    private <U> void callSetter(T entity, String name, U value) {
        Method setter;
        try {
            LOG.debug("Looking for setter for " + name + " in entity " + entity + " with value " + value);
            setter = entity.getClass().getDeclaredMethod("set" + name.substring(0, 1).toUpperCase() + name.substring(1), value.getClass());
        } catch (NoSuchMethodException ex) {
            return;
        }

        try {
            setter.invoke(entity, value);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            // oops
        }
    }
}

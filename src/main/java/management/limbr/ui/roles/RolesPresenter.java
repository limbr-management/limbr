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

package management.limbr.ui.roles;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import management.limbr.data.RoleRepository;
import management.limbr.data.model.Role;
import management.limbr.ui.Presenter;
import management.limbr.ui.entity.EntityEditorPresenter;
import management.limbr.ui.entity.EntityListView;
import management.limbr.ui.projects.ProjectsView;
import management.limbr.ui.roleeditor.RoleEditorPresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.Serializable;

@Presenter
public class RolesPresenter implements ProjectsView.Listener<Role>, Serializable, EntityEditorPresenter.EntityChangeHandler {
    private transient RoleRepository repository;
    private transient EntityListView view;
    private transient RoleEditorPresenter editor;

    @Autowired
    public RolesPresenter(RoleRepository repository, RoleEditorPresenter editor) {
        this.repository = repository;
        this.editor = editor;
    }

    @PostConstruct
    public void init() {
        editor.setEntityChangeHandler(this);
    }

    @Override
    public BeanItemContainer<Role> listEntities(String filter) {
        if (StringUtils.isEmpty(filter)) {
            return new BeanItemContainer<>(Role.class, repository.findAll());
        } else {
            return new BeanItemContainer<>(Role.class, repository.findByNameStartsWithIgnoreCase(filter));
        }
    }

    @Override
    public void viewInitialized(EntityListView view) {
        this.view = view;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void itemDoubleClicked(Item project) {
        if (project == null || !(project instanceof BeanItem)) {
            editor.hide();
        } else {
            editor.edit(((BeanItem<Role>)project).getBean());
        }
    }

    @Override
    public void addNewClicked() {
        editor.edit(new Role());
    }

    @Override
    public void onEntityChanged() {
        view.refresh();
    }
}

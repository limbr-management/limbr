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

package management.limbr.ui.projects;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import management.limbr.data.ProjectRepository;
import management.limbr.data.model.Project;
import management.limbr.ui.Presenter;
import management.limbr.ui.entity.EntityEditorPresenter;
import management.limbr.ui.entity.EntityListView;
import management.limbr.ui.projecteditor.ProjectEditorPresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.Serializable;

@Presenter
public class ProjectsPresenter implements ProjectsView.Listener<Project>, Serializable, EntityEditorPresenter.EntityChangeHandler {
    private transient ProjectRepository repository;
    private transient EntityListView view;
    private transient ProjectEditorPresenter editor;

    @Autowired
    public ProjectsPresenter(ProjectRepository repository, ProjectEditorPresenter editor) {
        this.repository = repository;
        this.editor = editor;
    }

    @PostConstruct
    public void init() {
        editor.setEntityChangeHandler(this);
    }

    @Override
    public BeanItemContainer<Project> listEntities(String filter) {
        if (StringUtils.isEmpty(filter)) {
            return new BeanItemContainer<>(Project.class, repository.findAll());
        } else {
            return new BeanItemContainer<>(Project.class, repository.findByNameStartsWithIgnoreCase(filter));
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
            editor.edit(((BeanItem<Project>)project).getBean());
        }
    }

    @Override
    public void addNewClicked() {
        editor.edit(new Project());
    }

    @Override
    public void onEntityChanged() {
        view.refresh();
    }
}

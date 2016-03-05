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

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import management.limbr.ui.PrivilegeLevels;
import management.limbr.ui.RequiresPrivilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;

import javax.annotation.PostConstruct;
import java.util.Collection;

@RequiresPrivilege(level = PrivilegeLevels.ADMIN)
@UIScope
@SpringView(name = ProjectsViewImpl.VIEW_NAME)
public class ProjectsViewImpl extends VerticalLayout implements View, ProjectsView {
    public static final String VIEW_NAME = "projects";

    private transient Collection<ProjectsViewListener> listeners;

    private transient I18N messages;

    private Grid grid;

    @Autowired
    public ProjectsViewImpl(Collection<ProjectsViewListener> listeners, I18N messages) {
        this.listeners = listeners;
        this.messages = messages;
    }

    @PostConstruct
    public void init() {
        grid = new Grid();
        TextField filter = new TextField();
        filter.setInputPrompt(messages.get("filterByProjectNameLabel"));
        filter.addTextChangeListener(event -> listProjects(event.getText()));

        Button addNewButton = new Button(messages.get("newProjectButtonLabel"), FontAwesome.PLUS);
        HorizontalLayout actions = new HorizontalLayout(filter, addNewButton);
        actions.setSpacing(true);
        grid.removeAllColumns();
        grid.addColumn("name");

        grid.addItemClickListener(event -> {
            if (event.isDoubleClick()) {
                listeners.forEach(listener -> listener.itemDoubleClicked(event.getItem()));
            }
        });

        addNewButton.addClickListener(event -> listeners.forEach(ProjectsViewListener::addNewClicked));

        addComponent(actions);
        addComponent(grid);
    }

    @Override
    public void refresh() {
        listProjects(null);
    }

    private void listProjects(String filter) {
        listeners.forEach(listener -> grid.setContainerDataSource(listener.listProjects(filter)));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // nothing needed here
    }
}

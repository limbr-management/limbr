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
import com.vaadin.data.util.BeanItemContainer;
import management.limbr.data.model.Project;

public interface ProjectsView {
    void refresh();

    interface ProjectsViewListener {
        BeanItemContainer<Project> listProjects(String filter);
        void viewInitialized(ProjectsView view);
        void itemDoubleClicked(Item project);
        void addNewClicked();
    }
}

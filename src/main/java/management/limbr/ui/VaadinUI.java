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

package management.limbr.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import management.limbr.LimbrApplication;
import management.limbr.UserEditor;
import management.limbr.data.UserRepository;
import management.limbr.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

@Theme("valo")
@SpringUI(path = "")
public class VaadinUI extends UI {
    @Autowired
    LimbrApplication.TestServiceOne service;

    private final UserEditor editor;
    private final UserRepository repository;

    @Autowired
    public VaadinUI(UserRepository repository, UserEditor editor) {
        this.repository = repository;
        this.editor = editor;
    }

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.addComponent(new Label(service.sayHi()));

        Grid grid = new Grid();
        TextField filter = new TextField();
        filter.setInputPrompt("Filter by username");
        filter.addTextChangeListener(e -> listUsers(grid, e.getText()));
        Button addNewButton = new Button("New user", FontAwesome.PLUS);
        HorizontalLayout actions = new HorizontalLayout(filter, addNewButton);
        actions.setSpacing(true);
        grid.removeAllColumns();
        grid.addColumn("username");
        grid.addColumn("displayName");
        grid.addColumn("emailAddress");

        grid.addSelectionListener(e -> {
            if (e.getSelected().isEmpty()) {
                editor.setVisible(false);
            } else {
                editor.editUser((User)e.getSelected().iterator().next());
            }
        });

        addNewButton.addClickListener(e -> editor.editUser(new User("", "", "", "")));

        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listUsers(grid, filter.getValue());
        });

        mainLayout.addComponent(actions);
        mainLayout.addComponent(grid);
        mainLayout.addComponent(editor);
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);

        setContent(mainLayout);

        listUsers(grid, null);
    }

    private void listUsers(Grid grid, String filter) {
        if (StringUtils.isEmpty(filter)) {
            grid.setContainerDataSource(new BeanItemContainer<>(User.class, repository.findAll()));
        } else {
            grid.setContainerDataSource(new BeanItemContainer<>(User.class,
                    repository.findByUsernameStartsWithIgnoreCase(filter)));
        }
    }
}

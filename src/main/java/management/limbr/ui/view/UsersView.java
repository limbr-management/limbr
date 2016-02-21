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

package management.limbr.ui.view;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import management.limbr.UserEditor;
import management.limbr.data.UserRepository;
import management.limbr.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

@SpringView(name = UsersView.VIEW_NAME)
public class UsersView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "users";

    private final UserEditor editor;
    private final UserRepository repository;

    @Autowired
    public UsersView(UserEditor editor, UserRepository repository) {
        this.editor = editor;
        this.repository = repository;
    }

    @PostConstruct
    void init() {
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

        addComponent(actions);
        addComponent(grid);
        addComponent(editor);

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

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }
}

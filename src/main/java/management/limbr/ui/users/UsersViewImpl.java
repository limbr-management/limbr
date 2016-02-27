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

package management.limbr.ui.users;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import management.limbr.ui.PrivilegeLevels;
import management.limbr.ui.RequiresPrivilege;
import management.limbr.ui.usereditor.UserEditorViewImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;

import javax.annotation.PostConstruct;
import java.util.Collection;

@RequiresPrivilege(level = PrivilegeLevels.ADMIN)
@UIScope
@SpringView(name = UsersViewImpl.VIEW_NAME)
@SuppressWarnings({"squid:S2160"}) // don't need to override equals here
public class UsersViewImpl extends VerticalLayout implements View, UsersView {
    public static final String VIEW_NAME = "users";

    @Autowired
    private UserEditorViewImpl editorView;

    @Autowired
    private transient Collection<UsersViewListener> listeners;

    @Autowired
    private transient I18N messages;

    private Grid grid;

    @PostConstruct
    public void init() {
        grid = new Grid();
        TextField filter = new TextField();
        filter.setInputPrompt(messages.get("filterByUsernameLabel"));
        filter.addTextChangeListener(e -> listUsers(e.getText()));
        Button addNewButton = new Button(messages.get("newUserButtonLabel"), FontAwesome.PLUS);
        HorizontalLayout actions = new HorizontalLayout(filter, addNewButton);
        actions.setSpacing(true);
        grid.removeAllColumns();
        grid.addColumn("username");
        grid.addColumn("displayName");
        grid.addColumn("emailAddress");

        grid.addItemClickListener(e -> {
            if (e.isDoubleClick()) {
                listeners.forEach(listener -> listener.itemDoubleClicked(e.getItem()));
            }
        });

        addNewButton.addClickListener(e -> listeners.forEach(UsersViewListener::addNewClicked));

        addComponent(actions);
        addComponent(grid);

        listeners.forEach(listener -> listener.viewInitialized(this));

        refresh();
    }

    @Override
    public void refresh() {
        listUsers(null);
    }

    private void listUsers(String filter) {
        listeners.forEach(listener -> grid.setContainerDataSource(listener.listUsers(filter)));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // don't need this, everything happens in init
    }
}

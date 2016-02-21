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

package management.limbr;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import management.limbr.data.UserRepository;
import management.limbr.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * A view used for editing a User object in the UI.
 */
@SpringComponent
@UIScope
public class UserEditor extends VerticalLayout {

    private final UserRepository repository;

    private User user;

    TextField userName = new TextField("Username");
    TextField displayName = new TextField("Real name");
    PasswordField password = new PasswordField("Password");
    TextField emailAddress = new TextField("E-mail address");

    private Button save = new Button("Save", FontAwesome.SAVE);
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", FontAwesome.TRASH_O);

    @Autowired
    public UserEditor(UserRepository repository) {
        this.repository = repository;

        CssLayout actions = new CssLayout(save, cancel, delete);
        addComponents(userName, displayName, password, emailAddress, actions);

        save.addClickListener(e -> repository.save(user));
        delete.addClickListener(e -> repository.delete(user));
        cancel.addClickListener(e -> editUser(user));
        setVisible(false);
    }

    public interface ChangeHandler {
        void onChange();
    }

    public void editUser(User u) {
        final boolean persisted = u.getId() != null;

        if (persisted) {
            user = repository.findOne(u.getId());
        } else {
            user = u;
        }

        cancel.setVisible(persisted);

        BeanFieldGroup.bindFieldsUnbuffered(user, this);

        setVisible(true);

        save.focus();
        userName.selectAll();
    }

    public void setChangeHandler(ChangeHandler h) {
        save.addClickListener(e -> h.onChange());
        delete.addClickListener(e -> h.onChange());
    }
}

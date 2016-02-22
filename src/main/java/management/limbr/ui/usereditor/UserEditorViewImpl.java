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

package management.limbr.ui.usereditor;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Collection;


/**
 * A view used for editing a User object in the UI.
 */
@SpringComponent
@UIScope
@SuppressWarnings({"squid:S2160"}) // don't need to override equals here
public class UserEditorViewImpl extends VerticalLayout implements UserEditorView {

    @Autowired
    private transient Collection<UserEditorViewListener> listeners;

    TextField userName = new TextField("Username");
    TextField displayName = new TextField("Real name");
    PasswordField password = new PasswordField("Password");
    TextField emailAddress = new TextField("E-mail address");

    private Button save = new Button("Save", FontAwesome.SAVE);
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete", FontAwesome.TRASH_O);

    @PostConstruct
    public void init() {
        CssLayout actions = new CssLayout(save, cancel, delete);
        addComponents(userName, displayName, password, emailAddress, actions);

        setVisible(false);

        listeners.forEach(listener -> listener.viewInitialized(this));

        save.addClickListener(e -> listeners.forEach(UserEditorViewListener::save));
        delete.addClickListener(e -> listeners.forEach(UserEditorViewListener::delete));
        cancel.addClickListener(e -> listeners.forEach(UserEditorViewListener::cancel));
    }

    @Override
    public void setUsername(String username) {
        this.userName.setValue(username);
    }

    @Override
    public String getUsername() {
        return userName.getValue();
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName.setValue(displayName);
    }

    @Override
    public String getDisplayName() {
        return displayName.getValue();
    }

    @Override
    public void setPassword(String password) {
        this.password.setValue(password);
    }

    @Override
    public String getPassword() {
        return password.getValue();
    }

    @Override
    public void setEmailAddress(String emailAddress) {
        this.emailAddress.setValue(emailAddress);
    }

    @Override
    public String getEmailAddress() {
        return this.emailAddress.getValue();
    }

    @Override
    public void setCancelVisible(boolean visible) {
        cancel.setVisible(visible);
    }

    @Override
    public void show() {
        setVisible(true);
        save.focus();
        userName.selectAll();
    }

    @Override
    public void hide() {
        setVisible(false);
    }

}

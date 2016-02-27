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
import management.limbr.ui.PrivilegeLevels;
import management.limbr.ui.RequiresPrivilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;

import javax.annotation.PostConstruct;
import java.util.Collection;


/**
 * A view used for editing a User object in the UI.
 */
@RequiresPrivilege(level = PrivilegeLevels.Admin)
@SpringComponent
@UIScope
@SuppressWarnings({"squid:S2160"}) // don't need to override equals here
public class UserEditorViewImpl extends Window implements UserEditorView {

    @Autowired
    private transient Collection<UserEditorViewListener> listeners;

    @Autowired
    private I18N messages;

    private TextField userName;
    private TextField displayName;
    private PasswordField password;
    private TextField emailAddress;

    private Button save;
    private Button cancel;

    @PostConstruct
    public void init() {
        VerticalLayout content = new VerticalLayout();

        userName = new TextField(messages.get("usernameFieldLabel"));
        displayName = new TextField(messages.get("realNameFieldLabel"));
        password = new PasswordField(messages.get("passwordFieldLabel"));
        emailAddress = new TextField(messages.get("emailAddressFieldLabel"));

        save = new Button(messages.get("saveButtonLabel"), FontAwesome.SAVE);
        cancel = new Button(messages.get("cancelButtonLabel"));
        Button delete = new Button(messages.get("deleteButtonLabel"), FontAwesome.TRASH_O);

        CssLayout actions = new CssLayout(save, cancel, delete);
        content.addComponents(userName, displayName, password, emailAddress, actions);
        content.setMargin(true);
        setContent(content);
        setClosable(true);
        setResizable(false);
        setModal(true);

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
        center();
        UI.getCurrent().addWindow(this);
        save.focus();
        userName.selectAll();
    }

    @Override
    public void hide() {
        close();
    }

}

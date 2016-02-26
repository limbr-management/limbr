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

package management.limbr.ui.login;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import management.limbr.LimbrApplication;
import management.limbr.ui.PrivilegeLevels;
import management.limbr.ui.RequiresPrivilege;
import management.limbr.ui.view.DefaultView;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;

import javax.annotation.PostConstruct;
import java.util.Collection;

@RequiresPrivilege(level = PrivilegeLevels.None)
@UIScope
@SpringView(name = LogInViewImpl.VIEW_NAME)
public class LogInViewImpl extends CustomComponent implements View, LogInView {
    public static final String VIEW_NAME = "logIn";

    @Autowired
    private transient Collection<LogInViewListener> listeners;

    @Autowired
    private I18N messages;

    private TextField usernameField;
    private PasswordField passwordField;

    @PostConstruct
    public void init() {
        setSizeFull();

        usernameField = new TextField(messages.get("usernameFieldLabel"));
        usernameField.setWidth(20.0f, Unit.EM);
        usernameField.setRequired(true);
        usernameField.setInputPrompt(messages.get("usernameFieldPrompt"));
        usernameField.addValidator(new StringLengthValidator(messages.get("usernameFieldValidation"), 3, 256, false));
        usernameField.setImmediate(true);
        usernameField.setInvalidAllowed(false);

        passwordField = new PasswordField(messages.get("passwordFieldLabel"));
        passwordField.setWidth(20.0f, Unit.EM);
        passwordField.setInputPrompt(messages.get("passwordFieldPrompt"));
        passwordField.addValidator(new StringLengthValidator(messages.get("passwordFieldValidation"), 8, 256, false));
        passwordField.setImmediate(true);
        passwordField.setRequired(true);
        passwordField.setNullRepresentation("");

        Button logInButton = new Button(messages.get("logInButtonLabel"));
        logInButton.addClickListener(event -> {
            usernameField.setValidationVisible(false);
            passwordField.setValidationVisible(false);
            try {
                usernameField.commit();
                passwordField.commit();
            } catch (Validator.InvalidValueException e) {
                Notification.show(e.getMessage());
                usernameField.setValidationVisible(true);
                passwordField.setValidationVisible(true);
            }
            listeners.forEach(LogInViewListener::logInClicked);
        });

        VerticalLayout fields = new VerticalLayout(usernameField, passwordField, logInButton);
        fields.setCaption(messages.get("logInCaption", LimbrApplication.getApplicationName()));
        fields.setSpacing(true);
        fields.setMargin(new MarginInfo(true, true, true, false));
        fields.setSizeUndefined();

        VerticalLayout mainLayout = new VerticalLayout(fields);
        mainLayout.setSizeFull();
        mainLayout.setComponentAlignment(fields, Alignment.MIDDLE_CENTER);
        mainLayout.setStyleName(ValoTheme.FORMLAYOUT_LIGHT);

        setCompositionRoot(mainLayout);

        listeners.forEach(listener -> listener.viewInitialized(this));
    }

    @Override
    public void setUsername(String username) {
        usernameField.setValue(username);
    }

    @Override
    public String getUsername() {
        return usernameField.getValue();
    }

    @Override
    public String getPassword() {
        return passwordField.getValue();
    }

    @Override
    public void reportError(String message) {
        Notification.show(message);
    }

    @Override
    public void loggedIn() {
        getUI().getNavigator().navigateTo(DefaultView.VIEW_NAME);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // nothing here, it's in init()
    }
}

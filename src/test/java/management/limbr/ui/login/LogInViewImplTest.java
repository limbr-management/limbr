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

import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import management.limbr.test.util.ComponentFinder;
import management.limbr.test.util.FakeI18N;
import management.limbr.ui.view.DefaultView;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.Test;
import org.vaadin.spring.i18n.I18N;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Test
public class LogInViewImplTest {
    public void storesUsername() {
        LogInViewImpl view = new LogInViewImpl();
        LogInView.LogInViewListener listener = mock(LogInView.LogInViewListener.class);
        ReflectionTestUtils.setField(view, "listeners", Collections.singleton(listener));

        I18N messages = new FakeI18N().add("usernameFieldLabel").add("usernameFieldPrompt")
                .add("usernameFieldValidation").add("passwordFieldLabel").add("passwordFieldPrompt")
                .add("passwordFieldValidation").add("logInButtonLabel").add("logInCaption").storeIn(view);

        view.init();

        Mockito.verify(listener).viewInitialized(view);

        view.setUsername("mister_user");
        assertEquals(view.getUsername(), "mister_user");
    }

    public void navigatesToDefaultViewOnLogin() {
        LogInViewImpl view = new LogInViewImpl();

        UI mockUI = mock(UI.class);
        view.setParent(mockUI);
        Navigator mockNavigator = mock(Navigator.class);
        when(mockUI.getNavigator()).thenReturn(mockNavigator);

        view.loggedIn();

        verify(mockNavigator).navigateTo(DefaultView.VIEW_NAME);
    }

    public void displaysErrors() {
        LogInViewImpl view = new LogInViewImpl();

        UI mockUI = mock(UI.class);
        UI.setCurrent(mockUI);
        Page mockPage = mock(Page.class);
        when(mockUI.getPage()).thenReturn(mockPage);
        ArgumentCaptor<Notification> notification = ArgumentCaptor.forClass(Notification.class);
        doNothing().when(mockPage).showNotification(notification.capture());

        view.reportError("that's kinda bad");

        assertEquals(notification.getValue().getCaption(), "that's kinda bad");
    }

    public void passesClickToListeners() {
        LogInViewImpl view = new LogInViewImpl();
        LogInView.LogInViewListener listener = mock(LogInView.LogInViewListener.class);
        ReflectionTestUtils.setField(view, "listeners", Collections.singleton(listener));

        I18N messages = new FakeI18N().add("usernameFieldLabel").add("usernameFieldPrompt")
                .add("usernameFieldValidation").add("passwordFieldLabel").add("passwordFieldPrompt")
                .add("passwordFieldValidation").add("logInButtonLabel").add("logInCaption").storeIn(view);

        view.init();

        Button logInButton = ComponentFinder.find(view, Button.class,
                component -> (component.getCaption().equals(messages.get("logInButtonLabel"))));
        assertNotNull(logInButton);
        logInButton.click();

        verify(listener).logInClicked();
    }
}

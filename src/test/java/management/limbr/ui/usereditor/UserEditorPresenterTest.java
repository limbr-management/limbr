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

import management.limbr.data.UserRepository;
import management.limbr.data.model.User;
import management.limbr.data.model.util.UserUtil;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@Test
public class UserEditorPresenterTest {

    private UserEditorPresenter presenter;
    private UserEditorView view;
    private UserRepository repository;
    private UserUtil userUtil;

    @BeforeMethod
    public void beforeMethod() {
        view = mock(UserEditorView.class);
        repository = mock(UserRepository.class);
        userUtil = mock(UserUtil.class);

        presenter = new UserEditorPresenter(repository, userUtil);
        presenter.viewInitialized(view);
    }

    public void hidesView() {
        presenter.hide();
        verify(view).hide();
    }

    public void editsNewUser() {
        User user = mock(User.class);

        when(user.getId()).thenReturn(null);
        when(user.getUsername()).thenReturn("username");
        when(user.getEmailAddress()).thenReturn("email");
        when(user.getDisplayName()).thenReturn("User");

        presenter.editUser(user);

        verify(view).setCancelVisible(false);
        verify(view).setUsername("username");
        verify(view).setPassword(anyString());
        verify(view).setEmailAddress("email");
        verify(view).setDisplayName("User");
        verify(view).show();
    }

    public void editsExistingUser() {
        User user = mock(User.class);

        when(user.getId()).thenReturn(42L);
        when(repository.findOne(42L)).thenReturn(user);
        when(user.getUsername()).thenReturn("username");
        when(user.getEmailAddress()).thenReturn("email");
        when(user.getDisplayName()).thenReturn("User");

        presenter.editUser(user);

        verify(view).setCancelVisible(true);
        verify(view).setUsername("username");
        verify(view).setPassword(anyString());
        verify(view).setEmailAddress("email");
        verify(view).setDisplayName("User");
        verify(view).show();
    }

    public void savesUser() {
        User user = mock(User.class);
        // normally "user" would already be set by calling editUser
        ReflectionTestUtils.setField(presenter, "user", user);

        when(view.getUsername()).thenReturn("username");
        when(view.getPassword()).thenReturn("password");
        when(userUtil.generatePasswordHash("username", "password")).thenReturn("hash");
        when(view.getEmailAddress()).thenReturn("email");
        when(view.getDisplayName()).thenReturn("User");

        presenter.save();

        verify(user).setUsername("username");
        verify(user).setPasswordHash("hash");
        verify(user).setEmailAddress("email");
        verify(user).setDisplayName("User");

        verify(view).hide();
    }

    public void deletesUser() {
        User user = mock(User.class);
        // normally "user" would already be set by calling editUser
        ReflectionTestUtils.setField(presenter, "user", user);

        presenter.delete();

        verify(repository).delete(user);

        verify(view).hide();
    }
}

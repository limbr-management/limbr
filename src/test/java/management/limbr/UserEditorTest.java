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

import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import management.limbr.data.UserRepository;
import management.limbr.data.model.User;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.AssertJUnit.assertTrue;

@Test
public class UserEditorTest {
    public void editsNewUser() {
        UserRepository repository = mock(UserRepository.class);
        UserEditor userEditor = new UserEditor(repository);
        userEditor.editUser(new User("hey", "", "", ""));

        TextField userName = (TextField) ReflectionTestUtils.getField(userEditor, "userName");
        assertEquals(userName.getValue(), "hey");
        Button cancelButton = (Button) ReflectionTestUtils.getField(userEditor, "cancel");
        assertFalse(cancelButton.isVisible());
    }

    public void editsExistingUser() {
        UserRepository repository = mock(UserRepository.class);
        UserEditor userEditor = new UserEditor(repository);
        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getUsername()).thenReturn("joe");
        when(mockUser.getDisplayName()).thenReturn("Joe");
        when(mockUser.getEmailAddress()).thenReturn("joe@schmoe.com");

        when(repository.findOne(1L)).thenReturn(mockUser);

        userEditor.editUser(mockUser);

        TextField userName = (TextField) ReflectionTestUtils.getField(userEditor, "userName");
        assertEquals(userName.getValue(), "joe");
        Button cancelButton = (Button) ReflectionTestUtils.getField(userEditor, "cancel");
        assertTrue(cancelButton.isVisible());

        userName.setValue("notJoe");

        Button saveButton = (Button) ReflectionTestUtils.getField(userEditor, "save");
        saveButton.click();

        verify(mockUser).setUsername("notJoe");
    }
}

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

import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.UI;
import management.limbr.data.UserRepository;
import management.limbr.data.model.User;
import management.limbr.ui.VaadinUI;
import management.limbr.ui.usereditor.UserEditorPresenter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

@Test
public class UsersPresenterTest {

    private UserRepository repository;
    private UsersPresenter presenter;
    private UserEditorPresenter userEditorPresenter;

    @BeforeMethod
    public void beforeMethod() {
        VaadinUI ui = mock(VaadinUI.class);
        UI.setCurrent(ui);
        UsersView view = mock(UsersView.class);
        repository = mock(UserRepository.class);
        userEditorPresenter = mock(UserEditorPresenter.class);

        presenter = new UsersPresenter(repository, userEditorPresenter);
        presenter.viewInitialized(view);
        presenter.init();

        verify(userEditorPresenter).setUserChangeHandler(presenter);
    }

    public void listsUsersEmptyFilter() {
        User mockUser = mock(User.class);
        when(repository.findAll()).thenReturn(Collections.singletonList(mockUser));
        BeanItemContainer<User> result = presenter.listUsers("");
        assertEquals(result.size(), 1);
        assertEquals(result.getIdByIndex(0), mockUser);
    }

    public void listsUsersWithFilter() {
        User mockUser = mock(User.class);
        when(repository.findByUsernameStartsWithIgnoreCase("filter")).thenReturn(Collections.singletonList(mockUser));
        BeanItemContainer<User> result = presenter.listUsers("filter");
        assertEquals(result.size(), 1);
        assertEquals(result.getIdByIndex(0), mockUser);
    }

    public void showsEditorWhenUserDoubleClicked() {
        User mockUser = mock(User.class);
        BeanItem<User> item = new BeanItem<>(mockUser);

        presenter.itemDoubleClicked(item);

        verify(userEditorPresenter).editUser(mockUser);
    }

    public void showsEditorWhenAddNewClicked() {
        presenter.addNewClicked();

        verify(userEditorPresenter).editUser(any(User.class));
    }
}
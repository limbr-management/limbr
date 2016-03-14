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

package management.limbr.ui.roles;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.UI;
import management.limbr.data.RoleRepository;
import management.limbr.data.model.Role;
import management.limbr.ui.VaadinUI;
import management.limbr.ui.roleeditor.RoleEditorPresenter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

@Test
public class RolesPresenterTest {
    private RoleRepository repository;
    private RolesPresenter presenter;
    private RoleEditorPresenter editor;
    private RolesView view;

    @BeforeMethod
    public void beforeMethod() {
        VaadinUI ui = mock(VaadinUI.class);
        UI.setCurrent(ui);
        view = mock(RolesView.class);
        repository = mock(RoleRepository.class);
        editor = mock(RoleEditorPresenter.class);

        presenter = new RolesPresenter(repository, editor);
        presenter.viewInitialized(view);
    }

    public void listsRolesEmptyFilter() {
        Role mockRole = mock(Role.class);
        when(repository.findAll()).thenReturn(Collections.singletonList(mockRole));
        BeanItemContainer<Role> result = presenter.listEntities("");
        assertEquals(result.size(), 1);
        assertEquals(result.getIdByIndex(0), mockRole);
    }

    public void listsRolesWithFilter() {
        Role mockRole = mock(Role.class);
        when(repository.findByNameStartsWithIgnoreCase("filter")).thenReturn(Collections.singletonList(mockRole));
        BeanItemContainer<Role> result = presenter.listEntities("filter");
        assertEquals(result.size(), 1);
        assertEquals(result.getIdByIndex(0), mockRole);
    }

    public void showsEditorWhenEditButtonClicked() {
        Role role = mock(Role.class);

        presenter.editItemClicked(role);

        verify(editor).edit(role);
    }

    public void hidesEditorWhenNothingClicked() {
        presenter.editItemClicked(null);

        verify(editor).hide();
    }

    public void createsNewRole() {
        presenter.addNewClicked();

        verify(editor).edit(any(Role.class));
    }

    public void refreshesOnChange() {
        presenter.onEntityChanged();

        verify(view).refresh();
    }

}

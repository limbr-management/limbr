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

import management.limbr.ui.ClientState;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Test
public class LogInPresenterTest {
    public void logsInWhenClicked() {
        ClientState clientState = mock(ClientState.class);
        LogInView view = mock(LogInView.class);
        LogInPresenter presenter = new LogInPresenter(clientState);
        presenter.viewInitialized(view);
        presenter.logInClicked();

        verify(clientState).setLoggedIn(true);
        verify(view).loggedIn();
    }
}

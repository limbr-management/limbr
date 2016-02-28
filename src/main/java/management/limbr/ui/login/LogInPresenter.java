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
import management.limbr.ui.Presenter;
import org.springframework.beans.factory.annotation.Autowired;

@Presenter
public class LogInPresenter implements LogInView.LogInViewListener {

    private transient LogInView view;

    private ClientState clientState;

    @Autowired
    public LogInPresenter(ClientState clientState) {
        this.clientState = clientState;
    }

    @Override
    public void viewInitialized(LogInView view) {
        this.view = view;
    }

    @Override
    public void logInClicked() {
        // TODO: implement
        clientState.setLoggedIn(true);
        view.loggedIn();
    }
}

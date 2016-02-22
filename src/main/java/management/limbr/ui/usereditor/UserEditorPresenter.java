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

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import management.limbr.data.UserRepository;
import management.limbr.data.model.User;
import management.limbr.data.model.util.UserUtil;
import management.limbr.ui.Presenter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

@Presenter
public class UserEditorPresenter implements UserEditorView.UserEditorViewListener, Serializable {
    @SuppressWarnings("squid:S2068") // it's not a hard-coded password
    public static final String PASSWORD_PLACEHOLDER = "$notPass$12345678";

    @Autowired
    private transient UserRepository repository;

    @Autowired
    private transient UserUtil userUtil;

    private User user;

    private transient UserEditorView view;

    private transient UserChangeHandler userChangeHandler;

    public void hide() {
        view.hide();
    }

    public void editUser(User u) {
        final boolean persisted = u.getId() != null;

        if (persisted) {
            user = repository.findOne(u.getId());
        } else {
            user = u;
        }

        view.setCancelVisible(persisted);

        BeanFieldGroup.bindFieldsUnbuffered(user, this);

        view.setUsername(u.getUsername());
        view.setPassword(PASSWORD_PLACEHOLDER);
        view.setDisplayName(u.getDisplayName());
        view.setEmailAddress(u.getEmailAddress());

        view.show();
    }

    @Override
    public void save() {
        String username = view.getUsername();
        user.setUsername(username);
        String password = view.getPassword();
        if (!PASSWORD_PLACEHOLDER.equals(password)) {
            user.setPasswordHash(userUtil.generatePasswordHash(username, password));
        }
        user.setDisplayName(view.getDisplayName());
        user.setEmailAddress(view.getEmailAddress());

        repository.save(user);

        if (userChangeHandler != null) {
            userChangeHandler.onUserChanged();
        }

        view.hide();
    }

    @Override
    public void delete() {
        // TODO: ask if they're sure!

        repository.delete(user);

        if (userChangeHandler != null) {
            userChangeHandler.onUserChanged();
        }

        view.hide();
    }

    @Override
    public void cancel() {
        view.hide();
    }

    @Override
    public void viewInitialized(UserEditorView view) {
        this.view = view;
    }

    @FunctionalInterface
    public interface UserChangeHandler {
        void onUserChanged();
    }

    public void setUserChangeHandler(UserChangeHandler userChangeHandler) {
        this.userChangeHandler = userChangeHandler;
    }
}

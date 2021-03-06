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

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import management.limbr.data.model.User;
import management.limbr.ui.PrivilegeLevels;
import management.limbr.ui.RequiresPrivilege;
import management.limbr.ui.entity.EntityListView;
import management.limbr.ui.entity.EntityListViewImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;

import java.util.Collection;

@RequiresPrivilege(level = PrivilegeLevels.ADMIN)
@UIScope
@SpringView(name = UsersViewImpl.VIEW_NAME)
public class UsersViewImpl extends EntityListViewImpl<User> {
    public static final String VIEW_NAME = "users";

    @Autowired
    public UsersViewImpl(Collection<EntityListView.Listener<User>> listeners, I18N messages) {
        super(User.class, listeners, messages);
    }
}

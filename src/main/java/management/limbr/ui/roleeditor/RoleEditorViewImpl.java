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

package management.limbr.ui.roleeditor;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import management.limbr.data.model.Role;
import management.limbr.data.model.util.EntityUtil;
import management.limbr.ui.entity.EntityEditorView;
import management.limbr.ui.entity.EntityEditorViewImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.i18n.I18N;

import java.util.Collection;

@SpringComponent
@UIScope
public class RoleEditorViewImpl extends EntityEditorViewImpl<Role> {
    @Autowired
    public RoleEditorViewImpl(Collection<EntityEditorView.Listener<Role>> listeners, I18N messages, EntityUtil entityUtil) {
        super(Role.class, listeners, messages, entityUtil);
    }
}

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

package management.limbr.ui.error;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import management.limbr.ui.PrivilegeLevels;
import management.limbr.ui.RequiresPrivilege;

@RequiresPrivilege(level = PrivilegeLevels.NONE)
@UIScope
@SpringView(name = ErrorView.VIEW_NAME)
@SuppressWarnings({"squid:S2160"}) // don't need to override equals here
public class ErrorView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "error";

    // TODO: Figure out how to handle localization and such in this class.
    // It gets instantiated directly with new ErrorView(), not with Spring,
    // so @Autowired doesn't work.

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        addComponent(new Label("An error happened."));
    }
}

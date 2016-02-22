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

package management.limbr.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import management.limbr.ui.users.UsersViewImpl;
import org.springframework.beans.factory.annotation.Autowired;

@Theme("valo")
@SpringUI(path = "")
public class VaadinUI extends UI {
    @Autowired
    SpringViewProvider viewProvider;

    // TODO: We should have either a ViewAccessControl or a ViewInstanceAccessControl for security
    // (just make a bean -- Spring will give it to the SpringViewProvider)

    @Override
    protected void init(VaadinRequest request) {

        final VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        root.setMargin(true);
        root.setSpacing(true);
        setContent(root);

        Image logo = new Image(null, new ExternalResource("images/logo1.png"));
        logo.setHeight(1.2f, Unit.EM);
        logo.setWidthUndefined();

        CssLayout navBar = new CssLayout();
        navBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        navBar.addComponent(logo);
        navBar.addComponent(createNavButton("Users", UsersViewImpl.VIEW_NAME));
        root.addComponent(navBar);

        final Panel viewContainer = new Panel();
        viewContainer.setSizeFull();
        root.addComponent(viewContainer);
        root.setExpandRatio(viewContainer, 1.0f);

        Navigator navigator = new Navigator(this, viewContainer);
        navigator.addProvider(viewProvider);

    }

    private Button createNavButton(String caption, String view) {
        Button button = new Button(caption);
        button.addStyleName(ValoTheme.BUTTON_SMALL);
        button.addClickListener(e -> getUI().getNavigator().navigateTo(view));
        return button;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof VaadinUI) {
            return viewProvider.equals(((VaadinUI) obj).viewProvider) && super.equals(obj);
        }
        return false;
    }
}

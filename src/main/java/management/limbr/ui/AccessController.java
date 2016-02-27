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

import com.vaadin.spring.access.ViewAccessControl;
import com.vaadin.ui.UI;
import management.limbr.ui.view.DefaultView;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Provides security to views.
 *
 * If a view is restricted to admins by @RequiresPrivilege(level = PrivilegeLevels.Admin),
 * for example, then the view will not be available to anyone who doesn't have at least
 * Admin level.
 */
@Component
public class AccessController implements ViewAccessControl, ApplicationContextAware {
    private ApplicationContext applicationContext;

    /**
     * Determines if access can be granted to a particular user to a particular view.
     * Gets the current user from the UI.
     *
     * @param ui The UI the user is using.
     * @param beanName The name of the bean of the view the user is trying to access.
     * @return true if the user can access it, otherwise false.
     */
    @Override
    public boolean isAccessGranted(UI ui, String beanName) {
        if (ui instanceof VaadinUI) {
            VaadinUI vaadinUI = (VaadinUI)ui;

            RequiresPrivilege annotation;

            if ("defaultView".equals(beanName)) {
                // When the app is first loading, no view is selected yet and we'll crash if we call
                // getBean, so we have a special case for this one.
                annotation = DefaultView.class.getAnnotation(RequiresPrivilege.class);
            } else {
                annotation = applicationContext.getBean(beanName).getClass().getAnnotation(RequiresPrivilege.class);
            }

            if (annotation != null && vaadinUI.getUserLevel().hasLevel(annotation.level())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

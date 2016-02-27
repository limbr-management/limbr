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

package management.limbr.test.util;

import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

public class ComponentFinder {
    public interface ComponentMatcher {
        boolean matches(Component component);
    }

    protected ComponentFinder() {
        // do no instantiate
    }

    @SuppressWarnings("unchecked")
    public static <T extends Component> T find(HasComponents parent, Class<T> clazz, ComponentMatcher matcher) {
        for (Component component : parent) {
            if (component instanceof HasComponents) {
                T found = find((HasComponents)component, clazz, matcher);
                if (found != null) {
                    return found;
                }
            } else {
                if (clazz.isInstance(component) && matcher.matches(component)) {
                    return (T)component;
                }
            }
        }

        return null;
    }

}

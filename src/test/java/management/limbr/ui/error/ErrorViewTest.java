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

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import management.limbr.test.util.FakeI18N;
import org.mockito.Mockito;
import org.testng.annotations.Test;
import org.vaadin.spring.i18n.I18N;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Test
public class ErrorViewTest {
    public void showsBasicMessage() {
        ErrorView errorView = new ErrorView();
        I18N messages = new FakeI18N().add("errorHappened").storeIn(errorView);

        errorView.enter(Mockito.mock(ViewChangeListener.ViewChangeEvent.class));

        assertEquals(errorView.getComponentCount(), 1);
        assertTrue(errorView.getComponent(0) instanceof Label);
        assertEquals(((Label)errorView.getComponent(0)).getValue(), messages.get("errorHappened"));
    }
}

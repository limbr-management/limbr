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

import org.springframework.context.ApplicationContext;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

@Test
public class AccessControllerTest {
    public void grantsAccessBasedOnAnnotation() {
        ApplicationContext mockContext = mock(ApplicationContext.class);
        ClientState clientState = mock(ClientState.class);
        AccessController accessController = new AccessController(clientState);
        accessController.setApplicationContext(mockContext);

        @RequiresPrivilege(level = PrivilegeLevels.ADMIN)
        class ABean {}
        ABean aBean = new ABean();

        when(mockContext.getBean("bean")).thenReturn(aBean);
        when(clientState.getUserLevel()).thenReturn(PrivilegeLevels.ADMIN);

        assertEquals(accessController.isAccessGranted(null, "bean"), true);

        verify(mockContext).getBean("bean");
        verify(clientState).getUserLevel();
    }

    public void grantsAccessToDefaultViewBasedOnAnnotation() {
        ApplicationContext mockContext = mock(ApplicationContext.class);
        ClientState clientState = mock(ClientState.class);
        AccessController accessController = new AccessController(clientState);
        accessController.setApplicationContext(mockContext);

        when(clientState.getUserLevel()).thenReturn(PrivilegeLevels.NONE);

        assertEquals(accessController.isAccessGranted(null, "defaultView"), true);

        verifyNoMoreInteractions(mockContext);
        verify(clientState).getUserLevel();
    }
}

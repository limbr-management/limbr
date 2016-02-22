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

package management.limbr.data.model.util;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Test
public class UserUtilTest {
    public void saltsAndHashesPassword() {
        UserUtil userUtil = new UserUtil();
        assertEquals(userUtil.generatePasswordHash("admin", "admin"), "e9bb0f231e2f35658dda443345a46f5d");
    }
}

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

import management.limbr.data.model.BaseEntity;
import management.limbr.data.model.DisplayName;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test
public class EntityUtilTest {
    public void saltsAndHashesPassword() {
        EntityUtil entityUtil = new EntityUtil();
        assertEquals(entityUtil.generatePasswordHash("admin", "admin"), "e9bb0f231e2f35658dda443345a46f5d");
    }

    public class TestEntity extends BaseEntity {
        private Long id;

        @DisplayName
        private String name;

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public void callsSetterForField() {
        TestEntity entity = new TestEntity();
        EntityUtil entityUtil = new EntityUtil();
        entityUtil.callSetter(entity, "id", 5L);
        assertEquals((long)entity.getId(), 5L);
    }

    public void callsGetterForField() {
        TestEntity entity = new TestEntity();
        entity.setId(6L);
        EntityUtil entityUtil = new EntityUtil();
        assertEquals(entityUtil.callGetter(entity, "id"), 6L);
    }

    public void getsEntityDisplayName() {
        TestEntity entity = new TestEntity();
        entity.setName("Bob");
        EntityUtil entityUtil = new EntityUtil();
        assertEquals(entityUtil.getDisplayName(entity), "Bob");
    }

    public class TestEntityWithNoDisplayName extends BaseEntity {
        private Long id;

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public void setId(Long id) {
            this.id = id;
        }
    }

    public void returnsClassNameIfNoDisplayName() {
        TestEntityWithNoDisplayName entity = new TestEntityWithNoDisplayName();

        EntityUtil entityUtil = new EntityUtil();
        assertEquals(entityUtil.getDisplayName(entity), "TestEntityWithNoDisplayName");
    }
}

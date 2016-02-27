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

import org.springframework.test.util.ReflectionTestUtils;
import org.vaadin.spring.i18n.I18N;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows mocking of I18N messages.
 *
 * Follows a Builder pattern for creation.
 */
public class FakeI18N extends I18N {

    private Map<String, String> strings = new HashMap<>();

    private int count;

    public FakeI18N() {
        super(null);
    }

    public FakeI18N add(String id) {
        strings.put(id, "string_" + Integer.toHexString(count++));
        return this;
    }

    public FakeI18N storeIn(Object object) {
        ReflectionTestUtils.setField(object, "messages", this, I18N.class);
        return this;
    }

    @Override
    public String get(String code, Object... arguments) {
        StringBuilder result = new StringBuilder(strings.get(code));
        for (Object arg : arguments) {
            result.append('|');
            result.append(arg.toString());
        }
        return result.toString();
    }
}

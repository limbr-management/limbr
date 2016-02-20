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

package management.limbr.data.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Base class to be used by model entities.
 */
public class BaseEntity {
    /**
     * Gets a textual representation of a POJO.
     *
     * @return JSON-like representation containing any fields with getters
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        Class clazz = getClass();

        builder.append("{class:\"");
        builder.append(clazz.getCanonicalName());
        builder.append("\",");

        for (Method method : clazz.getMethods()) {
            String methodName = method.getName();
            if (methodName.startsWith("get") || methodName.startsWith("is") || methodName.startsWith("has")) {
                try {
                    // get the value FIRST so we don't append other things if we can't get it
                    String value = safeValue(method.invoke(this));
                    if (value == null) {
                        value = "null";
                    }
                    builder.append(methodNameToFieldName(methodName));
                    builder.append(':');
                    builder.append(value);
                    builder.append(',');
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    // skip it
                }
            }
        }

        if (builder.charAt(builder.length() - 1) == ',') {
            builder.deleteCharAt(builder.length() - 1);
        }

        builder.append("}");

        return builder.toString();
    }

    private String safeValue(Object object) {
        if (object instanceof String) {
            return "\"" + object.toString().replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r") + "\"";
        }

        return object.toString();
    }

    private String methodNameToFieldName(String methodName) {
        String theRest;
        if (methodName.startsWith("get") || methodName.startsWith("has")) {
            theRest = methodName.substring(3);
        } else if (methodName.startsWith("is")) {
            theRest = methodName.substring(2);
        } else {
            theRest = methodName;
        }

        StringBuilder response = new StringBuilder();
        response.append(theRest.substring(0, 1).toLowerCase());
        if (theRest.length() > 1) {
            response.append(theRest.substring(1));
        }

        return response.toString();
    }
}

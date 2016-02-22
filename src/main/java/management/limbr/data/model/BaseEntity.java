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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Base class to be used by model entities.
 */
public abstract class BaseEntity implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(BaseEntity.class);

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
        builder.append(clazz.getTypeName());
        builder.append("\",");

        // we need to sort the methods so they come out in a predictable order
        Method[] methods = clazz.getMethods();
        Arrays.sort(methods,
                (Method a, Method b) -> methodNameToFieldName(a.getName()).compareTo(methodNameToFieldName(b.getName())));

        for (Method method : methods) {
            String methodName = method.getName();
            boolean isProbablyGetter =(methodName.startsWith("get")
                    || methodName.startsWith("is")
                    || methodName.startsWith("has"));

            if (isProbablyGetter && !("hashCode".equals(methodName) || "getClass".equals(methodName))) {

                try {
                    // get the value FIRST so we don't append other things if we can't get it
                    String value = safeValue(method.invoke(this));
                    builder.append(methodNameToFieldName(methodName));
                    builder.append(':');
                    builder.append(value);
                    builder.append(',');
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    log.debug("Trying to call method {0} on entity of type {1} threw an exception.", methodName, clazz.getTypeName(), ex);
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
        if (object == null) {
            return "null";
        }

        if (object instanceof String) {
            return "\"" + object.toString().replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r") + "\"";
        }

        return object.toString();
    }

    private static String methodNameToFieldName(String methodName) {
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

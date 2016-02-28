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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * Tests a POJO's getters and setters.
 */
public class PojoTester<T> {

    private T pojo;

    private static final Random random = new Random();

    private PojoTester(T pojo) {
        this.pojo = pojo;
    }

    public static <V> PojoTester<V> createFor(V pojo) {
        return new PojoTester<>(pojo);
    }

    @SuppressWarnings("unchecked")
    public void test() {
        Class<T> clazz = (Class<T>)pojo.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = field.getName();
            Method getter = findGetter(clazz, fieldName, field.getType().equals(Boolean.class));
            if (getter != null) {
                testGetter(getter, field);
            }

            Method setter = findSetter(clazz, fieldName, field.getType());
            if (setter != null) {
                testSetter(setter, field);
            }
        }
    }

    private void testGetter(Method getter, Field field) {
        Object testValue = getTestValueFor(field.getType());
        ReflectionTestUtils.setField(pojo, field.getName(), testValue);
        try {
            assertEquals(getter.invoke(pojo), testValue);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            fail("Couldn't access POJO's getter " + getter.getName(), ex);
        }
    }

    private Object getTestValueFor(Class<?> type) {
        if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return random.nextBoolean();
        } else if (type.equals(byte.class) || type.equals(Byte.class)) {
            return (byte) random.nextInt(Byte.MAX_VALUE);
        } else if (type.equals(short.class) || type.equals(Short.class)) {
            return (short) random.nextInt(Short.MAX_VALUE);
        } else if (type.equals(int.class) || type.equals(Integer.class)) {
            return random.nextInt();
        } else if (type.equals(long.class) || type.equals(Long.class)) {
            return random.nextLong();
        } else if (type.equals(char.class) || type.equals(Character.class)) {
            return (char) random.nextInt(Character.MAX_VALUE);
        } else if (type.equals(float.class) || type.equals(Float.class)) {
            return random.nextFloat();
        } else if (type.equals(double.class) || type.equals(Double.class)) {
            return random.nextDouble();
        } else if (type.equals(String.class)) {
            return Long.toString(random.nextLong());
        } else {
            throw new IllegalStateException("PojoTester does not yet support reference types like " + type.getName());
        }
    }

    private void testSetter(Method setter, Field field) {
        Object testValue = getTestValueFor(field.getType());
        try {
            setter.invoke(pojo, testValue);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            fail("Couldn't access POJO's setter " + setter.getName(), ex);
        }
        assertEquals(ReflectionTestUtils.getField(pojo, field.getName()), testValue);
    }

    private Method findSetter(Class<T> clazz, String fieldName, Class<?> fieldType) {
        final String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            return clazz.getMethod(methodName, fieldType);
        } catch (NoSuchMethodException | SecurityException ex) {
            return null;
        }
    }

    private Method findGetter(Class<T> clazz, String fieldName, boolean isBool) {
        final String methodName = (isBool ? "is" : "get") + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        try {
            return clazz.getMethod(methodName);
        } catch (NoSuchMethodException | SecurityException ex) {
            return null;
        }
    }
}

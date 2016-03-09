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

import com.vaadin.spring.annotation.SpringComponent;
import management.limbr.data.model.BaseEntity;
import management.limbr.data.model.DisplayName;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringComponent
public class EntityUtil {
    private static final Logger LOG = LoggerFactory.getLogger(EntityUtil.class);
    private static final String SALT = "The ocean is salty.";

    public String generatePasswordHash(String userid, String password) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            md5.update(userid.getBytes());
            md5.update(SALT.getBytes());
            md5.update(password.getBytes());
            byte[] digest = md5.digest();

            return Hex.encodeHexString(digest);

        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Could not load MD5 for hashing passwords", ex);
        }
    }

    public String getDisplayName(BaseEntity entity) {
        if (entity == null) {
            return "null";
        }

        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.getAnnotation(DisplayName.class) != null) {
                Object value = callGetter(entity, field.getName());
                if (value == null) {
                    return "null";
                } else {
                    return value.toString();
                }
            }
        }

        return entity.getClass().getSimpleName();
    }

    public Object callGetter(BaseEntity entity, String fieldName) {
        Method getter;
        try {
            getter = entity.getClass().getDeclaredMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
        } catch (NoSuchMethodException ex1) {
            LOG.debug("Didn't find getter starting with \"get\" for " + fieldName + " in " + entity.getClass().getName(), ex1);
            try {
                getter = entity.getClass().getDeclaredMethod("is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
            } catch (NoSuchMethodException ex2) {
                LOG.debug("Didn't find getter for " + fieldName + " in " + entity.getClass().getName(), ex2);
                return null;
            }
        }

        try {
            return getter.invoke(entity);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            LOG.debug("Couldn't execute getter: " + getter, ex);
            return entity;
        }
    }

    public void callSetter(BaseEntity entity, String fieldName, Object value) {
        Method setter;
        try {
            LOG.debug("Looking for setter for " + fieldName + " in entity " + entity + " with value " + value);
            setter = entity.getClass().getDeclaredMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), value.getClass());
        } catch (NoSuchMethodException ex) {
            LOG.debug("Didn't find setter for " + fieldName + " in " + entity.getClass().getName(), ex);
            return;
        }

        try {
            setter.invoke(entity, value);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            LOG.debug("Couldn't execute setter: " + setter, ex);
        }
    }
}

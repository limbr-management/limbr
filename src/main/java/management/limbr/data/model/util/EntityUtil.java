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
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringComponent
public class EntityUtil {
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
}

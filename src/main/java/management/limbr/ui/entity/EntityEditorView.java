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

package management.limbr.ui.entity;

import management.limbr.data.model.BaseEntity;

@SuppressWarnings("squid:S2326") // Type parameter is used to identify different views for a collection from Spring
public interface EntityEditorView<T> {
    <U> void setFieldValue(String name, U value);
    <U> U getFieldValue(Class<U> valueClass, String name);

    void setDeleteVisible(boolean visible);

    void show();
    void hide();

    @SuppressWarnings("squid:S2326") // Type parameter is used to identify different views for a collection from Spring
    interface Listener<T extends BaseEntity> {
        void save();
        void delete();
        void cancel();
    }
}

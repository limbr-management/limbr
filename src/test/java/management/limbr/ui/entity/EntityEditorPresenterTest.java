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

import management.limbr.data.model.util.EntityUtil;
import org.mockito.ArgumentMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

@Test
public class EntityEditorPresenterTest {

    class TestView implements EntityEditorView<TestBean> {

        @Override
        public <U> void setFieldValue(String name, U value) {

        }

        @Override
        public <U> U getFieldValue(Class<U> valueClass, String name) {
            return null;
        }

        @Override
        public void setDeleteVisible(boolean visible) {

        }

        @Override
        public void show() {

        }

        @Override
        public void hide() {

        }

        @Override
        public void confirmDelete(TestBean entity) {

        }
    }

    public class TestPresenter extends EntityEditorPresenter<TestBean> {

        public TestPresenter(JpaRepository<TestBean, Long> repository, EntityUtil entityUtil) {
            super(repository, entityUtil);
        }

        @Override
        protected Class<? extends EntityEditorView<TestBean>> getViewBeanClass() {
            return TestView.class;
        }
    }

    private JpaRepository<TestBean, Long> repository;
    private EntityEditorPresenter<TestBean> presenter;
    private TestView testView;

    @BeforeMethod
    @SuppressWarnings({"unchecked"})
    public void beforeMethod() {
        repository = mock(JpaRepository.class);

        presenter = new TestPresenter(repository, new EntityUtil());

        ApplicationContext applicationContext = mock(ApplicationContext.class);
        testView = mock(TestView.class);
        when(applicationContext.getBean(TestView.class)).thenReturn(testView);

        presenter.setApplicationContext(applicationContext);
    }

    public void hidesView() {
        presenter.hide();

        verify(testView).hide();
    }

    public void editsExistingEntity() throws Exception {
        TestBean pinto = new TestBean();
        pinto.setId(42L);
        pinto.setSomething("whatever");
        pinto.setPasswordHash(new EntityUtil().generatePasswordHash("42", "password"));

        when(repository.findOne(42L)).thenReturn(pinto);

        presenter.edit(pinto);

        verify(testView).setDeleteVisible(true);
        verify(testView).setFieldValue("something", "whatever");
        verify(testView).setFieldValue(eq("passwordHash"), argThat(new ArgumentMatcher<String>() {
            @Override
            public boolean matches(Object o) {
                return !pinto.getPasswordHash().equals(o);
            }
        }));
        verify(testView).show();
    }

    public void editsNewEntity() {
        TestBean pinto = new TestBean();

        presenter.edit(pinto);

        verify(testView).setDeleteVisible(false);
        verify(testView).setFieldValue("something", "");
        verify(testView).show();

        verifyNoMoreInteractions(repository);
    }

    public void savesEntity() {
        TestBean pinto = new TestBean();
        pinto.setId(42L);
        pinto.setSomething("whatever");
        ReflectionTestUtils.setField(presenter, "entity", pinto);

        when(testView.getFieldValue(String.class, "something")).thenReturn("whatever");
        when(testView.getFieldValue(String.class, "passwordHash")).thenReturn("password");

        presenter.save();

        assertEquals(pinto.getSomething(), "whatever");
    }

    public void confirmsDelete() {
        TestBean pinto = new TestBean();
        ReflectionTestUtils.setField(presenter, "entity", pinto);

        presenter.deleteClicked();

        verify(testView).confirmDelete(pinto);
    }

    public void deletesEntity() {
        TestBean pinto = new TestBean();
        ReflectionTestUtils.setField(presenter, "entity", pinto);

        presenter.deleteConfirmed();

        verify(repository).delete(pinto);
    }

    public void cancelsEdit() {
        presenter.cancel();

        verify(testView).hide();
    }
}

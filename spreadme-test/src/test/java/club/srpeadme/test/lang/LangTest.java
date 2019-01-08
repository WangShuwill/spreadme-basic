/*
 *  Copyright (c) 2019 Wangshuwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package club.srpeadme.test.lang;

import club.spreadme.database.annotation.Delete;
import club.spreadme.database.annotation.Insert;
import club.spreadme.database.annotation.Query;
import club.spreadme.database.annotation.Update;
import club.spreadme.lang.Reflection;
import club.spreadme.lang.StringUtil;
import club.srpeadme.test.domain.Person;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LangTest {

    private static final List<Class> optionClazzes = Arrays.asList(
            Query.class, Update.class, Insert.class, Delete.class);

    @Test
    public void testStringUtil() {

        String source = "   Test  test TESTtest   , ";
        System.out.println(source);
        System.out.println(StringUtil.trimStart(source));
        System.out.println(StringUtil.trimEnd(source));
        System.out.println(StringUtil.trimAll(source));

        String target = "Test,test,";
        System.out.println(target);
        System.out.println(StringUtil.trimStart(target, 'T'));
        System.out.println(StringUtil.trimEnd(target, ','));

        System.out.println(StringUtil.trimStart(target, "i"));
        System.out.println(StringUtil.trimEnd(target, "t,"));

    }

    @Test
    public void testRelfectionUtil() {
        Person person = new Person();
        Reflection.setFieldValue(person, "id", System.currentTimeMillis());
        Reflection.setFieldValue(person, "name", "Jame");
        Reflection.setFieldValue(person, "age", 12);
        System.out.println(Reflection.parseBeanToMap(person));

        Reflection.doWithField(person.getClass(), System.out::println);

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAnnotation() throws NoSuchMethodException {
        Person person = new Person();
        Method method = person.getClass().getMethod("setId", Long.class);
        Object[] annotations = optionClazzes.stream().map(item -> Reflection.getAnnotation(method, item)).filter(item -> item != null)
                .toArray();

        System.out.println(Arrays.toString(annotations));

        List<Person> people = new ArrayList<>();
        System.out.println(Reflection.getSuperClassGenricType(people.getClass(), 0));
    }
}

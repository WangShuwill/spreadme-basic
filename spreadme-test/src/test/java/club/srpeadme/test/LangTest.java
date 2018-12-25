package club.srpeadme.test;

import club.spreadme.lang.StringUtil;
import club.spreadme.lang.Reflection;
import club.srpeadme.test.domain.Person;
import org.junit.Test;

public class LangTest {

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
}

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

package club.spreadme.basic;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import club.spreadme.basic.code.IdGenerator;
import club.spreadme.basic.code.support.TwitterLongIdGenerator;
import club.spreadme.basic.properties.PropertyManager;
import club.spreadme.basic.utils.ClassUtil;
import club.spreadme.basic.utils.IOUtil;
import club.spreadme.basic.utils.StringUtil;
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
        System.out.println(StringUtil.toUpper(target, 1, 2, 3));

        System.out.println(StringUtil.randomString(6));
    }

    @Test
    public void testClassUtil() {
        ClassLoader classLoader = ClassUtil.getClassLoader();
        System.out.println(classLoader);
        System.out.println(ClassUtil.getClassPaths("club.spreadme.basic", true));
        System.out.println(ClassUtil.getClassPath());
        System.out.println(ClassUtil.deduceMainClass());
    }

    @Test
    public void testPropertiesUtil() {
        PropertyManager.loadProperties();
        try {
            Enumeration<URL> urls = ClassLoader.getSystemResources("model.ftl");
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                System.out.println(url.getPath());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIDGenerator() {
        IdGenerator<Long> idGenerator = new TwitterLongIdGenerator();
        for (int i = 0; i < 100; i++)
            System.out.println(idGenerator.generate());
    }

    @Test
    public void testZip(){
        String targetPath = "/Users/wangshuwei/Downloads/notes";
        String newZip = "/Users/wangshuwei/Downloads/notes.zip";
        try {
            IOUtil.compress(new File(targetPath), new File(newZip));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDecompress(){
        String zipPath = "/Users/wangshuwei/Downloads/notes.zip";
        String dstPath = "/Users/wangshuwei/Downloads/";
        try {
            IOUtil.decompress(new File(zipPath), new File(dstPath));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

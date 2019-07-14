/*
 * Copyright (c) 2019 Wangshuwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package club.spreadme.core;

import java.io.File;
import java.io.IOException;

import club.spreadme.core.codec.Id;
import club.spreadme.core.codec.support.Snowflake;
import club.spreadme.core.utils.ClassUtil;
import club.spreadme.core.utils.StringUtil;
import club.spreadme.core.utils.ZipUtil;
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

        System.out.println(StringUtil.randomString(12));
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
    public void testIDGenerator() {
        Id<Long> idGenerator = new Snowflake();
        for (int i = 0; i < 100; i++)
            System.out.println(idGenerator.generate());
    }

    @Test
    public void testZip(){
        String targetPath = "/Users/wangshuwei/Downloads/CAPTAIN_AMERICA";
        String newZip = "/Users/wangshuwei/Downloads/CAPTAIN_AMERICA.zip";
        try {
            ZipUtil.compress(new File(targetPath), new File(newZip));
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
            ZipUtil.decompress(new File(zipPath), new File(dstPath));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/*
 *  Copyright (c) 2018 Wangshuwei
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package club.srpeadme.test;

import club.spreadme.database.parser.entity.SQLBuildType;
import club.spreadme.database.parser.support.InsertSQLBeanParser;
import club.srpeadme.test.domain.Person;
import org.junit.Test;

public class SQLParserTest {

    @Test
    public void testInsertSQLParser() {
        Person person = new Person();
        person.setId(System.currentTimeMillis());
        person.setName("James");
        person.setAge(24);

        System.out.println(new InsertSQLBeanParser().parse(person, SQLBuildType.INSERT));
    }

}

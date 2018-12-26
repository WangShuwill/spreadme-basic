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

import club.spreadme.database.core.datasource.SpreadDataSource;
import club.spreadme.database.core.executor.support.StreamExecutor;
import club.spreadme.database.parser.SQLParser;
import club.spreadme.database.parser.entity.SQLBuildType;
import club.spreadme.database.parser.support.BeanSQLParser;
import club.spreadme.database.parser.support.RoutingSQLParser;
import club.srpeadme.test.domain.Person;
import org.junit.Test;

import javax.sql.DataSource;

public class SQLParserTest {

    private static final String URL = "jdbc:mysql://192.168.52.128:3306/imdb?autoReconnect=true&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    @Test
    public void testInsertSQLParser() {
        Person person = new Person();
        person.setId(System.currentTimeMillis());
        person.setName("James");
        person.setAge(24);

        SQLParser sqlParser1 = new RoutingSQLParser(new BeanSQLParser(person, SQLBuildType.INSERT));
        System.out.println(sqlParser1.parse());

        SQLParser sqlParser2 = new RoutingSQLParser(new BeanSQLParser(person, SQLBuildType.UPDATE));
        System.out.println(sqlParser2.parse());

        SQLParser sqlParser3 = new RoutingSQLParser(new BeanSQLParser(person, SQLBuildType.DELETE));
        System.out.println(sqlParser3.parse());
    }

    @Test
    public void testCommonDAO() {
        DataSource dataSource = new SpreadDataSource(URL, USERNAME, PASSWORD);

        StreamExecutor streamExecutor = new StreamExecutor();
        streamExecutor.setDataSource(dataSource);
        streamExecutor.query("select * from movies").limit(2).forEach(System.out::println);
    }

}

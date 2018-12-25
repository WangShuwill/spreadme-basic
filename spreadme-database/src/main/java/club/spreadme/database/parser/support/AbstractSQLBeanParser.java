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

package club.spreadme.database.parser.support;

import club.spreadme.database.annotation.Table;
import club.spreadme.database.core.grammar.Record;
import club.spreadme.database.parser.entity.SQLBean;
import club.spreadme.lang.Reflection;

import java.util.Map;

public abstract class AbstractSQLBeanParser implements SQLBeanParser {

    protected SQLBean parseSQLBean(Object bean) {
        Table tableAnno = Reflection.getAnnotation(bean, Table.class);
        String tableName;
        String primaryKeyName;
        Map<String, Object> beanMap;
        if (bean instanceof Record) {
            tableName = tableAnno == null ? ((Record) bean).getTableName() : tableAnno.name();
            primaryKeyName = tableAnno == null ? ((Record) bean).getPrimaryKeyName() : tableAnno.primarykey();
            beanMap = (Record) bean;
        } else {
            tableName = tableAnno == null ? null : tableAnno.name();
            primaryKeyName = tableName == null ? null : tableAnno.primarykey();
            beanMap = Reflection.parseBeanToMap(bean);
        }
        return new SQLBean(tableName, primaryKeyName, beanMap);
    }

}

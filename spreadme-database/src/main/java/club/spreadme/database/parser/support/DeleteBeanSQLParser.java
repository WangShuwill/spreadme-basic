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

import club.spreadme.database.metadata.SQLOptionType;
import club.spreadme.database.parser.grammar.SQLBean;
import club.spreadme.database.parser.grammar.SQLParameter;
import club.spreadme.database.parser.grammar.SQLStatement;
import club.spreadme.lang.Assert;

import java.util.ArrayList;
import java.util.List;

public class DeleteBeanSQLParser extends BeanSQLParser {

    private Object bean;
    private SQLOptionType optionType;

    public DeleteBeanSQLParser(Object bean, SQLOptionType optionType) {
        super();
        this.bean = bean;
        this.optionType = optionType;
    }

    @Override
    public SQLStatement parse() {
        Assert.isTrue(optionType.equals(SQLOptionType.DELETE), "it is not delete sql build type");
        SQLBean sqlBean = parseSQLBean(bean);
        DeleteSQLBuilder sqlBuilder = new DeleteSQLBuilder(sqlBean.getTaleName());
        sqlBuilder.where(sqlBean.getPrimaryKeyName() + " = ?");
        List<SQLParameter> sqlParameters = new ArrayList<>();
        Object primaryKey = sqlBean.getValueMap().get(sqlBean.getPrimaryKeyName());
        sqlParameters.add(new SQLParameter(sqlBean.getPrimaryKeyName(), primaryKey.getClass(), primaryKey));
        return new SQLStatement(sqlBuilder.build().toString(), sqlParameters);
    }
}

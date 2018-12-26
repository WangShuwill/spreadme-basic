/*
 *  Copyright (c) 2018 Wangshuwei
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

package club.spreadme.database.parser.support;

import club.spreadme.database.parser.entity.SQLBean;
import club.spreadme.database.parser.entity.SQLBuildType;
import club.spreadme.database.parser.entity.SQLParameter;
import club.spreadme.database.parser.entity.SQLStatement;
import club.spreadme.lang.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateBeanSQLParser extends BeanSQLParser {

    private Object bean;
    private SQLBuildType buildType;

    public UpdateBeanSQLParser(Object bean, SQLBuildType buildType) {
        super();
        this.bean = bean;
        this.buildType = buildType;
    }

    @Override
    public SQLStatement parse() {
        Assert.isTrue(buildType.equals(SQLBuildType.UPDATE), "it is not update sql build type");
        SQLBean sqlBean = parseSQLBean(bean);
        UpdateSQLBuilder sqlBuilder = new UpdateSQLBuilder(sqlBean.getTaleName());
        SQLStatement sqlStatement = new SQLStatement();
        List<SQLParameter> sqlParameters = new ArrayList<>();
        for (Map.Entry<String, Object> entry : sqlBean.getValueMap().entrySet()) {
            if (entry.getValue() != null && !sqlBean.getPrimaryKeyName().toLowerCase().equals(entry.getKey().toLowerCase())) {
                sqlBuilder.set(entry.getKey() + " = ?");
                sqlParameters.add(new SQLParameter(entry.getKey(), entry.getValue().getClass(), entry.getValue()));
            }
        }

        sqlBuilder.where(sqlBean.getPrimaryKeyName() + " = ?");
        Object primaryKey = sqlBean.getValueMap().get(sqlBean.getPrimaryKeyName());
        sqlParameters.add(new SQLParameter(sqlBean.getPrimaryKeyName(), primaryKey.getClass(), primaryKey));
        sqlStatement.setSql(sqlBuilder.build().toString());
        sqlStatement.setSqlParameters(sqlParameters);
        return sqlStatement;
    }
}

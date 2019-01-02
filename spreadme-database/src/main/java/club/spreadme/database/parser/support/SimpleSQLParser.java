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

import club.spreadme.database.parser.ExpressionHandler;
import club.spreadme.database.parser.SQLParser;
import club.spreadme.database.parser.grammar.SQLParameter;
import club.spreadme.database.parser.grammar.SQLStatement;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class SimpleSQLParser implements SQLParser {

    private final String sql;
    private final SQLParameter[] sqlParameters;

    public SimpleSQLParser(String sql, SQLParameter[] sqlParameters) {
        this.sql = sql;
        this.sqlParameters = sqlParameters;
    }

    @Override
    public SQLStatement parse() {
        ExpressionHandler expressionHandler = new GenericExpressionHandler(this.sqlParameters);
        GenericTokenParser tokenParser = new GenericTokenParser(expressionHandler);
        String sql = tokenParser.parse(tokenParser.parse(this.sql, ExpressionHandler.PREPAREPLACEHOLDER),
                ExpressionHandler.STATICPLACEHOLDER);
        return new SQLStatement(sql, Arrays.stream(this.sqlParameters).filter(Objects::nonNull).collect(Collectors.toList()));
    }

    public String getSql() {
        return sql;
    }

    public SQLParameter[] getSqlParameters() {
        return sqlParameters;
    }
}

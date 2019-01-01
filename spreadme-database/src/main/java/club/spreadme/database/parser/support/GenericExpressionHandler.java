/*
 *  Copyright (c) 2019 Wangshuwei
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

import club.spreadme.database.exception.DAOMehtodException;
import club.spreadme.database.parser.ExpressionHandler;
import club.spreadme.database.parser.grammar.SQLParameter;

import java.util.Arrays;
import java.util.Objects;

public class GenericExpressionHandler implements ExpressionHandler {

    private static final String PREPAREPLACEHOLDER = "#{/}"; // =
    private static final String STATICPLACEHOLDER = "${/}"; // LIKE
    private final SQLParameter[] sqlParameters;

    public GenericExpressionHandler(SQLParameter[] sqlParameters) {
        this.sqlParameters = sqlParameters;
    }

    @Override
    public String handler(int index, String expression, String placeHolder) {
        if (Objects.equals(PREPAREPLACEHOLDER, placeHolder)) {
            return "?";
        } else {
            SQLParameter sqlParameter = Arrays.stream(sqlParameters).filter(item -> Objects.equals(item.getIndex(), index))
                    .findAny().orElse(null);
            if (sqlParameter == null) {
                sqlParameter = Arrays.stream(sqlParameters).filter(item -> Objects.equals(item.getName(), expression))
                        .findAny().orElse(null);
            }
            if (sqlParameter == null) {
                throw new DAOMehtodException("No such parameter " + expression);
            }
            return sqlParameter.getValue().toString();
        }
    }
}

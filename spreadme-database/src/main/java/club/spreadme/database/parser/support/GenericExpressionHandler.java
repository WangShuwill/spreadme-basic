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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class GenericExpressionHandler implements ExpressionHandler {

    private final SQLParameter[] sqlParameters;

    public GenericExpressionHandler(SQLParameter[] sqlParameters) {
        this.sqlParameters = sqlParameters;
    }

    @Override
    public String handler(String expression, String placeHolder) {
        if (Objects.equals(PREPAREPLACEHOLDER, placeHolder)) {
            return "?";
        } else {
            List<SQLParameter> sqlParameters = Arrays.stream(this.sqlParameters)
                    .filter(item -> (item !=null && Objects.equals(item.getName(), expression)))
                    .collect(Collectors.toList());
            if (sqlParameters.size() > 1) {
                throw new DAOMehtodException("Too many parameters for " + expression);
            }
            if (sqlParameters.size() < 1) {
                throw new DAOMehtodException("No such parameters for " + expression);
            }
            SQLParameter sqlParameter = sqlParameters.get(0);
            this.sqlParameters[sqlParameter.getIndex()] = null;
            return sqlParameter.getValue().toString();
        }
    }
}

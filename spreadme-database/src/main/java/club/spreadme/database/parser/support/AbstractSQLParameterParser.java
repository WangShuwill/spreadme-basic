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

import club.spreadme.database.annotation.Param;
import club.spreadme.database.exception.DAOMehtodException;
import club.spreadme.database.parser.SQLParameterParser;
import club.spreadme.database.parser.grammar.SQLParameter;
import club.spreadme.lang.Reflection;
import club.spreadme.lang.StringUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

public abstract class AbstractSQLParameterParser implements SQLParameterParser {

    @Override
    public SQLParameter[] parse(Method method, Object[] values) {
        // get count of parameter and method's parameters
        int parameterCount = method.getParameterCount();
        Parameter[] parameters = method.getParameters();
        // create array of SQLParameter
        SQLParameter[] sqlParameters = new SQLParameter[parameterCount];
        if (parameterCount != values.length) {
            throw new DAOMehtodException("The parameter's count not equal to value's for " + method.getName());
        }
        for (int i = 0; i < values.length; i++) {
            if (!Objects.equals(parameters[i].getType(), values[i].getClass())) {
                throw new DAOMehtodException("The parameter's type not equal to value'type for " + method.getName());
            }
            SQLParameter sqlParameter = new SQLParameter();
            sqlParameter.setIndex(i);
            Param param = Reflection.getAnnotation(parameters[i], Param.class);
            // get parameter's name
            String paramName = "";
            if (param != null) {
                paramName = param.value();
            }
            if (StringUtil.isBlank(paramName)) {
                paramName = parameters[i].getName();
            }
            sqlParameter.setName(paramName);
            sqlParameter.setType(values[i].getClass());
            sqlParameter.setValue(values[i]);
            sqlParameters[i] = sqlParameter;
        }
        return sqlParameters;
    }
}

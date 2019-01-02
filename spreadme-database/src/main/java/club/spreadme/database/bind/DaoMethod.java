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

package club.spreadme.database.bind;

import club.spreadme.database.annotation.Transactional;
import club.spreadme.database.core.executor.Executor;
import club.spreadme.database.metadata.SQLOptionType;
import club.spreadme.lang.Reflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class DaoMethod extends AbstractDaoMethod {

    private static final Logger LOGGER = LoggerFactory.getLogger(DaoMethod.class);

    private Executor executor;
    private MethodSignature methodSignature;
    private SQLCommand sqlCommand;

    public DaoMethod() {
    }

    public <T> DaoMethod(final Class<T> daoInterface, final Method method, final Object[] values, final Executor executor) {
        this.executor = executor;
        this.methodSignature = getMethodSignature(daoInterface, method, values);
        this.sqlCommand = getSQLCommand(method);
    }

    public Object execute() {
        Object result = null;
        Transactional transactional = Reflection.getAnnotation(this.methodSignature.getMethod(), Transactional.class);
        if (transactional != null) {
            //TODO transaction
        } else {
            result = doExecute();
        }
        return result;
    }

    private Object doExecute() {
        if (SQLOptionType.QUERY.equals(this.sqlCommand.getSqlOptionType())) {
            return query(this.methodSignature, this.sqlCommand, this.executor);
        } else {
            return update(this.methodSignature, this.sqlCommand, this.executor);
        }
    }

}

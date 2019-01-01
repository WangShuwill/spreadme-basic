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

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractDaoMethod extends AbstractSQLOption implements DaoMethodRegiatrar {

    private Map<Object, DaoMethod> daoMethodMap = new ConcurrentHashMap<>(256);
    private Map<Object, MethodSignature> methodSignatureMap = new ConcurrentHashMap<>(256);
    private Map<Object, SQLCommand> sqlCommandMap = new ConcurrentHashMap<>(256);

    @Override
    public void register(Object key, DaoMethod daoMethod) {
        daoMethodMap.put(key, daoMethod);
    }

    @Override
    public void register(Method method, MethodSignature methodSignature) {
        methodSignatureMap.put(method, methodSignature);
    }

    @Override
    public void register(Method method, SQLCommand sqlCommand) {
        sqlCommandMap.put(method, sqlCommand);
    }

    @Override
    public DaoMethod getDaoMethod(Object key) {
        return daoMethodMap.get(key);
    }

    @Override
    public MethodSignature getMethodSignature(Class<?> daoInterface, Method method, Object[] values) {
        MethodSignature methodSignature = methodSignatureMap.get(method);
        if (methodSignature == null) {
            methodSignature = new MethodSignature(daoInterface, method, values);
            register(method, methodSignature);
        }
        return methodSignature;
    }

    @Override
    public SQLCommand getSQLCommand(Method method) {
        SQLCommand sqlCommand = sqlCommandMap.get(method);
        if (sqlCommand == null) {
            sqlCommand = new SQLCommand(method);
            register(method, sqlCommand);
        }
        return sqlCommand;
    }
}

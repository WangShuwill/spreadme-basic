/*
 *  Copyright (c) 2019 Wangshuwei
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

package club.spreadme.database.bind.support;

import club.spreadme.database.bind.DaoMethodRegiatrar;

import java.lang.reflect.Method;

public abstract class AbstractDaoMethodContext extends AbstractSQLOption implements DaoMethodRegiatrar {

    @Override
    public void register(Method method, MethodSignature methodSignature) {
        mscache.put(method, methodSignature);
    }

    @Override
    public void register(Method method, SQLCommand sqlCommand) {
        sdcache.put(method, sqlCommand);
    }

    @Override
    public MethodSignature getMethodSignature(Class<?> daoInterface, Method method, Object[] values) {
        MethodSignature methodSignature = mscache.get(method, MethodSignature.class);
        if (methodSignature == null) {
            methodSignature = new MethodSignature(daoInterface, method, values);
            register(method, methodSignature);
        }
        // reset method values
        methodSignature.setValues(values);
        return methodSignature;
    }

    @Override
    public SQLCommand getSQLCommand(Method method) {
        SQLCommand sqlCommand = sdcache.get(method, SQLCommand.class);
        if (sqlCommand == null) {
            sqlCommand = new SQLCommand(method);
            register(method, sqlCommand);
        }
        return sqlCommand;
    }
}

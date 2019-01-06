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

import club.spreadme.database.bind.DaoMethod;
import club.spreadme.database.core.executor.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DaoInvocationHandler<T> implements InvocationHandler, Serializable {

    private static final long serialVersionUID = 8096301565159000631L;
    private static Logger LOGGER = LoggerFactory.getLogger(DaoInvocationHandler.class);

    private final Class<T> daoInterface;
    private final Executor executor;

    public DaoInvocationHandler(Class<T> daoInterface, Executor executor) {
        this.daoInterface = daoInterface;
        this.executor = executor;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        DaoMethod daoMethod = new DaoMethod(daoInterface, method, args, executor);
        return daoMethod.invoke();
    }
}

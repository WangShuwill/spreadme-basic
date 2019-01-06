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

package club.spreadme.database.bind;

import club.spreadme.database.core.executor.Executor;

import java.lang.reflect.Proxy;

/**
 * @author Wangshuwei
 * @since 2018-8-7
 */
public class DaoProxyFactory<T> {

    private final Class<T> daoInterfance;

    public DaoProxyFactory(Class<T> daoInterfance) {
        this.daoInterfance = daoInterfance;
    }

    @SuppressWarnings("unchecked")
    protected T newInstance(DaoInvocationHandler<T> invocationHandler) {
        return (T) Proxy.newProxyInstance(daoInterfance.getClassLoader(), new Class[]{daoInterfance}, invocationHandler);
    }

    public T newInstance(Executor executor) {
        final DaoInvocationHandler<T> iDaoProxy = new DaoInvocationHandler<>(daoInterfance, executor);
        return newInstance(iDaoProxy);
    }
}

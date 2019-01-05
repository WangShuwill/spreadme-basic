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

package club.spreadme.database.core.transaction;

import club.spreadme.database.annotation.Transactional;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Wangshuwei
 * @since 2018-6-27
 */
public class TransactionInvocationHandler implements InvocationHandler {

    private Object proxy;
    private TransactionExecutor transactionExecutor;

    public TransactionInvocationHandler(Object object, DataSource dataSource, Transaction transaction) {
        this.proxy = object;
        this.transactionExecutor = new TransactionExecutor(dataSource);
        transactionExecutor.setTransaction(transaction);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {

        Transactional transactional = method.getAnnotation(Transactional.class);
        if (transactional != null) {
            return method.invoke(proxy, args);
        }

        return transactionExecutor.execute(() -> {
            try {
                return method.invoke(proxy, args);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}

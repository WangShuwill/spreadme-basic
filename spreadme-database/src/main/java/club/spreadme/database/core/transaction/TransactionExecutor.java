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

import javax.sql.DataSource;

/**
 * execute transaction
 *
 * @author Wangshuwei
 * @since 2018-6-23
 */
public class TransactionExecutor {

    private TransactionManager transactionManager;
    private Transaction transaction;

    public TransactionExecutor() {

    }

    public TransactionExecutor(DataSource dataSource) {
        transactionManager = new DefaultTransactionManager(dataSource);
    }

    public <T> T execute(TransactionCallback<T> transactionCallback) {
        if (transaction == null) {
            transaction = new Transaction();
        }
        TransactionStatus transactionStatus = transactionManager.getTransaction(transaction);
        T result;
        try {
            result = transactionCallback.execute();
        } catch (Throwable ex) {
            transactionManager.rollback(transactionStatus);
            throw ex;
        }
        transactionManager.commit(transactionStatus);
        return result;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public TransactionExecutor setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        return this;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public TransactionExecutor setTransaction(Transaction transaction) {
        this.transaction = transaction;
        return this;
    }
}

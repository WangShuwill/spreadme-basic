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

import club.spreadme.database.core.resource.ConnectionHolder;
import club.spreadme.database.exception.DataBaseAccessException;
import club.spreadme.database.metadata.TransactionIsolationLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Default transaction manager implement transactionmanager
 *
 * @author Wangshuwei
 * @see club.spreadme.database.core.transaction.TransactionManager
 * @since 2018-6-23
 */
public class DefaultTransactionManager implements TransactionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultTransactionManager.class);

    private DataSource dataSource;

    public DefaultTransactionManager() {

    }

    public DefaultTransactionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public TransactionStatus getTransaction(Transaction transaction) {
        ConnectionHolder connectionHolder = TransactionSynchronizationManager.getConnectionHolder();

        if (transaction == null) {
            transaction = new Transaction();
        }

        if (connectionHolder != null && connectionHolder.isTransactionActive()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("The transaction already exist,join local transaction");
            }
            return new TransactionStatus(connectionHolder, false, transaction.isReadOnly());
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("The transaction dose not exsit,create new transction");
        }
        TransactionStatus transactionStatus = new TransactionStatus(connectionHolder, true, transaction.isReadOnly());
        begin(connectionHolder, transaction);
        return transactionStatus;
    }

    @Override
    public void commit(TransactionStatus transactionStatus) {
        if (transactionStatus.isCompleted()) {
            throw new DataBaseAccessException("Transaction is already completed,do not call commit or rollback");
        }
        try {
            doCommit(transactionStatus);
        } catch (SQLException ex) {
            throw new DataBaseAccessException(ex.getMessage());
        }
    }

    @Override
    public void rollback(TransactionStatus transactionStatus) {
        if (transactionStatus.isCompleted()) {
            throw new DataBaseAccessException("Transaction is already completed,do not call commit or rollback");
        }

        try {
            doRollBack(transactionStatus);
        } catch (SQLException ex) {
            throw new DataBaseAccessException(ex.getMessage());
        }
    }

    /**
     * init connection by transaction definition where begin a transaction
     *
     * @param connectionHolder connection hodler
     * @param transaction      transaction definition
     */
    protected void begin(ConnectionHolder connectionHolder, Transaction transaction) {
        try {
            connectionHolder.setTransactionActive(true);
            Connection connection = connectionHolder.getConnection(dataSource);

            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }

            if (transaction.isReadOnly()) {
                connection.setReadOnly(transaction.isReadOnly());
            }

            // set transaction isolation level
            TransactionIsolationLevel isolationLevel = transaction.getIsolationLevel();
            if (!TransactionIsolationLevel.DEFAULT.equals(isolationLevel)) {
                connection.setTransactionIsolation(isolationLevel.getLevel());
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Transaction Begin,Connection Hashcode is {}", connection.hashCode());
            }

        } catch (SQLException ex) {
            throw new DataBaseAccessException(ex.getMessage());
        }
    }

    /**
     * close transaction and clear resources where transaction completed
     *
     * @param transactionStatus transaction status
     */
    protected void close(TransactionStatus transactionStatus) {
        transactionStatus.setCompleted(true);
        if (transactionStatus.isNewTransaction()) {
            ConnectionHolder connectionHolder = transactionStatus.getConnectionHolder();
            try {
                Connection connection = connectionHolder.getConnection(dataSource);
                connection.setAutoCommit(true);
                connection.close();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Transaction close,Connection Hashcode is {}", connection.hashCode());
                }
            } catch (SQLException ex) {
                throw new DataBaseAccessException(ex.getMessage());
            } finally {
                connectionHolder.setTransactionActive(false);
                connectionHolder.removeConnection(dataSource);
                TransactionSynchronizationManager.clear();
            }

        }
    }

    protected void doCommit(TransactionStatus transactionStatus) throws SQLException {
        try {
            ConnectionHolder connectionHolder = transactionStatus.getConnectionHolder();
            if (transactionStatus.isNewTransaction()) {
                Connection connection = connectionHolder.getConnection(dataSource);
                connection.commit();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Transaction commit,Connection Hashcode is {}", connection.hashCode());
                }
            } else {
                connectionHolder.setCommitOnly(true);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Transaction commit status set {},the isNewTransaction status {}",
                            connectionHolder.isCommitOnly(), transactionStatus.isNewTransaction()
                    );
                }
            }

        } finally {
            close(transactionStatus);
        }
    }

    protected void doRollBack(TransactionStatus transactionStatus) throws SQLException {
        try {
            ConnectionHolder connectionHolder = transactionStatus.getConnectionHolder();
            if (transactionStatus.isNewTransaction()) {
                Connection connection = connectionHolder.getConnection(dataSource);
                connection.rollback();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Transaction rollback,Connection Hashcode is {}", connection.hashCode());
                }
            } else {
                connectionHolder.setRollbackOnly(true);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Transaction rollback status set {},the isNewTransaction status {}",
                            connectionHolder.isCommitOnly(), transactionStatus.isNewTransaction()
                    );
                }
            }

        } finally {
            close(transactionStatus);
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}

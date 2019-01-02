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

public class TransactionStatus {

    private final ConnectionHolder connectionHolder;
    private final boolean isNewTransaction;
    private final boolean readOnly;
    private boolean isCompleted;

    public TransactionStatus(ConnectionHolder connectionHolder, boolean isNewTransaction, boolean readOnly) {
        this.connectionHolder = connectionHolder;
        this.isNewTransaction = isNewTransaction;
        this.readOnly = readOnly;
    }

    public ConnectionHolder getConnectionHolder() {
        return connectionHolder;
    }

    public boolean isNewTransaction() {
        return isNewTransaction;
    }

    public boolean isReadOnly() {
        return readOnly;
    }


    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}

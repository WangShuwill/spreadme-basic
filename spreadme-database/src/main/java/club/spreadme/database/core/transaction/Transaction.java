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

import club.spreadme.database.metadata.TransactionIsolationLevel;

public class Transaction {

    private TransactionIsolationLevel isolationLevel = TransactionIsolationLevel.DEFAULT;
    private boolean readOnly = false;
    private int timeout = -1;

    public Transaction() {

    }

    public Transaction(TransactionIsolationLevel isolationLevel, boolean readOnly, int timeout) {
        this.isolationLevel = isolationLevel;
        this.readOnly = readOnly;
        this.timeout = timeout;
    }

    public TransactionIsolationLevel getIsolationLevel() {
        return isolationLevel;
    }

    public Transaction setIsolationLevel(TransactionIsolationLevel isolationLevel) {
        this.isolationLevel = isolationLevel;
        return this;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public Transaction setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public Transaction setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }
}

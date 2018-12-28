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

package club.spreadme.database.core.statement.support;

import club.spreadme.database.core.statement.StatementCallback;
import club.spreadme.database.core.statement.WrappedStatement;
import club.spreadme.database.metadata.ConcurMode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class AsyncStatementCallBack<T> implements StatementCallback<Future<T>> {

    private final ExecutorService executorService;
    private final StatementCallback<T> callback;

    public AsyncStatementCallBack(ExecutorService executorService, StatementCallback<T> callback) {
        this.executorService = executorService;
        this.callback = callback;
    }

    @Override
    public Future<T> executeStatement(WrappedStatement wrappedStatement) {
        return executorService.submit(() -> callback.executeStatement(wrappedStatement));
    }

    @Override
    public ConcurMode getCouncurMode() {
        return callback.getCouncurMode();
    }
}

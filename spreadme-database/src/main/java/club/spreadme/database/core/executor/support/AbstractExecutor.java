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

package club.spreadme.database.core.executor.support;

import club.spreadme.database.core.aware.ExecutorAware;
import club.spreadme.database.core.executor.Executor;
import club.spreadme.database.core.statement.StatementBuilder;
import club.spreadme.database.core.statement.StatementCallback;
import club.spreadme.database.core.grammar.StatementConfig;

public abstract class AbstractExecutor implements Executor, ExecutorAware {

    private StatementConfig statementConfig;

    @Override
    public void setStatementConfig(StatementConfig config) {
        this.statementConfig = config;
    }

    @Override
    public <T> T execute(StatementBuilder builder, StatementCallback<T> action) {
        return doExecute(builder, action, statementConfig);
    }

    protected abstract <T> T doExecute(StatementBuilder builder, StatementCallback<T> action, StatementConfig config);
}

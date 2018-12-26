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

package club.spreadme.database.core.resultset.support;

import club.spreadme.database.core.statement.StatementBuilder;
import club.spreadme.database.core.statement.StatementCallback;
import club.spreadme.database.util.JdbcUtil;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class ResultSpliterator<T> implements Spliterator<T> {

    private Connection connection;
    private DataSource dataSource;
    private StatementBuilder builder;
    private StatementCallback<T> action;

    public ResultSpliterator(Connection connection, StatementBuilder builder, StatementCallback<T> action) {
        this.connection = connection;
        this.builder = builder;
        this.action = action;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        JdbcUtil.closeConnection(connection, dataSource);
        return false;
    }

    @Override
    public Spliterator<T> trySplit() {

        return null;
    }

    @Override
    public long estimateSize() {
        return 0;
    }

    @Override
    public int characteristics() {
        return 0;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {

    }

    @Override
    public long getExactSizeIfKnown() {
        return 0;
    }

    @Override
    public boolean hasCharacteristics(int characteristics) {
        return false;
    }

    @Override
    public Comparator<? super T> getComparator() {
        return null;
    }
}

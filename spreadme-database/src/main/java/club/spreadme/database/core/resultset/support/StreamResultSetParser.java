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

import club.spreadme.database.core.datasource.CloseHandler;
import club.spreadme.database.core.resultset.ResultSetParser;
import club.spreadme.database.core.resultset.RowMapper;
import club.spreadme.database.exception.DataBaseAccessException;

import java.sql.ResultSet;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamResultSetParser<T> implements Spliterator<T>, ResultSetParser<Stream<T>> {

    private CloseHandler closeHandler;
    private RowMapper<T> rowMapper;

    private ResultSet resultSet;

    public StreamResultSetParser(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    @Override
    public Stream<T> parse(ResultSet resultSet) {
        this.resultSet = resultSet;
        return StreamSupport.stream(this, true).onClose(closeHandle());
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        try {
            if (!resultSet.next()) {
                closeHandler.close();
                return false;
            }
            action.accept(rowMapper.mapping(resultSet));
            return true;
        } catch (Exception ex) {
            throw new DataBaseAccessException(ex.getMessage());
        }
    }

    @Override
    public Spliterator<T> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return Long.MAX_VALUE;
    }

    @Override
    public int characteristics() {
        return CONCURRENT;
    }

    private Runnable closeHandle() {
        return () -> closeHandler.close();
    }

    public void setCloseHandler(CloseHandler closeHandler) {
        this.closeHandler = closeHandler;
    }
}

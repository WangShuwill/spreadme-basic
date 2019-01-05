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

import club.spreadme.database.core.resultset.support.StreamResultSetParser;
import club.spreadme.database.core.statement.StatementCallback;
import club.spreadme.database.core.statement.WrappedStatement;
import club.spreadme.database.metadata.ConcurMode;
import club.spreadme.database.core.resource.ResourceHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.stream.Stream;

public class StreamQueryStatementCallback<T> implements StatementCallback<Stream<T>> {

    private final StreamResultSetParser<T> resultSetParser;

    private DataSource dataSource;
    private Connection connection;

    private ConcurMode concurMode;

    public StreamQueryStatementCallback(StreamResultSetParser<T> resultSetParser) {
        this.resultSetParser = resultSetParser;
    }

    @Override
    public Stream<T> executeStatement(WrappedStatement wrappedStatement) throws Exception {
        concurMode = wrappedStatement.getConcurMode();
        ResultSet resultSet = wrappedStatement.query();
        resultSetParser.setCloseHandler(() -> {
            ResourceHandler.closeResultSet(resultSet);
            ResourceHandler.closeWrappedStatement(wrappedStatement);
            ResourceHandler.closeConnection(connection, dataSource);
        });
        return resultSetParser.parse(resultSet);
    }

    @Override
    public ConcurMode getCouncurMode() {
        return concurMode;
    }

    public void nest(DataSource dataSource, Connection connection) {
        this.dataSource = dataSource;
        this.connection = connection;
    }
}

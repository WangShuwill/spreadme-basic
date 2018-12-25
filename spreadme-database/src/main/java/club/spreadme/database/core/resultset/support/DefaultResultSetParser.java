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

import club.spreadme.database.core.resultset.ResultSetParser;
import club.spreadme.database.core.resultset.RowMapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DefaultResultSetParser<T> implements ResultSetParser<List<T>> {

    private final int rowsExpected;
    private final RowMapper<T> rowMapper;

    public DefaultResultSetParser(RowMapper<T> rowMapper) {
        this(rowMapper, 0);
    }

    public DefaultResultSetParser(RowMapper<T> rowMapper, int rowsExpected) {
        this.rowMapper = rowMapper;
        this.rowsExpected = rowsExpected;
    }

    @Override
    public List<T> parse(ResultSet rs) throws Exception {
        List<T> results = rowsExpected > 0 ? new ArrayList<>(rowsExpected) : new ArrayList<>();
        while (rs.next()) {
            results.add(rowMapper.mapping(rs));
        }
        return results;
    }

}

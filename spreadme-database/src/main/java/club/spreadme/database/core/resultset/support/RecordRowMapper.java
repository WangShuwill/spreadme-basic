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

import club.spreadme.database.core.grammar.Record;
import club.spreadme.database.core.resultset.RowMapper;
import club.spreadme.database.util.JdbcUtil;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * The class map resultset to record
 *
 * @author Wangshuwei
 * @since 2018-6-23
 */
public class RecordRowMapper implements RowMapper<Record> {

    @Override
    public Record mapping(ResultSet rs) throws Exception {
        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();
        Record record = new Record(columnCount);
        record.setTableName(resultSetMetaData.getTableName(1));
        for (int i = 1; i <= columnCount; i++) {
            String key = JdbcUtil.getColumnName(resultSetMetaData, i);
            Object value = JdbcUtil.getResultSetValue(rs, i);
            record.put(key.toLowerCase(), value);
        }

        return record;
    }
}

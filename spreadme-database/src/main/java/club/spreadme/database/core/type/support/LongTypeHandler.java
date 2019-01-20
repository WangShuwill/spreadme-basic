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

package club.spreadme.database.core.type.support;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LongTypeHandler extends AbstractTypeHandler<Long> {

    @Override
    protected void setNonNullParameter(PreparedStatement ps, int index, Long parameter, JDBCType jdbcType) throws SQLException {
        ps.setLong(index, parameter);
    }

    @Override
    protected Long getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        long result = resultSet.getLong(columnName);
        return resultSet.wasNull() ? null : result;
    }

    @Override
    protected Long getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        long result = resultSet.getLong(columnIndex);
        return resultSet.wasNull() ? null : result;
    }

}

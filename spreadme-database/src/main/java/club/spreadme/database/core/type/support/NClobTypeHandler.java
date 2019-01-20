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

import java.io.StringReader;
import java.sql.*;

public class NClobTypeHandler extends AbstractTypeHandler<String> {

    @Override
    protected void setNonNullParameter(PreparedStatement ps, int index, String parameter, JDBCType jdbcType) throws SQLException {
        StringReader reader = new StringReader(parameter);
        ps.setCharacterStream(index, reader, parameter.length());
    }

    @Override
    protected String getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        Clob clob = resultSet.getClob(columnName);
        return toString(clob);
    }

    @Override
    protected String getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        Clob clob = resultSet.getClob(columnIndex);
        return toString(clob);
    }

    private String toString(Clob clob) throws SQLException {
        return clob == null ? null : clob.getSubString(1, (int) clob.length());
    }
}

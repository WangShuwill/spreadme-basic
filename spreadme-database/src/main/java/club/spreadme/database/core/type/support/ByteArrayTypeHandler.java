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

public class ByteArrayTypeHandler extends AbstractTypeHandler<byte[]> {

    @Override
    protected void setNonNullParameter(PreparedStatement ps, int index, byte[] parameter, JDBCType jdbcType) throws SQLException {
        ps.setBytes(index, parameter);
    }

    @Override
    protected byte[] getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return resultSet.getBytes(columnName);
    }

    @Override
    protected byte[] getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return resultSet.getBytes(columnIndex);
    }

}

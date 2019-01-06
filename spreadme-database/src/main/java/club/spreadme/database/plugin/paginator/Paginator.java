/*
 *  Copyright (c) 2019 Wangshuwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package club.spreadme.database.plugin.paginator;

import club.spreadme.database.core.grammar.StatementConfig;
import club.spreadme.database.core.statement.StatementBuilder;
import club.spreadme.database.exception.PluginException;
import club.spreadme.database.plugin.Interceptor;
import club.spreadme.database.plugin.Invocation;
import club.spreadme.database.plugin.Plugin;
import club.spreadme.database.plugin.annotation.Intercepts;
import club.spreadme.database.plugin.annotation.Signature;
import club.spreadme.database.plugin.paginator.dialect.PaginationDialect;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Wangshuwei
 * @since 2018-8-13
 */
@Intercepts({@Signature(type = StatementBuilder.class, method = "build", args = {StatementConfig.class})})
public class Paginator implements Interceptor {

    private static final String SQLFIELD = "sql";
    private static final String OBJECTSFIELD = "objects";

    private List<Class<? extends PaginationDialect>> paginationDialects = new ArrayList<>();

    public Paginator() {

    }

    public Paginator(Class<? extends PaginationDialect>[] paginationDialects) {
        addAllDialect(paginationDialects);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementBuilder statementBuilder = (StatementBuilder) invocation.getTarget();
        DatabaseMetaData metaData = statementBuilder.getDatabaseMetaData();
        String dataBaseProductName = metaData.getDatabaseProductName();

        String sql = statementBuilder.getSql();
        Object[] originValues = statementBuilder.getValues();
        List<Object> values = new ArrayList<>();
        Page<?> page = null;
        for (Object value : originValues) {
            if (value instanceof Page) {
                page = (Page<?>) value;
            }
            else {
                values.add(value);
            }
        }

        if (page != null) {
            int pagesize = page.getPageSize(), pagenum = page.getPageNum();
            int limit = pagesize * (pagenum - 1);
            if (paginationDialects.size() < 1) {
                throw new PluginException("No pagination dialog");
            }
            sql = generatePageSql(sql, dataBaseProductName, limit, pagesize);
            statementBuilder.setSql(sql);
            if (values.size() > 0) {
                statementBuilder.setValues(values.toArray());
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public boolean removeDialect(Class<? extends PaginationDialect> paginationDialect) {
        return this.paginationDialects.remove(paginationDialect);
    }

    public void clearDialect() {
        this.paginationDialects.clear();
    }

    public Paginator addDialect(Class<? extends PaginationDialect> paginationDialect) {
        this.paginationDialects.add(paginationDialect);
        return this;
    }

    public Paginator addAllDialect(Class<? extends PaginationDialect>[] paginationDialects) {
        for (Class<? extends PaginationDialect> paginationDialect : paginationDialects) {
            addDialect(paginationDialect);
        }
        return this;
    }

    protected String generatePageSql(String sql, String databaseName, int offset, int limit) throws Exception {
        for (Class<? extends PaginationDialect> clazz : paginationDialects) {
            PaginationDialect paginationDialect = clazz.newInstance();
            if (paginationDialect.isTargetDatabase(databaseName)) {
                return paginationDialect.getPagenation(sql, offset, limit);
            }
        }

        return sql;
    }
}

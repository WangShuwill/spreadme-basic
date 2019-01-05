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

package club.spreadme.database.bind;

import club.spreadme.database.annotation.PostProcessor;
import club.spreadme.database.core.executor.Executor;
import club.spreadme.database.core.grammar.Record;
import club.spreadme.database.core.resultset.RowMapper;
import club.spreadme.database.core.resultset.support.BeanRowMapper;
import club.spreadme.database.core.resultset.support.DefaultResultSetParser;
import club.spreadme.database.core.resultset.support.RecordRowMapper;
import club.spreadme.database.core.statement.StatementBuilder;
import club.spreadme.database.core.statement.support.PrepareStatementBuilder;
import club.spreadme.database.core.statement.support.QueryStatementCallback;
import club.spreadme.database.core.statement.support.UpdateStatementCallback;
import club.spreadme.database.exception.DAOMehtodException;
import club.spreadme.database.metadata.ConcurMode;
import club.spreadme.database.parser.grammar.SQLParameter;
import club.spreadme.database.parser.grammar.SQLStatement;
import club.spreadme.database.parser.support.AbstractSQLParameterParser;
import club.spreadme.database.parser.support.BeanSQLParser;
import club.spreadme.database.parser.support.RoutingSQLParser;
import club.spreadme.database.parser.support.SimpleSQLParser;
import club.spreadme.database.plugin.InterceptorChain;
import club.spreadme.database.plugin.PluginHandler;
import club.spreadme.lang.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AbstractSQLOption extends AbstractSQLParameterParser implements SQLOption, PluginHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSQLOption.class);

    @Override
    @SuppressWarnings("unchecked")
    public Object query(MethodSignature methodSignature, SQLCommand sqlCommand, Executor executor) {

        SQLParameter[] sqlParameters = parse(methodSignature.getMethod(), methodSignature.getValues());
        SQLStatement sqlStatement = new RoutingSQLParser(new SimpleSQLParser(sqlCommand.getSql(), sqlParameters)).parse();

        // post process sqlstatement
        Class<? extends PostProcessor> processorClass = sqlCommand.getPostProcessor();
        sqlStatement = postProcessSQLStatement(sqlStatement, processorClass);

        String sql = sqlStatement.getSql();
        if (StringUtil.isBlank(sql)) {
            throw new DAOMehtodException("There no sql statement for the method " + methodSignature.getMethodName());
        }
        Object[] values = sqlStatement.getValues();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("parse sql {}, values {}", sql, Arrays.toString(values));
        }

        StatementBuilder statementBuilder = getStatementBuilder(sql, values, ConcurMode.READ_ONLY);
        if (methodSignature.isReturnsMany()) {
            Type type = methodSignature.getActualTypes()[0];
            return query(statementBuilder, new BeanRowMapper((Class<?>) type), 0, executor);

        }
        else if (methodSignature.isReturnsMap()) {
            List<Record> results = query(statementBuilder, new RecordRowMapper(), 1, executor);
            return results.iterator().hasNext() ? results.iterator().next() : null;
        }
        else {
            List<?> results = query(statementBuilder, new BeanRowMapper<>(methodSignature.getReturnType()), 1, executor);
            return results.iterator().hasNext() ? results.iterator().next() : null;
        }
    }

    @Override
    public Object update(MethodSignature methodSignature, SQLCommand sqlCommand, Executor executor) {

        SQLParameter[] sqlParameters = parse(methodSignature.getMethod(), methodSignature.getValues());
        SQLStatement preSqlStatement = new RoutingSQLParser(new SimpleSQLParser(sqlCommand.getSql(), sqlParameters)).parse();

        if (!methodSignature.isAllPrimaryParamter()) {
            if (methodSignature.getMethod().getParameterCount() > 1) {
                throw new DAOMehtodException("Too many parameters for the dao method " + methodSignature.getMethodName());
            }
            if (methodSignature.getMethod().getParameterCount() < 1) {
                throw new DAOMehtodException("Too litter parameters for the dao method " + methodSignature.getMethodName());
            }
            SQLStatement sqlStatement = new RoutingSQLParser(new BeanSQLParser(preSqlStatement.getValues()[0],
                    sqlCommand.getSqlOptionType())).parse();

            // post process sqlstatement
            Class<? extends PostProcessor> processorClass = sqlCommand.getPostProcessor();
            sqlStatement = postProcessSQLStatement(sqlStatement, processorClass);

            if (StringUtil.isBlank(sqlStatement.getSql())) {
                throw new DAOMehtodException("There no sql statement for the method " + methodSignature.getMethodName());
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("parse sql {}, values {}", sqlStatement.getSql(), Arrays.toString(sqlStatement.getValues()));
            }
            StatementBuilder statementBuilder = getStatementBuilder(sqlStatement.getSql(), sqlStatement.getValues(), ConcurMode.UPDATABLE);
            return update(statementBuilder, executor);
        }
        else {
            // post process sqlstatement
            Class<? extends PostProcessor> processorClass = sqlCommand.getPostProcessor();
            preSqlStatement = postProcessSQLStatement(preSqlStatement, processorClass);

            if (StringUtil.isBlank(preSqlStatement.getSql())) {
                throw new DAOMehtodException("There no sql statement for the method " + methodSignature.getMethodName());
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("parse sql {}, values {}", preSqlStatement.getSql(), Arrays.toString(preSqlStatement.getValues()));
            }
            StatementBuilder statementBuilder = getStatementBuilder(preSqlStatement.getSql(), preSqlStatement.getValues(), ConcurMode.UPDATABLE);
            return update(statementBuilder, executor);
        }

    }

    @Override
    public InterceptorChain getInterceptorChain() {
        return PluginHandler.INTERCEPTOR_CHAIN;
    }

    protected SQLStatement postProcessSQLStatement(SQLStatement sqlStatement, Class<? extends PostProcessor> processorClass) {
        if (processorClass != null && !processorClass.isInterface() && processorClass.getModifiers() != Modifier.ABSTRACT) {
            try {
                PostProcessor processor = processorClass.newInstance();
                return Optional.ofNullable(processor.process(sqlStatement)).orElse(sqlStatement);
            }
            catch (Exception ex) {
                throw new DAOMehtodException("can not instance sqlprocessor," + ex.getMessage());
            }
        }

        return sqlStatement;
    }

    protected StatementBuilder getStatementBuilder(String sql, Object[] values, ConcurMode concurMode) {
        StatementBuilder builder = new PrepareStatementBuilder(sql, values, concurMode);
        return (StatementBuilder) getInterceptorChain().pluginAll(builder);
    }

    protected <T> List<T> query(StatementBuilder builder, final RowMapper<T> rowMapper, int rowsExpected, Executor executor) {
        return executor.execute(builder, new QueryStatementCallback<>(new DefaultResultSetParser<>(rowMapper, rowsExpected)));
    }

    protected Integer update(StatementBuilder builder, Executor executor) {
        return executor.execute(builder, new UpdateStatementCallback());
    }
}

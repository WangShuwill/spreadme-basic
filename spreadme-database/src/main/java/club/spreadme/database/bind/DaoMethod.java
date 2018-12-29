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

package club.spreadme.database.bind;

import club.spreadme.database.annotation.PostProcessor;
import club.spreadme.database.core.executor.Executor;
import club.spreadme.database.core.grammar.Record;
import club.spreadme.database.core.resultset.support.BeanRowMapper;
import club.spreadme.database.core.resultset.support.RecordRowMapper;
import club.spreadme.database.dao.CommonDao;
import club.spreadme.database.exception.DAOMehtodException;
import club.spreadme.database.parser.grammar.SQLStatement;
import club.spreadme.database.parser.support.RoutingSQLParser;
import club.spreadme.database.parser.support.SimpleSQLParser;
import club.spreadme.lang.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

public class DaoMethod extends AbstractDaoMethod {

    private static final Logger LOGGER = LoggerFactory.getLogger(DaoMethod.class);

    private Executor executor;
    private MethodSignature methodSignature;
    private SQLCommand sqlCommand;

    public DaoMethod() {
    }

    public <T> DaoMethod(final Class<T> daoInterface, final Method method, final Object[] values, final Executor executor) {
        this.executor = executor;
        this.methodSignature = getMethodSignature(daoInterface, method, values);
        this.sqlCommand = getSQLCommand(method);
    }

    public Object execute() {
        return null;
    }

    static class SQLOption {

        public Object query(MethodSignature methodSignature, SQLCommand sqlCommand, Executor executor) {
            String sql = sqlCommand.getSql();
            Object[] values = methodSignature.getValues();

            Class<? extends PostProcessor> processorClass = sqlCommand.getPostProcessor();
            sql = processSql(sql, methodSignature, processorClass);

            if (StringUtil.isBlank(sql)) {
                throw new DAOMehtodException("There no sql statement for the method " + methodSignature.getMethodName());
            }

            SQLStatement sqlStatement = new RoutingSQLParser(new SimpleSQLParser(sql, values)).parse();
            sql = sqlStatement.getSql();
            values = sqlStatement.getValues();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("parse sql {}", sql);
            }

            if (methodSignature.isReturnsMany()) {

                Type type = methodSignature.getActualTypes()[0];
                if (Record.class.equals(type)) {
                    return CommonDao.getInstance(executor).query(sql, new RecordRowMapper(), values);
                } else {
                    return CommonDao.getInstance(executor).query(sql, new BeanRowMapper<>((Class<?>) type), values);
                }

            } else if (methodSignature.isReturnsMap()) {

                return CommonDao.getInstance(executor).queryOne(sql, values);

            } else {

                return CommonDao.getInstance(executor).query(sql, new BeanRowMapper<>(methodSignature.getReturnType()), values);

            }
        }

        protected static String processSql(String sql, MethodSignature methodSignature, Class<? extends PostProcessor> processorClass) {
            if (processorClass != null && !processorClass.isInterface() && processorClass.getModifiers() != Modifier.ABSTRACT) {
                Object values = methodSignature.getValues();
                String daoMethodName = methodSignature.getMethodName();
                try {
                    PostProcessor processor = processorClass.newInstance();
                    processor.setSql(sql);
                    processor.setParameters(values);
                    processor.setDaoMethodName(daoMethodName);
                    return StringUtil.isBlank(sql) ? sql : processor.process();
                } catch (Exception ex) {
                    throw new DAOMehtodException("can not instance sqlprocessor," + ex.getMessage());
                }
            }

            return sql;
        }
    }
}

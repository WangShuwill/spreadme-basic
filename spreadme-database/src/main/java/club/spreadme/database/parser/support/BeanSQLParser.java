/*
 *  Copyright (c) 2018 Wangshuwei
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

package club.spreadme.database.parser.support;

import club.spreadme.database.exception.DataBaseAccessException;
import club.spreadme.database.metadata.SQLOptionType;
import club.spreadme.database.parser.grammar.SQLStatement;

public class BeanSQLParser extends AbstractSQLBeanParser {

    private BeanSQLParser delegate;

    protected BeanSQLParser() {

    }

    public BeanSQLParser(Object bean, SQLOptionType optionType) {
        switch (optionType) {
            case INSERT:
                delegate = new InsertBeanSQLParser(bean, optionType);
                break;
            case UPDATE:
                delegate = new UpdateBeanSQLParser(bean, optionType);
                break;
            case DELETE:
                delegate = new DeleteBeanSQLParser(bean, optionType);
                break;
            default:
                throw new DataBaseAccessException("no sql build type match");
        }
    }

    @Override
    public SQLStatement parse() {
        return delegate.parse();
    }

}

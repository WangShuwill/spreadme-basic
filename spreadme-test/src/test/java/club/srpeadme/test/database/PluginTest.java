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

package club.srpeadme.test.database;

import club.spreadme.database.core.grammar.StatementConfig;
import club.spreadme.database.core.statement.StatementBuilder;
import club.spreadme.database.core.statement.support.PrepareStatementBuilder;
import club.spreadme.database.metadata.ConcurMode;
import club.spreadme.database.plugin.InterceptorChain;
import club.spreadme.database.plugin.Plugin;
import club.spreadme.database.plugin.paginator.Page;
import club.spreadme.database.plugin.paginator.Paginator;
import club.srpeadme.test.domain.Movie;
import org.junit.Test;

import java.sql.SQLException;

public class PluginTest {

    @Test
    public void testPlugin() throws SQLException {
        StatementBuilder statementBuilder = new PrepareStatementBuilder("select * from movies where name = ?",
                new Object[]{"test", new Page<Movie>().setPageNum(1).setPageSize(5)},
                ConcurMode.READ_ONLY);

        InterceptorChain chain = new InterceptorChain();
        chain.addInterceptor(new Paginator());
        statementBuilder = (StatementBuilder) chain.pluginAll(statementBuilder);

        statementBuilder = (StatementBuilder) Plugin.getTarget(statementBuilder);

        statementBuilder.build(new StatementConfig());
    }

}

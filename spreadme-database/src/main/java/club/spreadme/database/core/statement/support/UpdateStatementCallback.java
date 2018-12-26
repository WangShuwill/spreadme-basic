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

package club.spreadme.database.core.statement.support;

import club.spreadme.database.core.statement.StatementCallback;
import club.spreadme.database.core.statement.WrappedStatement;
import club.spreadme.database.metadata.ConcurMode;

public class UpdateStatementCallback implements StatementCallback<Integer> {

    private ConcurMode concurMode;

    @Override
    public Integer executeStatement(WrappedStatement wrappedStatement) throws Exception {
        concurMode = wrappedStatement.getConcurMode();
        return wrappedStatement.update();
    }

    @Override
    public ConcurMode getCouncurMode() {
        return concurMode;
    }
}

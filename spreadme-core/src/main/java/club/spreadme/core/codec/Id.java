/*
 * Copyright (c) 2019 Wangshuwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package club.spreadme.core.codec;

import java.io.Serializable;

import club.spreadme.core.codec.support.Snowflake;
import club.spreadme.core.codec.support.UID;
import club.spreadme.core.codec.support.UUID;

public interface Id<S extends Serializable> {

    Id<Long> SNOWFLAKE = new Snowflake();
    Id<Long> UID = new UID(1);
    Id<String> UUID = new UUID();

    S generate();
}

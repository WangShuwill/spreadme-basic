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

package club.spreadme.database.exception;

/**
 * @author Wangshuwei
 * @since 2018-8-6
 */
public class DAOMehtodException extends RuntimeException {

    private static final long serialVersionUID = -5001356535534921343L;

    public DAOMehtodException(String reason) {
        super(reason);
    }

    public DAOMehtodException(String reason, Throwable ex) {
        super(reason, ex);
    }

}

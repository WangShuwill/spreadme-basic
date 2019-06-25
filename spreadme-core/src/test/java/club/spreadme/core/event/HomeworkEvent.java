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

package club.spreadme.core.event;

public class HomeworkEvent extends Event {

    private static final long serialVersionUID = 1677055378379029089L;

    public HomeworkEvent(Teacher teacher) {
        super(teacher);
    }

    public Teacher getTeacher() {
        return (Teacher) super.getSource();
    }
}
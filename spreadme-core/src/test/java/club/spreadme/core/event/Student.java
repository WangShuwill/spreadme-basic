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

public class Student implements IEventListener<HomeworkEvent> {

    private String name;
    private long handleTime;

    public Student(String name, long handleTime) {
        this.name = name;
        this.handleTime = handleTime;
    }

    @Override
    public boolean supportsEventType(Class<?> eventType) {
        return eventType.equals(HomeworkEvent.class);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return sourceType.equals(Teacher.class);
    }

    @Override
    public void onApplicationEvent(HomeworkEvent event) {
        Teacher teacher = (Teacher) event.getSource();
        try {
            Thread.sleep(handleTime);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("学生%s被通知%s布置了作业 \n", this.name, teacher.getName());
    }
}

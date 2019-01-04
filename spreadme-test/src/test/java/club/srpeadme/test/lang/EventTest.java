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

package club.srpeadme.test.lang;

import club.spreadme.lang.EventBus;
import club.srpeadme.test.lang.event.HomeworkEvent;
import club.srpeadme.test.lang.event.Student;
import club.srpeadme.test.lang.event.Teacher;
import org.junit.Test;

import java.util.concurrent.Executors;

public class EventTest {

    @Test
    public void testEvent() throws InterruptedException {
        Student student1 = new Student("张三", 2 * 1000);
        Student student2 = new Student("李四", 4 * 1000);

        Teacher teacher = new Teacher("zuikc");

        EventBus eventBus = new EventBus(Executors.newCachedThreadPool());
        eventBus.addEventListener(student1).addEventListener(student2);

        eventBus.publishEvent(new HomeworkEvent(teacher));

        System.out.println("Doing some other thing...");

        Thread.sleep(10 * 1000);
    }

}

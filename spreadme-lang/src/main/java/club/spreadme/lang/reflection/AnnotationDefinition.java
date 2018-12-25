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

package club.spreadme.lang.reflection;

import club.spreadme.lang.Reflection;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * the definition of annotation
 *
 * @author wswei
 * @see Reflection
 * @since 2018
 */
public class AnnotationDefinition {

    private Class<? extends Annotation> type;
    private Map<String, Object> attributes;

    public AnnotationDefinition() {

    }

    public AnnotationDefinition(Class<? extends Annotation> type, Map<String, Object> attributes) {
        this.type = type;
        this.attributes = attributes;
    }

    public Class<? extends Annotation> getType() {
        return type;
    }

    public void setType(Class<? extends Annotation> type) {
        this.type = type;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}

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

package club.spreadme.database.bind.support;

import club.spreadme.database.annotation.*;
import club.spreadme.database.exception.DAOMehtodException;
import club.spreadme.database.metadata.SQLOptionType;
import club.spreadme.lang.Reflection;
import club.spreadme.lang.StringUtil;
import club.spreadme.lang.reflection.AnnotationDefinition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SQLCommand {

    private static final List<Class> optionClazzes = Arrays.asList(
            Query.class, Update.class, Insert.class, Delete.class);

    private final String type;
    private final String sql;
    private final SQLOptionType sqlOptionType;
    private final Class<? extends PostProcessor> postProcessor;

    @SuppressWarnings("unchecked")
    public SQLCommand(Method method) {
        // get database annotion by method
        Object[] optionAnnotations = optionClazzes.stream()
                .map(item -> Reflection.getAnnotation(method, item))
                .filter(Objects::nonNull)
                .toArray();

        if (optionAnnotations.length < 1) {
            throw new DAOMehtodException("No database option annatation for the method " + method.getName());
        }
        if (optionAnnotations.length > 1) {
            throw new DAOMehtodException("Too many database option annatation for the method " + method.getName());
        }

        Annotation optionAnnotation = (Annotation) optionAnnotations[0];
        AnnotationDefinition annotationDefinition = Reflection.getAnnotationDefinition(optionAnnotation);
        this.type = annotationDefinition.getType().getTypeName();
        this.sql = Optional.ofNullable(annotationDefinition.getAttributes().get("value")).orElse("").toString();
        if (StringUtil.isBlank(this.sql)) {
            throw new DAOMehtodException("No sql statement fo database option annatation for the method " + method.getName());
        }

        this.postProcessor = (Class<? extends PostProcessor>) annotationDefinition.getAttributes().get("processor");
        String packageName = annotationDefinition.getType().getPackage().getName();
        this.sqlOptionType = SQLOptionType.resolve(type.replace(packageName + ".", "").toUpperCase());
    }

    public String getType() {
        return type;
    }

    public String getSql() {
        return sql;
    }

    public SQLOptionType getSqlOptionType() {
        return sqlOptionType;
    }

    public Class<? extends PostProcessor> getPostProcessor() {
        return postProcessor;
    }
}

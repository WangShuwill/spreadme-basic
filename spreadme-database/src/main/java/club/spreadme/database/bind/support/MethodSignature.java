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

package club.spreadme.database.bind;

import club.spreadme.lang.Reflection;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class MethodSignature {

    private final Class<?> daoInterface;
    private final String methodName;
    private final boolean returnsMany;
    private final boolean returnsMap;
    private final boolean returnsVoid;
    private final boolean isAllPrimaryParamter;

    private final Method method;
    private final Class<?> returnType;

    private Object[] values;
    private Type[] actualTypes;

    public MethodSignature(Class<?> daoInterface, Method method, Object[] values) {
        this.daoInterface = daoInterface;
        this.methodName = method.getName();
        this.values = values;
        this.isAllPrimaryParamter = Arrays.stream(values).filter(item -> Reflection.isPrimaryType(item.getClass())).count() == values.length;
        Type resolveReturnType = method.getGenericReturnType();

        if (resolveReturnType instanceof Class<?>) {
            this.returnType = (Class<?>) resolveReturnType;
        }
        else if (resolveReturnType instanceof ParameterizedType) {
            this.returnType = (Class<?>) ((ParameterizedType) resolveReturnType).getRawType();
            this.actualTypes = ((ParameterizedType) resolveReturnType).getActualTypeArguments();
        }
        else {
            this.returnType = method.getReturnType();
        }

        this.method = method;
        this.returnsVoid = void.class.equals(returnType);
        this.returnsMany = Collection.class.isAssignableFrom(returnType) || this.returnType.isArray();
        this.returnsMap = Map.class.isAssignableFrom(returnType);
    }

    public Class<?> getDaoInterface() {
        return daoInterface;
    }

    public String getMethodName() {
        return methodName;
    }

    public boolean isReturnsMany() {
        return returnsMany;
    }

    public boolean isReturnsMap() {
        return returnsMap;
    }

    public boolean isReturnsVoid() {
        return returnsVoid;
    }

    public boolean isAllPrimaryParamter() {
        return isAllPrimaryParamter;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    public Type[] getActualTypes() {
        return actualTypes;
    }
}

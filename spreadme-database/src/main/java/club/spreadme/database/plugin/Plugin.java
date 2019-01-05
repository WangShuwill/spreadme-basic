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

package club.spreadme.database.plugin;

import club.spreadme.database.exception.PluginException;
import club.spreadme.database.plugin.annotation.Intercepts;
import club.spreadme.database.plugin.annotation.Signature;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Wangshuwei
 * @since 2018-8-13
 */
public class Plugin implements InvocationHandler {

    private final Object target;
    private final Interceptor interceptor;
    private final Map<Class<?>, Set<Method>> signatureMap;

    private Plugin(Object target, Interceptor interceptor, Map<Class<?>, Set<Method>> signatureMap) {
        this.target = target;
        this.interceptor = interceptor;
        this.signatureMap = signatureMap;
    }

    public static Object wrap(Object target, Interceptor interceptor) {
        Map<Class<?>, Set<Method>> signatureMap = getSignatureMap(interceptor);
        Class<?> type = target.getClass();
        Class<?>[] interfaces = getAllInterfaces(type, signatureMap);
        if (interfaces.length > 0) {
            return Proxy.newProxyInstance(type.getClassLoader(), interfaces, new Plugin(target, interceptor, signatureMap));
        }
        return target;
    }

    // get really target object from proxy object
    public static Object getTarget(Object proxy) {
        Plugin plugin = (Plugin) Proxy.getInvocationHandler(proxy);
        Object target = plugin.getTarget();
        if (Proxy.isProxyClass(target.getClass())) {
            return getTarget(target);
        } else {
            return target;
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Set<Method> methods = signatureMap.get(method.getDeclaringClass());
        if (methods != null && methods.contains(method)) {
            return interceptor.intercept(new Invocation(target, method, args));
        }
        return method.invoke(target, args);
    }

    private static Map<Class<?>, Set<Method>> getSignatureMap(Interceptor interceptor) {
        Intercepts interceptsAnnotation = interceptor.getClass().getAnnotation(Intercepts.class);

        if (interceptsAnnotation == null) {
            throw new PluginException("No @intercepts annotation was found in interceptor " + interceptor.getClass().getName());
        }

        Signature[] signatures = interceptsAnnotation.value();
        Map<Class<?>, Set<Method>> signatureMap = new HashMap<>();
        for (Signature signature : signatures) {
            Set<Method> methods = signatureMap.computeIfAbsent(signature.type(), k -> new HashSet<>());
            try {
                Method method = signature.type().getMethod(signature.method(), signature.args());
                methods.add(method);
            } catch (NoSuchMethodException e) {
                throw new PluginException(e.getMessage());
            }
        }

        return signatureMap;
    }

    private static Class<?>[] getAllInterfaces(Class<?> type, Map<Class<?>, Set<Method>> signatureMap) {
        Set<Class<?>> interfaces = new HashSet<>();
        while (type != null) {
            for (Class<?> clazz : type.getInterfaces()) {
                if (signatureMap.containsKey(clazz)) {
                    interfaces.add(clazz);
                }
            }
            type = type.getSuperclass();
        }
        return interfaces.toArray(new Class<?>[0]);
    }

    public Object getTarget() {
        return target;
    }

    public Interceptor getInterceptor() {
        return interceptor;
    }

    public Map<Class<?>, Set<Method>> getSignatureMap() {
        return signatureMap;
    }
}

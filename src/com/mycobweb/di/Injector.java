// di/Injector.java
package com.mycobweb.di;

import com.mycobweb.annotation.Injection;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Injector {
    private final Map<Class<?>, Object> instanceCache = new HashMap<>();

    public <T> T getInstance(Class<T> type) {
        if (instanceCache.containsKey(type)) {
            return type.cast(instanceCache.get(type));
        }

        try {
            T instance = type.getDeclaredConstructor().newInstance();
            injectDependencies(instance);
            instanceCache.put(type, instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create instance of " + type.getSimpleName(), e);
        }
    }

    private void injectDependencies(Object instance) {
        Class<?> clazz = instance.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Injection.class)) {
                try {
                    field.setAccessible(true);

                    Injection ann = field.getAnnotation(Injection.class);
                    String implClassName = ann.type();

                    // Load class by name
                    Class<?> implClass;
                    try {
                        implClass = Class.forName(implClassName);
                    } catch (ClassNotFoundException e) {
                        throw new IllegalArgumentException(
                            "Class not found: " + implClassName, e);
                    }

                    // Check assignment compatibility
                    if (!field.getType().isAssignableFrom(implClass)) {
                        throw new IllegalArgumentException(
                            "Cannot assign " + implClass.getSimpleName() +
                            " to field of type " + field.getType().getSimpleName());
                    }

                    // Create instance of the implementation
                    Object dependency = implClass.getDeclaredConstructor().newInstance();

                    // Recursively inject dependencies in the implementation
                    injectDependencies(dependency);

                    // Set it on the field
                    field.set(instance, dependency);

                } catch (Exception e) {
                    throw new RuntimeException("Failed to inject field: " + field.getName(), e);
                }
            }
        }
    }
}
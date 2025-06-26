package com.newsfeed.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {
    private static Map<Class<?>, Object> objectsContainer = new HashMap<>();

    public static <T> T getObject(Class<T> className) {
        try {
            if (objectsContainer.containsKey(className)) {
                return className.cast(objectsContainer.get(className));
            }

            Constructor<?>[] constructors = className.getConstructors();
            Constructor<?> constructor = constructors[0];

            Class<?>[] paramTypes = constructor.getParameterTypes();
            Object[] dependencies = new Object[paramTypes.length];

            for (int i = 0; i < paramTypes.length; i++) {
            	Class<?> parameterType = paramTypes[i];
                dependencies[i] = getObject(parameterType);
                objectsContainer.put(parameterType, dependencies[i]);
            }

            T instance = className.cast(constructor.newInstance(dependencies));
            objectsContainer.put(className, instance);
            return instance;
            
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException| InvocationTargetException exception) {
            throw new RuntimeException("Failed to instantiate: " + className.getName());
        }
    }
    
    public static <T> void registerInstance(Class<T> className, T instance) {
    	objectsContainer.put(className, instance);
    }
}


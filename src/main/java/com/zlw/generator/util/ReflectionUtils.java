package com.zlw.generator.util;

import com.zlw.generator.db.util.CacheFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public class ReflectionUtils {

    public static final String CGLIB_CLASS_SEPARATOR = "$$";
    private static final Logger log = LoggerFactory.getLogger(ReflectionUtils.class);

    private ReflectionUtils() {
    }

    public static Object invokeGetter(Object obj, String propertyName) {
        String getterMethodName = get(propertyName);
        return invokeMethod(obj, getterMethodName, new Class[0], new Object[0]);
    }

    public static void invokeSetter(Object obj, String propertyName, Object value) {
        if(value != null) {
            invokeSetter(obj, propertyName, value, (Class)null);
        }
    }

    public static void invokeSetter(Object obj, String propertyName, Object value, Class<?> propertyType) {
        Class type = propertyType != null?propertyType:value.getClass();
        String setterMethodName = set(propertyName);
        invokeMethod(obj, setterMethodName, new Class[]{type}, new Object[]{value});
    }

    public static Object getFieldValue(Object obj, String fieldName) {
        Field field = getAccessibleField(obj, fieldName);
        if(field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        } else {
            Object result = null;

            try {
                result = field.get(obj);
            } catch (IllegalAccessException var5) {
                log.error("{}[不可访问的对象]", obj.getClass().getName(), var5);
            }

            return result;
        }
    }

    public static void setFieldValue(Object obj, String fieldName, Object value) {
        Field field = getAccessibleField(obj, fieldName);
        if(field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        } else {
            try {
                field.set(obj, value);
            } catch (IllegalAccessException var5) {
                log.error("{}[不可访问的对象]", obj.getClass().getName(), var5);
            }

        }
    }

    public static Class<?> getUserClass(Class<?> clazz) {
        if(clazz != null && clazz.getName().contains("$$")) {
            Class superClass = clazz.getSuperclass();
            if(superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }

        return clazz;
    }

    public static Object invokeMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object[] args) {
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if(method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        } else {
            try {
                return method.invoke(obj, args);
            } catch (Exception var6) {
                throw convertReflectionExceptionToUnchecked(var6);
            }
        }
    }

    public static Field getAccessibleField(Object obj, String fieldName) {
        Field field = CacheFactory.MINSTANCE.getField(obj, fieldName);
        if(field != null) {
            return field;
        } else {
            Class superClass = obj instanceof Class?(Class)obj:obj.getClass();

            while(superClass != Object.class) {
                try {
                    field = superClass.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    CacheFactory.MINSTANCE.putField(obj, field);
                    return field;
                } catch (NoSuchFieldException var5) {
                    superClass = superClass.getSuperclass();
                }
            }

            return null;
        }
    }

    public static Method getAccessibleMethod(Object obj, String methodName, Class<?>... parameterTypes) {
        Method method = CacheFactory.MINSTANCE.getMethod(obj, methodName, parameterTypes);
        if(method != null) {
            return method;
        } else {
            try {
                method = ((Class)((Class)(obj instanceof Class?obj:obj.getClass()))).getMethod(methodName, parameterTypes);
                method.setAccessible(true);
                CacheFactory.MINSTANCE.putMethod(obj, parameterTypes, method);
                return method;
            } catch (NoSuchMethodException var8) {
                Class superClass = obj.getClass();

                while(superClass != Object.class) {
                    try {
                        method = superClass.getDeclaredMethod(methodName, parameterTypes);
                        method.setAccessible(true);
                        CacheFactory.MINSTANCE.putMethod(obj, parameterTypes, method);
                        return method;
                    } catch (NoSuchMethodException var7) {
                        superClass = superClass.getSuperclass();
                    }
                }

                return null;
            }
        }
    }

    public static List<Field> getAllFields(Object obj) {
        return getAllFields(obj, Object.class);
    }

    public static List<Field> getAllFields(Object obj, Class theEnd) {
        ArrayList fields = new ArrayList();

        for(Class superClass = obj instanceof Class?(Class)obj:obj.getClass(); superClass != theEnd; superClass = superClass.getSuperclass()) {
            Field[] field = superClass.getDeclaredFields();
            Field[] var5 = field;
            int var6 = field.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                Field f = var5[var7];
                f.setAccessible(true);
                fields.add(f);
            }
        }

        return fields;
    }

    public static List<Method> getAllMethods(Object obj) {
        log.trace("getAllMethods from class:{}", obj);
        ArrayList methods = new ArrayList();

        for(Class superClass = obj instanceof Class?(Class)obj:obj.getClass(); superClass != Object.class && superClass != null; superClass = superClass.getSuperclass()) {
            Method[] method = superClass.getDeclaredMethods();
            Method[] var4 = method;
            int var5 = method.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Method method1 = var4[var6];
                method1.setAccessible(true);
                methods.add(method1);
            }
        }

        return methods;
    }

    public static <T> Class<T> getSuperClassGenricType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    public static Class getSuperClassGenricType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if(!genType.getClass().isAssignableFrom(ParameterizedType.class)) {
            return Object.class;
        } else {
            Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
            return index < params.length && index >= 0?(!params[index].getClass().isAssignableFrom(Class.class)?Object.class:(Class)params[index]):Object.class;
        }
    }

    public static Object instance(String className) {
        log.trace("instance for {}", className);

        try {
            Class e = Class.forName(className);
            return e.newInstance();
        } catch (ClassNotFoundException var2) {
            log.error("[无法找到方言类]", var2);
            return null;
        } catch (InstantiationException var3) {
            log.error("[实例化方言错误]", var3);
            return null;
        } catch (IllegalAccessException var4) {
            log.error("[实例化方言错误]", var4);
            return null;
        }
    }

    public static Object instance(String className, Class[] types, Object[] objects) {
        log.trace("instance for {},types {},objects {}", new Object[]{className, types, objects});

        try {
            Class e = Class.forName(className);
            Constructor constructor = e.getDeclaredConstructor(types);
            constructor.setAccessible(true);
            return constructor.newInstance(objects);
        } catch (ClassNotFoundException var5) {
            log.error("[无法找到方言类]", var5);
            return null;
        } catch (NoSuchMethodException var6) {
            log.error("[无法找到方言类]", var6);
            return null;
        } catch (InstantiationException var7) {
            log.error("[实例化方言错误]", var7);
            return null;
        } catch (InvocationTargetException var8) {
            log.error("[实例化方言错误]", var8);
            return null;
        } catch (IllegalAccessException var9) {
            log.error("[实例化方言错误]", var9);
            return null;
        }
    }

    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        return (RuntimeException)(!(e instanceof IllegalAccessException) && !(e instanceof IllegalArgumentException) && !(e instanceof NoSuchMethodException)?(e instanceof InvocationTargetException?new RuntimeException(((InvocationTargetException)e).getTargetException()):(e instanceof RuntimeException?(RuntimeException)e:new RuntimeException("Unexpected Checked Exception.", e))):new IllegalArgumentException(e));
    }

    public static boolean isSimpleType(Class clazz) {
        return Number.class.isAssignableFrom(clazz) || clazz.equals(String.class) || Date.class.isAssignableFrom(clazz) || Boolean.TYPE.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz);
    }

    public static String get(String name) {
        String get = "get" + (name.charAt(0) + "").toUpperCase() + name.substring(1);
        return get;
    }

    public static String set(String name) {
        return "set" + (name.charAt(0) + "").toUpperCase() + name.substring(1);
    }
}

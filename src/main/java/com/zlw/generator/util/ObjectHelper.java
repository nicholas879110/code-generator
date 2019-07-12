package com.zlw.generator.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public class ObjectHelper {
    public ObjectHelper() {
    }

    public static boolean isNullOrEmptyString(Object o) {
        return o == null?true:o.toString().length() == 0;
    }

    public static boolean isEmpty(Object o) {
        if(o == null) {
            return true;
        } else {
            if(o instanceof String) {
                if(((String)o).length() == 0) {
                    return true;
                }
            } else if(o instanceof Collection) {
                if(((Collection)o).isEmpty()) {
                    return true;
                }
            } else if(o.getClass().isArray()) {
                if(Array.getLength(o) == 0) {
                    return true;
                }
            } else {
                if(!(o instanceof Map)) {
                    return false;
                }

                if(((Map)o).isEmpty()) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean isNotEmpty(Object c) throws IllegalArgumentException {
        return !isEmpty(c);
    }

    public static boolean isAllNull(Object... objects) {
        Object[] var1 = objects;
        int var2 = objects.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Object object = var1[var3];
            if(object != null) {
                return false;
            }
        }

        return true;
    }

    public static boolean isAllNotNull(Object... objects) {
        Object[] var1 = objects;
        int var2 = objects.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            Object object = var1[var3];
            if(object == null) {
                return false;
            }
        }

        return true;
    }

    public static boolean isAllEmpty(Object... objects) {
        for(int i = 0; i < objects.length; ++i) {
            if(!isNullOrEmptyString(objects[i])) {
                return false;
            }
        }

        return true;
    }

    public static boolean isAllNotEmpty(Object... objects) {
        for(int i = 0; i < objects.length; ++i) {
            if(isNullOrEmptyString(objects[i])) {
                return false;
            }
        }

        return true;
    }
}

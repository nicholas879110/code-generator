package com.zlw.generator.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/9/28
 *
 */
public class ConverString {
    private static String[] FALSE_STRINGS = new String[]{"false", "null", "nul", "off", "no", "n"};
    private static String DEFAULT_DATE_PATTTERN = "yyyy-MM-dd";

    public ConverString() {
    }

    public static BigDecimal asBigDecimal(String str) {
        return asBigDecimal(str, new BigDecimal(BigInteger.ZERO));
    }

    public static BigDecimal asBigDecimal(String str, BigDecimal defaultValue) {
        try {
            return new BigDecimal(str.trim());
        } catch (NullPointerException var3) {
            return defaultValue;
        } catch (NumberFormatException var4) {
            return defaultValue;
        } catch (RuntimeException var5) {
            return defaultValue;
        }
    }

    public static BigInteger asBigInteger(String str) {
        return asBigInteger(str, BigInteger.ZERO);
    }

    public static BigInteger asBigInteger(String str, BigInteger defaultValue) {
        try {
            return new BigInteger(str.trim());
        } catch (NullPointerException var3) {
            return defaultValue;
        } catch (NumberFormatException var4) {
            return defaultValue;
        }
    }

    public static boolean asBoolean(String str) {
        return asBoolean(str, false);
    }

    public static Boolean asBoolean(String str, Boolean defaultValue) {
        try {
            str = str.trim();
            return Integer.decode(str).intValue() == 0?Boolean.FALSE:Boolean.TRUE;
        } catch (NullPointerException var3) {
            return defaultValue;
        } catch (NumberFormatException var4) {
            if(str.equals("")) {
                return defaultValue;
            } else {
                for(int i = 0; i < FALSE_STRINGS.length; ++i) {
                    if(str.equalsIgnoreCase(FALSE_STRINGS[i])) {
                        return Boolean.FALSE;
                    }
                }

                return Boolean.TRUE;
            }
        }
    }

    public static boolean asBoolean(String str, boolean defaultValue) {
        try {
            str = str.trim();
            return Integer.decode(str).intValue() != 0;
        } catch (NullPointerException var3) {
            return defaultValue;
        } catch (NumberFormatException var4) {
            if(str.equals("")) {
                return defaultValue;
            } else {
                for(int i = 0; i < FALSE_STRINGS.length; ++i) {
                    if(str.equalsIgnoreCase(FALSE_STRINGS[i])) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    public static byte asByte(String str) {
        return asByte(str, (byte)0);
    }

    public static Byte asByte(String str, Byte defaultValue) {
        try {
            return Byte.decode(str.trim());
        } catch (NullPointerException var3) {
            return defaultValue;
        } catch (NumberFormatException var4) {
            return defaultValue;
        }
    }

    public static byte asByte(String str, byte defaultValue) {
        try {
            return Byte.decode(str.trim()).byteValue();
        } catch (NullPointerException var3) {
            return defaultValue;
        } catch (NumberFormatException var4) {
            return defaultValue;
        }
    }

    public static char asCharacter(String str) {
        return asCharacter(str, '\u0000');
    }

    public static Character asCharacter(String str, Character defaultValue) {
        try {
            return new Character(str.trim().charAt(0));
        } catch (NullPointerException var3) {
            return defaultValue;
        } catch (IndexOutOfBoundsException var4) {
            return defaultValue;
        }
    }

    public static char asCharacter(String str, char defaultValue) {
        try {
            return str.trim().charAt(0);
        } catch (NullPointerException var3) {
            return defaultValue;
        } catch (IndexOutOfBoundsException var4) {
            return defaultValue;
        }
    }

    public static double asDouble(String str) {
        return asDouble(str, 0.0D);
    }

    public static Double asDouble(String str, Double defaultValue) {
        try {
            return new Double(str.trim());
        } catch (NullPointerException var3) {
            return defaultValue;
        } catch (NumberFormatException var4) {
            return defaultValue;
        }
    }

    public static double asDouble(String str, double defaultValue) {
        try {
            return (new Double(str.trim())).doubleValue();
        } catch (NullPointerException var4) {
            return defaultValue;
        } catch (NumberFormatException var5) {
            return defaultValue;
        }
    }

    public static float asFloat(String str) {
        return asFloat(str, 0.0F);
    }

    public static Float asFloat(String str, Float defaultValue) {
        try {
            return new Float(str.trim());
        } catch (NullPointerException var3) {
            return defaultValue;
        } catch (NumberFormatException var4) {
            return defaultValue;
        }
    }

    public static float asFloat(String str, float defaultValue) {
        try {
            return (new Float(str.trim())).floatValue();
        } catch (NullPointerException var3) {
            return defaultValue;
        } catch (NumberFormatException var4) {
            return defaultValue;
        }
    }

    public static int asInteger(String str) {
        return asInteger(str, 0);
    }

    public static Integer asInteger(String str, Integer defaultValue) {
        try {
            return Integer.decode(str.trim());
        } catch (NullPointerException var3) {
            return defaultValue;
        } catch (NumberFormatException var4) {
            return defaultValue;
        }
    }

    public static int asInteger(String str, int defaultValue) {
        try {
            return Integer.decode(str.trim()).intValue();
        } catch (NullPointerException var3) {
            return defaultValue;
        } catch (NumberFormatException var4) {
            return defaultValue;
        }
    }

    public static long asLong(String str) {
        return asLong(str, 0L);
    }

    public static Long asLong(String str, Long defaultValue) {
        try {
            return Long.decode(str.trim());
        } catch (NullPointerException var3) {
            return defaultValue;
        } catch (NumberFormatException var4) {
            return defaultValue;
        }
    }

    public static long asLong(String str, long defaultValue) {
        try {
            return Long.decode(str.trim()).longValue();
        } catch (NullPointerException var4) {
            return defaultValue;
        } catch (NumberFormatException var5) {
            return defaultValue;
        }
    }

    public static short asShort(String str) {
        return asShort(str, (short)0);
    }

    public static Short asShort(String str, Short defaultValue) {
        try {
            return Short.decode(str.trim());
        } catch (NullPointerException var3) {
            return defaultValue;
        } catch (NumberFormatException var4) {
            return defaultValue;
        }
    }

    public static short asShort(String str, short defaultValue) {
        try {
            return Short.decode(str.trim()).shortValue();
        } catch (NullPointerException var3) {
            return defaultValue;
        } catch (NumberFormatException var4) {
            return defaultValue;
        }
    }

    public static String asString(String str) {
        return asString(str, "", "");
    }

    public static String asString(String str, String defaultValue) {
        return asString(str, defaultValue, defaultValue);
    }

    public static String asString(String str, String defaultValue, String emptyStringValue) {
        try {
            return str.equals("")?emptyStringValue:str;
        } catch (NullPointerException var4) {
            return defaultValue;
        }
    }

    public static Date asDate(String str) {
        return asDate(str, new Date(), (String)null);
    }

    public static Date asDate(String str, Date defaultValue) {
        return asDate(str, defaultValue, (String)null);
    }

    public static Date asDate(String str, Date defaultValue, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern != null?pattern:DEFAULT_DATE_PATTTERN);

        try {
            return formatter.parse(str);
        } catch (ParseException var5) {
            return defaultValue;
        } catch (NullPointerException var6) {
            return defaultValue;
        }
    }

    public static Object asType(Class type, String str) {
        return type.isAssignableFrom(String.class)?asString(str, "", ""):(!type.isAssignableFrom(Integer.class) && !type.equals(Integer.TYPE)?(!type.isAssignableFrom(Double.class) && !type.equals(Double.TYPE)?(!type.isAssignableFrom(Boolean.class) && !type.equals(Boolean.TYPE)?(!type.isAssignableFrom(Float.class) && !type.equals(Float.TYPE)?(!type.isAssignableFrom(Long.class) && !type.equals(Long.TYPE)?(!type.isAssignableFrom(Short.class) && !type.equals(Short.TYPE)?(!type.isAssignableFrom(Byte.class) && !type.equals(Byte.TYPE)?(!type.isAssignableFrom(Character.class) && !type.equals(Character.TYPE)?(type.isAssignableFrom(BigDecimal.class)?asBigDecimal(str, new BigDecimal(BigInteger.ZERO)):(type.isAssignableFrom(BigInteger.class)?asBigInteger(str, BigInteger.ZERO):(type.isAssignableFrom(java.sql.Date.class)?java.sql.Date.valueOf(str):(type.isAssignableFrom(Timestamp.class)?Timestamp.valueOf(str):(type.isAssignableFrom(Date.class)?asDate(str, new Date(), (String)null):null))))):asCharacter(str, new Character('\u0000'))):asByte(str, new Byte((byte)0))):asShort(str, new Short((short)0))):asLong(str, new Long(0L))):asFloat(str, new Float(0.0F))):asBoolean(str, Boolean.FALSE)):asDouble(str, new Double(0.0D))):asInteger(str, new Integer(0)));
    }

    public static Object asType(Class type, String str, Object defaultValue) {
        return type.isAssignableFrom(String.class)?asString(str, (String)defaultValue):(!type.isAssignableFrom(Integer.class) && !type.equals(Integer.TYPE)?(!type.isAssignableFrom(Double.class) && !type.equals(Double.TYPE)?(!type.isAssignableFrom(Boolean.class) && !type.equals(Boolean.TYPE)?(!type.isAssignableFrom(Float.class) && !type.equals(Float.TYPE)?(!type.isAssignableFrom(Long.class) && !type.equals(Long.TYPE)?(!type.isAssignableFrom(Short.class) && !type.equals(Short.TYPE)?(!type.isAssignableFrom(Byte.class) && !type.equals(Byte.TYPE)?(!type.isAssignableFrom(Character.class) && !type.equals(Character.TYPE)?(type.isAssignableFrom(BigDecimal.class)?asBigDecimal(str, (BigDecimal)defaultValue):(type.isAssignableFrom(BigInteger.class)?asBigInteger(str, (BigInteger)defaultValue):(type.isAssignableFrom(java.sql.Date.class)?java.sql.Date.valueOf(str):(type.isAssignableFrom(Timestamp.class)?Timestamp.valueOf(str):(type.isAssignableFrom(Date.class)?asDate(str, (Date)defaultValue):null))))):asCharacter(str, (Character)defaultValue)):asByte(str, (Byte)defaultValue)):asShort(str, (Short)defaultValue)):asLong(str, (Long)defaultValue)):asFloat(str, (Float)defaultValue)):asBoolean(str, (Boolean)defaultValue)):asDouble(str, (Double)defaultValue)):asInteger(str, (Integer)defaultValue));
    }

    public static Object asType(Class type, Object obj) {
        if(!type.equals(String.class) && type.isInstance(obj)) {
            return obj;
        } else if(obj != null && !(obj instanceof String)) {
            if(obj instanceof Date && String.class.isAssignableFrom(type)) {
                return (new SimpleDateFormat(DEFAULT_DATE_PATTTERN)).format((Date)obj);
            } else {
                if(obj instanceof Number && Number.class.isAssignableFrom(type)) {
                    Number num = (Number)obj;
                    if(type.isAssignableFrom(Number.class)) {
                        return num;
                    }

                    if(type.isAssignableFrom(Integer.class)) {
                        return new Integer(num.intValue());
                    }

                    if(type.isAssignableFrom(Double.class)) {
                        return new Double(num.doubleValue());
                    }

                    if(type.isAssignableFrom(Float.class)) {
                        return new Float(num.floatValue());
                    }

                    if(type.isAssignableFrom(Long.class)) {
                        return new Long(num.longValue());
                    }

                    if(type.isAssignableFrom(Short.class)) {
                        return new Short(num.shortValue());
                    }

                    if(type.isAssignableFrom(Byte.class)) {
                        return new Byte(num.byteValue());
                    }

                    if(type.isAssignableFrom(BigInteger.class)) {
                        return (new BigDecimal(num.toString())).toBigInteger();
                    }

                    if(type.isAssignableFrom(BigDecimal.class)) {
                        return new BigDecimal(num.toString());
                    }
                }

                return asType(type, (String)obj.toString());
            }
        } else {
            return asType(type, (String)((String)obj));
        }
    }

    public static Object asType(Class type, Object obj, Object defaultValue) {
        if(!type.equals(String.class) && type.isInstance(obj)) {
            return obj;
        } else if(obj != null && !(obj instanceof String)) {
            if(obj instanceof Date && String.class.isAssignableFrom(type)) {
                return (new SimpleDateFormat(DEFAULT_DATE_PATTTERN)).format((Date)obj);
            } else {
                if(obj instanceof Number && Number.class.isAssignableFrom(type)) {
                    Number num = (Number)obj;
                    if(type.isAssignableFrom(Number.class)) {
                        return num;
                    }

                    if(type.isAssignableFrom(Integer.class)) {
                        return new Integer(num.intValue());
                    }

                    if(type.isAssignableFrom(Double.class)) {
                        return new Double(num.doubleValue());
                    }

                    if(type.isAssignableFrom(Float.class)) {
                        return new Float(num.floatValue());
                    }

                    if(type.isAssignableFrom(Long.class)) {
                        return new Long(num.longValue());
                    }

                    if(type.isAssignableFrom(Short.class)) {
                        return new Short(num.shortValue());
                    }

                    if(type.isAssignableFrom(Byte.class)) {
                        return new Byte(num.byteValue());
                    }

                    if(type.isAssignableFrom(BigInteger.class)) {
                        return (new BigDecimal(num.toString())).toBigInteger();
                    }

                    if(type.isAssignableFrom(BigDecimal.class)) {
                        return new BigDecimal(num.toString());
                    }
                }

                return asType(type, (String)obj.toString(), defaultValue);
            }
        } else {
            return asType(type, (String)((String)obj), defaultValue);
        }
    }

    public static String getClassName(Class cls) {
        return getClassName((String)cls.getName());
    }

    public static String getClassName(String fullName) {
        if(fullName == null) {
            return null;
        } else {
            fullName = fullName.trim();
            String className = fullName.substring(fullName.lastIndexOf(46) + 1).replace('$', '.').trim();
            return className.equals("")?null:className;
        }
    }

    public static int ConvertToInt(String str) {
        int num;
        try {
            num = Integer.parseInt(str);
        } catch (Exception var3) {
            num = 0;
        }

        return num;
    }

    public static Integer ConvertToInteger(String str) {
        Integer num;
        try {
            num = Integer.valueOf(Integer.parseInt(str));
        } catch (Exception var3) {
            num = Integer.valueOf(0);
        }

        return num;
    }

    public static long ConvertToLong(String str) {
        long num;
        try {
            num = Long.parseLong(str);
        } catch (Exception var4) {
            num = 0L;
        }

        return num;
    }

    public static Long ConvertToLonger(String str) {
        Long num;
        try {
            num = Long.valueOf(Long.parseLong(str));
        } catch (Exception var3) {
            num = Long.valueOf(0L);
        }

        return num;
    }
}

package com.zlw.generator.util;

import java.math.BigDecimal;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public class NumberUtils {

    public NumberUtils() {
    }

    public static Float format(Object num, int scale) {
        BigDecimal b = new BigDecimal(num.toString());
        return Float.valueOf(b.setScale(scale, 4).floatValue());
    }

    public static int parseInt(Object o) {
        if(o == null) {
            return 0;
        } else if(o instanceof Integer) {
            return ((Integer)o).intValue();
        } else {
            try {
                return Integer.parseInt(o.toString());
            } catch (NumberFormatException var2) {
                return 0;
            }
        }
    }

    public static Double parseDouble(Object o) {
        if(o == null) {
            return Double.valueOf(0.0D);
        } else if(o instanceof Double) {
            return (Double)o;
        } else {
            try {
                return Double.valueOf(Double.parseDouble(o.toString()));
            } catch (NumberFormatException var2) {
                return Double.valueOf(0.0D);
            }
        }
    }

    public static Integer[] parseInts(String str) {
        String[] split = str.split(",");
        Integer[] integers = new Integer[split.length];

        for(int i = 0; i < split.length; ++i) {
            integers[i] = Integer.valueOf(parseInt(split[i]));
        }

        return integers;
    }
}

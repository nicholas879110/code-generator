package com.zlw.generator.persistence;

import java.lang.annotation.*;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/9/27
 *
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {
    String name() default "";
}


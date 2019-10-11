package com.github.chuanzh.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author：zhangchuan
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface INote {
    String value();

    boolean require() default false;

    int sort() default 0;

}

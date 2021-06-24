package com.yu.hu.libnavannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;


/**
 * @author Hy
 * created on 2020/04/14 23:43
 * <p>
 * 用于注解Activity
 **/
@Target(ElementType.TYPE) //只能标记在类的头部
public @interface ActivityDestination {

    String pageUrl();

    boolean needLogin() default false;

    boolean asStarter() default false;

    boolean tabPage() default false;
}

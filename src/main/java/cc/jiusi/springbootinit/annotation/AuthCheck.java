package cc.jiusi.springbootinit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2024-04-28  16:54
 * @Description: 权限校验，加注解即需要登录才能访问，还可以通过 mustRole 指定角色权限
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {
    /**
     * 是否开启校验
     */
    boolean enableCheck() default true;

    /**
     * 必须有某个角色
     */
    String mustRole() default "";
}
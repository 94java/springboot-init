package cc.jiusi.springbootinit.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2024-04-28  16:54
 * @Description: 公共字段自动填充
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoFill {
    String fieldName() default ""; // 可以用来指定数据库中的字段名，如果与属性名一致则不需要  

    AutoFillType type() default AutoFillType.BOTH; // 用来指定填充类型，比如创建时填充、更新时填充或都填充

    FieldType fieldType() default FieldType.DATETIME; // 填充数据类型：时间或者用户 id

    enum AutoFillType {
        INSERT,
        UPDATE,
        BOTH
    }

    enum FieldType {
        DATETIME, // 时间类型
        USER_ID // 用户ID类型，从ThreadLocal获取
    }
}
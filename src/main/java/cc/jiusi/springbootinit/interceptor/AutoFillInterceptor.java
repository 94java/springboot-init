package cc.jiusi.springbootinit.interceptor;

import cc.jiusi.springbootinit.annotation.AutoFill;
import cc.jiusi.springbootinit.common.ErrorCode;
import cc.jiusi.springbootinit.common.UserContextHolder;
import cc.jiusi.springbootinit.exception.BusinessException;
import cn.hutool.core.collection.CollUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.PreparedStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.*;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2024-05-01 10:32
 * @Description: mybatis 公共字段填充插件 INSERT / UPDATE / BOTH
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
@Component
public class AutoFillInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        // 获取SQL命令类型
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        // 自动填充只在新增或者修改有效
        if (!(sqlCommandType.equals(SqlCommandType.INSERT) ||
                sqlCommandType.equals(SqlCommandType.UPDATE))) {
            return invocation.proceed();
        }
        // 获取参数
        Object parameter = invocation.getArgs()[1];

        if (parameter != null) {
            fillFields(parameter, sqlCommandType);
        }

        // 继续执行后续操作
        return invocation.proceed();
    }

    private void fillFields(Object parameterObject, SqlCommandType sqlCommandType) throws IllegalAccessException {
        Class<?> parameterType = parameterObject.getClass();
        // 判断是否批量操作
        boolean isBatch = false;
        if (parameterObject instanceof Map) {
            // 批量操作
            Map<?, ?> map = (Map<?, ?>) parameterObject;
            parameterObject = map.get("param1");
            isBatch = true;
            // 是空直接返回
            if (CollUtil.isEmpty((List<?>) parameterObject)) {
                return;
            }
            // 获取List中真正存储的类型Class
            parameterType = ((List<?>) parameterObject).get(0).getClass();
        }
        // 遍历字段
        for (Field field : parameterType.getDeclaredFields()) {
            AutoFill autoFill = field.getAnnotation(AutoFill.class);
            // 确保字段上有AutoFill注解
            if (autoFill != null) {
                AutoFill.AutoFillType fillType = autoFill.type();
                boolean shouldFill = false;

                switch (fillType) {
                    case INSERT:
                        shouldFill = sqlCommandType.equals(SqlCommandType.INSERT);
                        break;
                    case UPDATE:
                        shouldFill = sqlCommandType.equals(SqlCommandType.UPDATE);
                        break;
                    case BOTH:
                        shouldFill = sqlCommandType.equals(SqlCommandType.INSERT) ||
                                sqlCommandType.equals(SqlCommandType.UPDATE);
                        break;
                    default:
                        // 处理其他未定义的AutoFillType情况
                        break;
                }

                if (shouldFill) {
                    // 进行字段填充的逻辑
                    field.setAccessible(true);
                    Object value = null;

                    switch (autoFill.fieldType()) {
                        case DATETIME:
                            // 时间戳类型，填充当前时间
                            value = new Date();
                            break;
                        case USER_ID:
                            // 用户ID类型，从ThreadLocal获取
                            value = UserContextHolder.getUserId();
                            break;
                        default:
                            // 处理其他类型或者未定义类型的情况
                            break;
                    }

                    if (value != null) {
                        if (isBatch) {
                            // 批量操作，批量填充
                            Object finalValue = value;
                            ((List<?>) parameterObject).forEach(item -> {
                                try {
                                    field.set(item, finalValue);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        } else {
                            // 单个操作，单个填充
                            field.set(parameterObject, value);
                        }
                    }
                }
            }
        }
    }


    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }
}
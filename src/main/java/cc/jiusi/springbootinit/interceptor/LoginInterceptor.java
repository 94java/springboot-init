package cc.jiusi.springbootinit.interceptor;

import cc.jiusi.springbootinit.annotation.AuthCheck;
import cc.jiusi.springbootinit.common.ErrorCode;
import cc.jiusi.springbootinit.constant.UserConstant;
import cc.jiusi.springbootinit.exception.BusinessException;
import cc.jiusi.springbootinit.service.UserService;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2024-04-28  16:54
 * @Description: 登录拦截器
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("当前请求路径：{}", request.getRequestURI());
        // 获取原方法
        if (!(handler instanceof HandlerMethod)) {
            // 请求静态或其他资源，放行
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        // 寻找注解
        AuthCheck annotation = handlerMethod.getMethodAnnotation(AuthCheck.class);
        // 方法不存在注解，则判断对应类是否存在注解
        if (annotation == null) {
            // 获取类上的注解
            annotation = handlerMethod.getBeanType().getAnnotation(AuthCheck.class);
        }
        if (annotation == null) {
            // 类和方法都不存在鉴权注解，则放行
            return true;
        }
        if (!annotation.enableCheck()) {
            // 若对应方法不需要进行权限校验，直接放行
            return true;
        }

        // ===========需要鉴权认证===========
        // 获取 token
        String token = request.getHeader(UserConstant.USER_TOKEN_HEADER);
        if (StringUtils.isBlank(token)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 解析token
        String userId = stringRedisTemplate.opsForValue().get(UserConstant.LOGIN_TOKEN + token);
        // token 无效，拦截
        if (StringUtils.isBlank(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }

        // 获取权限校验属性值（需要的角色名）
        String mustRole = annotation.mustRole();
        if (StrUtil.isEmpty(mustRole)) {
            // 未进行角色控制（登录即可），放行
            return true;
        }
        // 指定了特定角色
        String userRole = userService.getUserRole(Long.valueOf(userId));
        if (!mustRole.equals(userRole)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 放行
        return true;
    }
}
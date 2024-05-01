package cc.jiusi.springbootinit.interceptor;

import cc.jiusi.springbootinit.common.UserContextHolder;
import cc.jiusi.springbootinit.constant.UserConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2024-04-28  16:54
 * @Description: token 刷新拦截器
 */
@Component
public class FreshInterceptor implements HandlerInterceptor {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(UserConstant.USER_TOKEN_HEADER);
        if (StringUtils.isNotBlank(token)) {
            // token 不为空
            String userId = stringRedisTemplate.opsForValue().get(UserConstant.LOGIN_TOKEN + token);
            if (StringUtils.isNotBlank(userId)) {
                // 存在 token
                UserContextHolder.setUserId(Long.valueOf(userId));
                // 刷新过期时间 (30 minutes)
                stringRedisTemplate.expire(UserConstant.LOGIN_TOKEN + token, UserConstant.LOGIN_TOKEN_EXPIRE, TimeUnit.MINUTES);
            }
        }
        // 放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清除用户信息，防止内存溢出
        UserContextHolder.clear();
    }
}
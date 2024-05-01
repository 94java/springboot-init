package cc.jiusi.springbootinit.constant;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2024-04-28  16:54
 * @Description: 用户常量
 */
public interface UserConstant {

    //  region 登录注册

    /**
     * 用户登录态键
     */
    String LOGIN_TOKEN = "user:login:token:";

    /**
     * 用户快捷登录验证码
     */
    String LOGIN_CODE = "user:login:code:";

    /**
     * token 默认过期时间（单位 分钟）
     */
    Long LOGIN_TOKEN_EXPIRE = 30L;

    /**
     * token 请求头 key 值
     */
    String USER_TOKEN_HEADER = "Token";

    // endregion


    //  region 权限

    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";


    // endregion
}

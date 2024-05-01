package cc.jiusi.springbootinit.model.dto.user;

import java.io.Serializable;
import java.util.Date;

import cc.jiusi.springbootinit.annotation.AutoId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2024-04-28  16:54
 * @Description: 用户登录请求（除了账号密码，还支持邮箱/手机号快捷登录）
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = -25606291877059322L;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;
    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "邮箱/手机验证码")
    private String code;
}

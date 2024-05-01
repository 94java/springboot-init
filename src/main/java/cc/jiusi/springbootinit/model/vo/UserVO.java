package cc.jiusi.springbootinit.model.vo;

import java.io.Serializable;
import java.util.Date;

import cc.jiusi.springbootinit.annotation.AutoFill;
import cc.jiusi.springbootinit.annotation.AutoId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2024-04-28  16:54
 * @Description: 用户视图（脱敏）-管理端使用
 */
@Data
public class UserVO implements Serializable {

    private static final long serialVersionUID = -25606291877059322L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickname;
    /**
     * 年龄
     */
    @ApiModelProperty(value = "年龄")
    private Integer age;
    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private String sex;
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
    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String avatar;
    /**
     * 签名
     */
    @ApiModelProperty(value = "签名")
    private String sign;
    /**
     * 用户状态（0-禁用，1-正常）
     */
    @ApiModelProperty(value = "用户状态（0-禁用，1-正常）")
    private String status;
    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    private String province;
    /**
     * 城市
     */
    @ApiModelProperty(value = "城市")
    private String city;
    /**
     * 访客数量（冗余设计）
     */
    @ApiModelProperty(value = "访客数量（冗余设计）")
    private Long visitorCount;
    /**
     * 最后登录时间
     */
    @ApiModelProperty(value = "最后登录时间")
    private Date lastLoginTime;
    /**
     * 最后登录ip
     */
    @ApiModelProperty(value = "最后登录ip")
    private String lastLoginIp;
    /**
     * 创建者id
     */
    @ApiModelProperty(value = "创建者id")
    private Long createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 修改者id
     */
    @ApiModelProperty(value = "修改者id")
    private Long updateBy;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
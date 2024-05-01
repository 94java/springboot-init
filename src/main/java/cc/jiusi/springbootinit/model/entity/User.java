package cc.jiusi.springbootinit.model.entity;

import java.util.Date;
import java.io.Serializable;

import cc.jiusi.springbootinit.annotation.AutoFill;
import cc.jiusi.springbootinit.annotation.AutoId;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2024-04-29 20:39:07
 * @Description: 用户信息(User)实体类
 */
@Data
@ApiModel(value = "User", description = "用户信息")
public class User implements Serializable {
    private static final long serialVersionUID = -25606291877059322L;
    /**
     * 主键
     */
    @AutoId
    @ApiModelProperty(value = "主键")
    private Long id;
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
    @AutoFill(type = AutoFill.AutoFillType.INSERT, fieldType = AutoFill.FieldType.USER_ID)
    private Long createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @AutoFill(type = AutoFill.AutoFillType.INSERT)
    private Date createTime;
    /**
     * 修改者id
     */
    @AutoFill(fieldType = AutoFill.FieldType.USER_ID)
    @ApiModelProperty(value = "修改者id")
    private Long updateBy;
    /**
     * 修改时间
     */
    @AutoFill
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    /**
     * 逻辑删除标记
     */
    @ApiModelProperty(value = "逻辑删除标记")
    private String delFlag;
}


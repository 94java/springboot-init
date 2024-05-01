package cc.jiusi.springbootinit.service.impl;

import cc.jiusi.springbootinit.common.DeleteRequest;
import cc.jiusi.springbootinit.common.ErrorCode;
import cc.jiusi.springbootinit.constant.UserConstant;
import cc.jiusi.springbootinit.exception.BusinessException;
import cc.jiusi.springbootinit.model.dto.user.UserAddRequest;
import cc.jiusi.springbootinit.model.dto.user.UserQueryRequest;
import cc.jiusi.springbootinit.model.dto.user.UserRegisterRequest;
import cc.jiusi.springbootinit.model.dto.user.UserUpdateRequest;
import cc.jiusi.springbootinit.model.entity.User;
import cc.jiusi.springbootinit.mapper.UserMapper;
import cc.jiusi.springbootinit.service.UserService;
import cc.jiusi.springbootinit.utils.NetUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cn.hutool.core.bean.BeanUtil;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2024-04-29 20:39:07
 * @Description: 用户信息(User)表服务实现类
 */
@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // region 登录注册

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "jiusi66";

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求对象
     */
    @Override
    public void register(UserRegisterRequest userRegisterRequest) {
        // 查询用户名是否存在
        String username = userRegisterRequest.getUsername();
        User userQuery = new User();
        userQuery.setUsername(username);
        List<User> users = userMapper.selectAll(userQuery);
        if (CollUtil.isNotEmpty(users)) {
            // 用户名已存在
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名已存在");
        }
        // 用户名不存在，则进行注册
        // 密码加密存储
        String password = userRegisterRequest.getPassword();
        userRegisterRequest.setPassword(DigestUtil.md5Hex(SALT + password));
        // 插入数据
        User user = BeanUtil.copyProperties(userRegisterRequest, User.class);
        userMapper.insert(user);
    }

    /**
     * 获取用户角色
     *
     * @param id 用户id
     */
    @Override
    public String getUserRole(Long id) {
        // 模拟数据（默认为 User 用户）
        // todo: 真实情况可从数据库查询
        return UserConstant.DEFAULT_ROLE;
    }

    /**
     * 根据邮箱快捷登录
     *
     * @param email 邮箱
     * @param code  验证码
     */
    @Override
    public String loginByEmail(HttpServletRequest request, String email, String code) {
        // 判断验证码是否正确
        String codeRedis = stringRedisTemplate.opsForValue().get(UserConstant.LOGIN_CODE + email);
        if (!StrUtil.equals(codeRedis, code)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码不正确");
        }
        // 查询邮箱是否存在
        User user = new User();
        user.setEmail(email);
        List<User> users = userMapper.selectAll(user);
        if (CollUtil.isNotEmpty(users)) {
            // 存在用户，直接登录
            // 因为 username 唯一，所以查到的一定是只有一个
            user = users.get(0);
        } else {
            // 用户不存在，自动注册
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername("email_" + RandomUtil.randomNumbers(10));
            newUser.setPassword(DigestUtil.md5Hex(SALT + RandomUtil.randomString(10)));
            userMapper.insert(newUser);
            user = newUser;
        }
        // 生成 token（Redis存储UUID-用户id）
        String token = UUID.randomUUID().toString(true);
        stringRedisTemplate.opsForValue().set(UserConstant.LOGIN_TOKEN + token, user.getId().toString());
        // token 默认 30 分钟过期时间
        stringRedisTemplate.expire(UserConstant.LOGIN_TOKEN + token, UserConstant.LOGIN_TOKEN_EXPIRE, TimeUnit.MINUTES);

        // 更新用户登录记录
        String ip = NetUtils.getIpAddress(request);
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setLastLoginIp(ip);
        updateUser.setLastLoginTime(new Date());
        // 更新
        userMapper.update(user);
        return token;
    }

    /**
     * 发送邮箱验证码
     *
     * @param email 邮箱
     */
    @Override
    public void sendEmailCode(String email) {
        // 生成验证码
        String code = RandomUtil.randomNumbers(4);
        // todo: 如果项目中用到了快捷登录，此处可以自行替换真实发送逻辑
        // ....

        // 存入Redis
        stringRedisTemplate.opsForValue().set(UserConstant.LOGIN_CODE + email, code);
        log.info("邮箱验证码为：{}", code);
    }

    /**
     * 通过账号密码登录
     *
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    @Override
    public String login(HttpServletRequest request, String username, String password) {
        // 密码加密
        String encryptPassword = DigestUtil.md5Hex(SALT + password);
        // 构建查询对象
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptPassword);
        // 根据用户名密码查询
        List<User> users = userMapper.selectAll(user);
        if (CollUtil.isEmpty(users)) {
            // 用户未查到---账号或密码不正确
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 因为 username 唯一，所以查到的一定是只有一个
        user = users.get(0);
        // 生成 token（Redis存储UUID-用户id）
        String token = UUID.randomUUID().toString(true);
        stringRedisTemplate.opsForValue().set(UserConstant.LOGIN_TOKEN + token, user.getId().toString());
        // token 默认 30 分钟过期时间
        stringRedisTemplate.expire(UserConstant.LOGIN_TOKEN + token, UserConstant.LOGIN_TOKEN_EXPIRE, TimeUnit.MINUTES);

        // 更新用户登录记录
        String ip = NetUtils.getIpAddress(request);
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setLastLoginIp(ip);
        updateUser.setLastLoginTime(new Date());
        // 更新
        userMapper.update(user);

        return token;
    }
    // endregion

    // region 增删改查

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public User queryById(Long id) {
        return userMapper.selectById(id);
    }

    /**
     * 通过条件查询所有数据
     *
     * @param userQueryRequest 查询条件
     * @return List<User> 实例对象列表
     */
    @Override
    public List<User> queryAll(UserQueryRequest userQueryRequest) {
        User user = BeanUtil.copyProperties(userQueryRequest, User.class);
        return userMapper.selectAll(user);
    }

    /**
     * 通过条件查询分页数据
     *
     * @param userQueryRequest 查询条件
     * @return PageInfo<User> 分页信息对象
     */
    @Override
    public PageInfo<User> queryPage(UserQueryRequest userQueryRequest) {
        int pageNum = userQueryRequest.getPageNum();
        int pageSize = userQueryRequest.getPageSize();
        PageHelper.startPage(pageNum, pageSize);
        User user = BeanUtil.copyProperties(userQueryRequest, User.class);
        List<User> users = userMapper.selectAll(user);
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return pageInfo;
    }

    /**
     * 根据条件统计总行数
     *
     * @param userQueryRequest 查询条件
     * @return 总行数
     */
    @Override
    public long queryCount(UserQueryRequest userQueryRequest) {
        User user = BeanUtil.copyProperties(userQueryRequest, User.class);
        return userMapper.count(user);
    }

    /**
     * 新增数据
     *
     * @param userAddRequest 实例对象
     * @return 实例对象
     */
    @Override
    public User insert(UserAddRequest userAddRequest) {
        User user = BeanUtil.copyProperties(userAddRequest, User.class);
        userMapper.insert(user);
        return user;
    }

    /**
     * 批量新增数据
     *
     * @param entities List<UserAddRequest> 实例对象列表
     * @return 影响行数
     */
    @Override
    public int insertBatch(List<UserAddRequest> entities) {
        List<User> users = entities.stream()
                .map(item -> BeanUtil.copyProperties(item, User.class))
                .collect(Collectors.toList());
        return userMapper.insertBatch(users);
    }

    /**
     * 修改数据
     *
     * @param userUpdateRequest 实例对象
     * @return 实例对象
     */
    @Override
    public User update(UserUpdateRequest userUpdateRequest) {
        User user = BeanUtil.copyProperties(userUpdateRequest, User.class);
        userMapper.update(user);
        return queryById(user.getId());
    }

    /**
     * 通过主键集合批量删除数据
     *
     * @param deleteRequest 删除请求对象
     * @return 影响行数
     */
    @Override
    public int deleteBatchByIds(DeleteRequest deleteRequest) {
        return userMapper.deleteBatchByIds(deleteRequest.getIds());
    }
    // endregion
}


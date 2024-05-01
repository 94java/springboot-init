# SpringBoot 项目初始模板
> 本模板参考自 鱼皮 项目初始化模板

基于 Java SpringBoot 的项目初始模板，整合了常用框架和主流业务的示例代码。

基于EasyCode-MybatisCodeHelper（IDEA插件）提供了MyBatis代码生成器模板，可以和本模板完美结合。
插件具体使用方式自行百度。

## 模板特点

### 主流框架 & 特性

- Spring Boot 2.7.x
- Spring MVC
- MyBatis 数据访问
- Spring Boot 调试工具和项目处理器
- Spring AOP 切面编程
- Spring 事务注解

### 数据存储

- MySQL 数据库
- Redis 内存数据库

### 工具类

- Hutool 工具库
- Lombok 注解
- 集成代码MyBatis代码生成模板（与本项目配套使用）---配合EasyCode-MybatisCodeHelper（IDEA插件）

### 业务特性

- 全局请求响应拦截器（记录日志）
- 全局异常处理器
- 自定义错误码
- 封装通用响应类
- Swagger + Knife4j 接口文档
- 自定义权限注解 + 全局校验
- 全局跨域处理
- 长整数丢失精度解决（JSON转换时Long->String）
- 多环境配置
- 自定义注解 + MyBatis 插件 实现 ID 自动生成（批量新增时需要注意，方法形参为 entities）
- 自定义注解 + MyBatis 插件 实现 MyBatis 下公共字段的自动填充（可选Insert、Update和Both）

## 业务功能

- 提供示例 SQL（用户表）
- 用户信息相关的增删改查操作（代码生成器生成）

### 单元测试

- JUnit 单元测试
- 示例单元测试类

## 快速上手

> 所有需要修改的地方都标记了 `todo`，便于大家找到修改的位置(可在IDEA快速定位)
- 项目默认端口：`8866`
- 项目默认context路径：`/api`

启动后端后可以访问 knif4j 接口文档进行测试：http://localhost:8866/api/doc.html

## 后续计划（欢迎大家贡献代码）
1. 对代码生成器进行优化，脱离 IDEA插件，自行实现一个 IDEA插件或者采用【命令行】形式快速生成代码
2. 自定义 MyBatis 插件，实现逻辑删除功能
3. ......
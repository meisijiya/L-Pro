## 项目介绍

这是一个基于Spring Boot开发的用户管理系统，具有完整的用户注册、登录、权限管理等功能。

### 项目结构

```java
com.ljh.UserSystem
├── annotation          // 自定义注解
├── aspect              // AOP切面
├── common              // 通用类（响应、错误码等）
├── config              // 配置类
│   ├── Interceptor     // 拦截器
│   ├── Listener        // 监听器
│   └── properties      // 配置属性
├── constant            // 常量定义
├── controller          // 控制器层
├── exception           // 异常处理
├── mapper              // 数据访问层
├── module              // 模块定义
│   ├── domain          // 实体类
│   ├── dto             // 数据传输对象
│   └── request         // 请求对象
├── service             // 业务逻辑层
│   └── impl            // 业务实现
└── utils               // 工具类
```


### 核心功能

1. **用户认证**
   - 用户注册/登录
   - 邮箱验证码验证
   - Session状态管理

2. **用户管理**
   - 用户信息查询
   - 用户列表分页查询
   - 用户信息更新
   - 用户注销/删除

3. **安全机制**
   - 密码加密存储
   - 登录状态拦截器
   - 注册验证码拦截器
   - CORS跨域配置

4. **技术特性**
   - MyBatis-Plus数据库操作
   - Redis缓存支持
   - Swagger API文档
   - 统一响应格式
   - 全局异常处理

### 主要技术栈

- **后端框架**: Spring Boot 3.x
- **数据库**: MySQL + MyBatis-Plus
- **API文档**: Swagger/OpenAPI
- **工具类**: Hutool, Lombok
- **邮件服务**: Spring Mail
- **构建工具**: Maven

这是一个功能完整、结构清晰的用户管理系统，适合作为学习Spring Boot开发的参考项目。

package com.ljh.UserSystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljh.UserSystem.common.ErrorCode;
import com.ljh.UserSystem.exception.BusinessException;
import com.ljh.UserSystem.module.domain.User;
import com.ljh.UserSystem.module.request.DeleteRequest;
import com.ljh.UserSystem.service.UserService;
import com.ljh.UserSystem.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ljh.UserSystem.constant.PWConstant.SALT;
import static com.ljh.UserSystem.constant.UserConstant.*;

/**
* @author 22923
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-10-18 15:35:25
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Resource
    private UserMapper userMapper;

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名或密码为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名长度不能少于4位");
        }
        // 校验密码格式：必须包含大小写字母和特殊字符
        if (!isValidPassword(userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码格式不正确");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名不能包含特殊字符");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在，同时排除注销的用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount)
                    .eq("password", encryptPassword)
                    .eq("is_delete", USER_NORMAL);
        User user = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("用户登录失败，查询数据库为空");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在","用户没在数据库||用户已注销||密码错误");
        }
        // 3. 用户脱敏
        User safetyUser = getSafetyUser(user);
        // 4. 记录用户的登录态


        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        // 注册Session到全局映射
        ServletContext context = request.getServletContext();
        if (context.getAttribute("userSessions") == null) {
            context.setAttribute("userSessions", new ConcurrentHashMap<String, HttpSession>());
        }
        @SuppressWarnings("unchecked")
        Map<String, HttpSession> userSessions = (Map<String, HttpSession>) context.getAttribute("userSessions");
        userSessions.put(userAccount, request.getSession());

        //打印Session管理器中的Session信息
        log.info("Session管理器中的Session信息：{}", userSessions);

        return safetyUser;
    }


    /**
     * 校验密码是否符合要求：包含大写字母、小写字母和特殊字符
     * @param password 密码
     * @return 是否符合要求
     */
    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (!Character.isDigit(c)) {
                // 非数字非字母的字符视为特殊字符
                hasSpecialChar = true;
            }
        }

        return hasUpperCase && hasLowerCase && hasSpecialChar;
    }

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUser_account(originUser.getUser_account());
        safetyUser.setAvatar_url(originUser.getAvatar_url());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setRole(originUser.getRole());
        safetyUser.setStatus(originUser.getStatus());
        safetyUser.setCreate_time(originUser.getCreate_time());
        return safetyUser;
    }

    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名或密码为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名长度不能少于4位");
        }
        // 校验密码格式：必须包含大小写字母和特殊字符
        if (!isValidPassword(userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码过于简单");
        }
        if(!checkPassword.equals(userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不一致");
        }
        // 账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名不能包含特殊字符");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("password", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户已存在
        if (user != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户已存在");
        }
        // 3. 插入数据
        User NewUser = new User();
        NewUser.setUser_account(userAccount);
        NewUser.setPassword(encryptPassword);
        boolean saveResult = this.save(NewUser);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        return NewUser.getId();
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 获取Session中获取用户数据
        HttpSession session = request.getSession();
        Object user = session.getAttribute(USER_LOGIN_STATE);
        boolean isLogin= (user!= null);
        // 判断用户是否登录
        if (!isLogin) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户未登录");
        }
        // 移除登录态
        session.removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public User getCurrentUser(HttpServletRequest request) {
        // 获取Session中获取用户数据
        HttpSession session = request.getSession();
        Object user = session.getAttribute(USER_LOGIN_STATE);
        boolean isLogin= user!= null;
        // 判断用户是否登录
        if (!isLogin) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户未登录");
        }
        return (User) user;
    }

    @Override
    public Page<User> getUserList(String userAccount, long current, long size) {
        Page<User> page = new Page<>(current, size);
        if (StringUtils.isBlank(userAccount)) {  // 使用 StringUtils.isBlank 更严谨
            return this.page(page);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("user_account", userAccount.trim());  // trim 去除首尾空格
        return this.page(page, queryWrapper);
    }

    @Override
    public int delete(String userAccount, HttpServletRequest request) {
        // 在一个全局可访问的地方存储Session映射
        ServletContext context = request.getServletContext();
        if (context.getAttribute("userSessions") == null) {
            context.setAttribute("userSessions", new ConcurrentHashMap<String, HttpSession>());
        }
        @SuppressWarnings("unchecked")
        Map<String, HttpSession> userSessions = (Map<String, HttpSession>) context.getAttribute("userSessions");
        HttpSession session ;
        //注销自己
        if(userAccount == null){
            session = request.getSession();
            Object object_user=session.getAttribute(USER_LOGIN_STATE);
            userLogout(request);
            User user=(User) object_user;
            long userId=user.getId();
            UpdateWrapper<User> updateWrapper=new UpdateWrapper<>();
            updateWrapper.eq("id",userId)
                            .set("is_delete",USER_DELETE);
            return userMapper.update(updateWrapper);
        }else {;
            // 获取特定用户的Session
            HttpSession targetSession = userSessions.get(userAccount);
            if (targetSession != null) {
                targetSession.removeAttribute(USER_LOGIN_STATE);
            }
            //打印Session管理器中的Session信息
            log.info("Session管理器中的Session信息：{}", userSessions);
            //数据库逻辑删除
            UpdateWrapper<User> updateWrapper=new UpdateWrapper<>();
            updateWrapper.eq("user_account",userAccount)
                            .set("is_delete",USER_DELETE);
            return userMapper.update(updateWrapper);
        }
    }

    @Override
    public int update(User user, HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        User userLogin = (User) attribute;
        //将有值的字段更新
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", userLogin.getId());

        setIfNotNull(updateWrapper, "username", user.getUsername());
        setIfNotNull(updateWrapper, "gender", user.getGender());
        setIfNotNull(updateWrapper, "phone", user.getPhone());
        setIfNotNull(updateWrapper, "email", user.getEmail());
        setIfNotNull(updateWrapper, "avatar_url", user.getAvatar_url());
        setIfNotNull(updateWrapper, "tags", user.getTags());
        //数据库取数据失败
        int updated = userMapper.update(updateWrapper);
        if(updated ==0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败，数据库错误");
        }
        return updated;
    }

    /**
     * 如果值不为null，则设置更新条件
     *
     * @param wrapper 更新包装器，用于构建更新条件
     * @param column 要更新的列名
     * @param value 要设置的值，如果为值为 null - 返回 false
     *                        值为 空字符串 "" - 返回 false
     *                        值为 空白字符（如 " ", "\t", "\n" 等）- 返回 false
     */
    private void setIfNotNull(UpdateWrapper<User> wrapper, String column, Object value) {
        if (StringUtils.isNotBlank((String)value)) {
            wrapper.set(column, value);
        }
    }

}





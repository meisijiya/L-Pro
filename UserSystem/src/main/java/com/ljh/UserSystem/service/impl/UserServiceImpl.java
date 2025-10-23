package com.ljh.UserSystem.service.impl;

import cn.hutool.Hutool;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljh.UserSystem.common.ErrorCode;
import com.ljh.UserSystem.exception.BusinessException;
import com.ljh.UserSystem.module.domain.User;
import com.ljh.UserSystem.module.dto.UserDTO;
import com.ljh.UserSystem.module.request.DeleteRequest;
import com.ljh.UserSystem.service.UserService;
import com.ljh.UserSystem.mapper.UserMapper;
import com.ljh.UserSystem.utils.UserHolder;
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
import java.util.stream.Collectors;

import static com.ljh.UserSystem.constant.EmailConstant.EMAIL_CODE;
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
    public UserDTO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
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
        UserDTO userDTO =BeanUtil.copyProperties(user, UserDTO.class);
        // 4. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, userDTO);

        // 注册该请求的用户Session到全局映射
        registerUserToSessions(userAccount, request);

        return userDTO;
    }

    private void registerUserToSessions(String userAccount, HttpServletRequest request) {
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


    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword,String email,String code,HttpServletRequest request) {
        String emailCode = (String) request.getSession().getAttribute(EMAIL_CODE);
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
        if(StringUtils.isAnyBlank(email,code)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱或验证码为空");
        }
        if(!email.contains("@")){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式错误");
        }
        if(!code.equals(emailCode)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误");
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
        NewUser.setEmail(email);
        boolean saveResult = this.save(NewUser);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        return NewUser.getId();
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        request.getSession().invalidate();
        return true;
    }

    @Override
    public UserDTO getCurrentUser(HttpServletRequest request) {
        // 通过线程获取Session中获取用户数据
        return UserHolder.getUser();
    }

    @Override
    public Page<UserDTO> getUserList(String userAccount, long current, long size) {
        //Page<T> 的泛型类型必须与数据库实体类对应
        Page<User> page = new Page<>(current, size);
        Page<User> resultPage;
        Page<UserDTO> dtoPage = new Page<>(current, size);
        if(userAccount == null){
            resultPage=this.page(page);
        }else {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("user_account", userAccount.trim());// trim 去除首尾空格
            resultPage = this.page(page, queryWrapper);
        }
        ToDTO(resultPage, dtoPage);
        return dtoPage;
    }

    private void ToDTO(Page<User> page, Page<UserDTO> dtoPage) {
        Page<User> resultPage;
        resultPage=this.page(page);
        // 再转换为 UserDTO
        // 复制分页信息
        dtoPage.setRecords(resultPage.getRecords().stream()
                .map(user -> BeanUtil.copyProperties(user, UserDTO.class))
                .collect(Collectors.toList()));
        dtoPage.setTotal(resultPage.getTotal());
        dtoPage.setSize(resultPage.getSize());
        dtoPage.setCurrent(resultPage.getCurrent());
    }

    @Override
    public int userDelete(String userAccount, HttpServletRequest request) {
        //注销自己
        if(userAccount == null){
            //获取用户id，如何逻辑删除用户
            long userId = UserHolder.getUser().getId();
            //登出并销毁Session
            userLogout(request);
            UpdateWrapper<User> updateWrapper=new UpdateWrapper<>();
            updateWrapper.eq("id",userId)
                            .set("is_delete",USER_DELETE);
            return userMapper.update(updateWrapper);
        }else {
            // 获取Session管理器中的Session信息
            ServletContext context = request.getServletContext();
            @SuppressWarnings("unchecked")
            Map<String, HttpSession> userSessions =
                    (Map<String, HttpSession>) context.getAttribute("userSessions");
            // 获取特定用户的Session
            HttpSession targetSession = userSessions.get(userAccount);
            if (targetSession != null) {
                // 销毁Session
                targetSession.invalidate();
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
    public int userInfoUpdate(UserDTO user, HttpServletRequest request) {
        //将有值的字段更新
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", UserHolder.getUser().getId());
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
        //更新成功后，也要Session中保存数据
        User selectById = userMapper.selectById(UserHolder.getUser().getId());
        HttpSession session = request.getSession();
        session.setAttribute(USER_LOGIN_STATE, selectById);
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





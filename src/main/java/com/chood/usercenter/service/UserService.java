package com.chood.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chood.usercenter.model.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author chood
 * <p>
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 确认密码
     * @return 注册成功返回用户id，失败返回 0
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @return 登录成功返回用户信息，失败返回 null
     */
    User doLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 根据用户名来搜索用户
     *
     * @param username 用户名
     * @return 用户列表
     */
    List<User> searchUsers(String username);

    /**
     * 用户信息脱敏
     * @param originUser 原始用户信息
     * @return 脱敏后的用户信息
     */
    User getSafetyUser(User originUser);
}

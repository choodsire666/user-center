package com.chood.usercenter.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chood.usercenter.common.Result;
import com.chood.usercenter.model.entity.User;
import com.chood.usercenter.model.request.FindMethodRequest;
import com.chood.usercenter.model.request.ResetPasswordRequest;
import com.chood.usercenter.model.request.SearchUsersQuery;
import com.chood.usercenter.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;

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
    Result<Long> userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @return 登录成功返回用户信息，失败返回 null
     */
    Result<String> doLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 搜索用户
     */
    Result<IPage<UserVO>> searchUsers(SearchUsersQuery searchUsersQuery);

    /**
     * 用户信息脱敏
     *
     * @param originUser 原始用户信息
     * @return 脱敏后的用户信息
     */
    UserVO getSafetyUser(User originUser);

    /**
     * 用户注销
     */
    void userLogout(HttpServletRequest request);

    /**
     * 根据找回方式找回用户id
     *
     * @param findMethodRequest 请求实体
     * @return 用户id
     */
    Result<Long> findMethod(FindMethodRequest findMethodRequest);

    /**
     * 重置密码
     *
     * @param resetPasswordRequest 重置密码请求实体
     */
    Result<String> resetPassword(ResetPasswordRequest resetPasswordRequest);
}

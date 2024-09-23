package com.chood.usercenter.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chood.usercenter.common.Result;
import com.chood.usercenter.common.ResultCode;
import com.chood.usercenter.constant.consist.UserConsist;
import com.chood.usercenter.constant.enums.FindMethodEnums;
import com.chood.usercenter.constant.enums.UserEnums;
import com.chood.usercenter.exception.BusinessException;
import com.chood.usercenter.mapper.UserMapper;
import com.chood.usercenter.model.entity.User;
import com.chood.usercenter.model.request.FindMethodRequest;
import com.chood.usercenter.model.request.ResetPasswordRequest;
import com.chood.usercenter.model.request.SearchUsersQuery;
import com.chood.usercenter.model.vo.UserVO;
import com.chood.usercenter.service.CaptchService;
import com.chood.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author chood
 * <p>
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource(name = "numberCaptchService")
    private CaptchService numberCaptchService;

    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 确认密码
     */
    @Override
    public Result<Void> userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "参数为空!");
        }

        // 账号不小于4位
        if (userAccount.length() < 4) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账号小于4位!");
        }

        // 密码不小于8位
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "密码小于4位!");

        }

        // 账号不能包含特殊字符,正则判断
        boolean match = ReUtil.contains("[^a-zA-Z0-9]", userAccount);
        if (match) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账号包含特殊字符!");
        }

        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "两次输入密码不一致!");
        }

        // 账户不能重复
        Long count = this.lambdaQuery().eq(User::getUserAccount, userAccount).count();
        if (count > 0) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账号已被注册!");
        }

        // 2.加密
        // 生成盐
        String salt = UUID.randomUUID().toString().replaceAll("-", "");
        String encryptPassword = DigestUtil.md5Hex(salt + userPassword);

        // 3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setSalt(salt);

        boolean save = save(user);

        if (!save) {
            throw new BusinessException(ResultCode.ERROR, "注册失败!");
        }

        return Result.success("注册成功!");
    }

    @Override
    public Result<Void> doLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账号或密码为空!");
        }

        // 账号不小于4位
        if (userAccount.length() < 4) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账号小于4位!");
        }

        // 密码不小于8位
        if (userPassword.length() < 8) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "密码小于8位!");
        }

        // 账号不能包含特殊字符,正则判断
        boolean match = ReUtil.contains("[^a-zA-Z0-9]", userAccount);
        if (match) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账号包含特殊字符!");
        }

        User user = baseMapper.selectOne(lambdaQuery().eq(User::getUserAccount, userAccount).getWrapper());

        // 用户不存在
        if (user == null) {
            log.info("user login failed, {} account not found", userAccount);
            throw new BusinessException(ResultCode.PARAMS_ERROR, "用户不存在!");
        }

        // 2.加密
        String encryptPassword = DigestUtil.md5Hex(user.getSalt() + userPassword);

        // 3.判断密码是否正确
        if (!user.getUserPassword().equals(encryptPassword)) {
            // 密码错误
            log.info("user login failed, userPassword can not match user:{}", userAccount);
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账号或密码不正确!");
        }

        // 判断用户是否被禁用
        if (UserEnums.ACCOUNT_BAN.getCode().equals(user.getStatus())) {
            // 用户被禁用
            throw new BusinessException(ResultCode.USER_INVALID);
        }

        // 3.脱敏
        UserVO safetyUser = getSafetyUser(user);

        // 4.记录用户登录状态
        request.getSession().setAttribute(UserConsist.USER_LOGIN_STATE, safetyUser);

        return Result.success("登录成功!");
    }

    @Override
    public void userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(UserConsist.USER_LOGIN_STATE);
    }

    /**
     * 根据用户名来搜索用户
     *
     * @param searchUsersQuery 搜索用户Query
     * @return 用户列表
     */
    @Override
    public Result<IPage<UserVO>> searchUsers(SearchUsersQuery searchUsersQuery) {
        // 校验分页参数
        searchUsersQuery.valid();
        Page<User> page = baseMapper.selectPage(new Page<>(searchUsersQuery.getCurrent(), searchUsersQuery.getPageSize()), lambdaQuery()
                .like(StringUtils.isNotBlank(searchUsersQuery.getUserAccount()), User::getUserAccount, searchUsersQuery.getUserAccount())
                .like(StringUtils.isNotBlank(searchUsersQuery.getUsername()), User::getUsername, searchUsersQuery.getUsername())
                .eq(ObjectUtil.isNotEmpty(searchUsersQuery.getGender()), User::getGender, searchUsersQuery.getGender())
                .eq(ObjectUtil.isNotEmpty(searchUsersQuery.getStatus()), User::getStatus, searchUsersQuery.getStatus())
                .eq(ObjectUtil.isNotEmpty(searchUsersQuery.getRole()), User::getRole, searchUsersQuery.getRole())
                .orderBy(StringUtils.isNotBlank(searchUsersQuery.getCreateTime()), "ascend".equals(searchUsersQuery.getCreateTime()), User::getCreateTime)
                .getWrapper());

        Page<UserVO> userVOPage = new Page<>();

        userVOPage.setRecords(page.getRecords().stream()
                .map(this::getSafetyUser)
                .collect(Collectors.toList()));

        return Result.success(userVOPage);
    }

    /**
     * 用户信息脱敏
     *
     * @param originUser 原始用户信息
     * @return 脱敏后的用户信息
     */
    @Override
    public UserVO getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(originUser, userVO);
        return userVO;
    }

    /**
     * 根据找回方式查找用户id
     *
     * @param findMethodRequest 请求实体
     * @return 用户id
     */
    @Override
    public Result<Long> findMethod(FindMethodRequest findMethodRequest) {
        if (ObjectUtil.isEmpty(findMethodRequest)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }

        String type = findMethodRequest.getType();
        if (StringUtils.isBlank(type)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "找回类型不能为空！");
        }

        if (FindMethodEnums.PHONE.getType().equals(type)) {
            String phone = findMethodRequest.getPhone();
            if (StringUtils.isBlank(phone)) {
                throw new BusinessException(ResultCode.PARAMS_ERROR, "找回的手机号不能为空！");
            }

            // 正则表达式, 校验是否为合法手机号
            boolean match = ReUtil.isMatch("^1[3-9]\\d{9}$", phone);
            if (!match) {
                throw new BusinessException(ResultCode.PARAMS_ERROR, "找回的手机号格式不正确！");
            }

            // 根据手机号查找用户id
            User user = getOne(Wrappers.<User>lambdaQuery().eq(User::getPhone, phone));
            if (user == null) {
                return Result.error(ResultCode.USER_NOT_EXIST, "用户不存在或未绑定手机号码！");
            }

            return Result.success(user.getId());
        } else if (FindMethodEnums.EMAIL.getType().equals(type)) {
            String email = findMethodRequest.getEmail();
            if (StringUtils.isBlank(email)) {
                throw new BusinessException(ResultCode.PARAMS_ERROR, "找回的邮箱不能为空！");
            }

            // 正则表达式, 校验是否为合法邮箱
            boolean match = ReUtil.isMatch("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", email);
            if (!match) {
                throw new BusinessException(ResultCode.PARAMS_ERROR, "找回的邮箱格式不正确！");
            }

            // 根据邮箱查找用户id
            User user = getOne(Wrappers.<User>lambdaQuery().eq(User::getEmail, email));
            if (user == null) {
                return Result.error(ResultCode.USER_NOT_EXIST, "用户不存在或未绑定邮箱！");
            }

            return Result.success(user.getId());
        }

        return Result.error(ResultCode.PARAMS_ERROR, "找回类型不正确！");
    }

    /**
     * 重置密码
     *
     * @param resetPasswordRequest 重置密码请求实体
     */
    @Override
    public Result<Void> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        if (ObjectUtil.isEmpty(resetPasswordRequest)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }

        Long userId = resetPasswordRequest.getUserId();

        if (ObjectUtil.isEmpty(userId)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }

        String newPassword = resetPasswordRequest.getNewPassword();
        String checkPassword = resetPasswordRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(newPassword, checkPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }

        if (newPassword.length() < 8) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "新密码长度不能小于8位！");
        }

        if (!newPassword.equals(checkPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "两次输入的密码不一致！");
        }

        // 获取用户
        User user = baseMapper.selectById(userId);
        if (ObjectUtil.isEmpty(user)) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST, "用户不存在！");
        }

        String salt = user.getSalt();
        String password = DigestUtil.md5Hex(salt + newPassword);
        user.setUserPassword(password);

        boolean update = updateById(user);
        if (!update) {
            throw new BusinessException(ResultCode.ERROR, "密码重置失败！");
        }

        return Result.success(null, "密码重置成功！");
    }
}

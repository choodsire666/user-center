package com.chood.usercenter.service.impl;

import cn.hutool.core.util.ReUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chood.usercenter.constant.consist.UserConsist;
import com.chood.usercenter.mapper.UserMapper;
import com.chood.usercenter.model.entity.User;
import com.chood.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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

    /**
     * 用户注册
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 确认密码
     * @return 用户id, 失败返回 -1
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            // TODO: 自定义异常
            return -1;
        }

        // 账号不小于4位
        if (userAccount.length() < 4) {
            return -1;
        }

        // 密码不小于8位
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }

        // 账号不能包含特殊字符,正则判断
        boolean match = ReUtil.contains("[^a-zA-Z0-9]", userAccount);
        if (match) {
            return -1;
        }

        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }

        // 账户不能重复
        Long count = this.lambdaQuery().eq(User::getUserAccount, userAccount).count();
        if (count > 0) {
            return -1;
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

        save(user);

        return user.getId();
    }

    @Override
    public User doLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            // TODO: 自定义异常
            return null;
        }

        // 账号不小于4位
        if (userAccount.length() < 4) {
            return null;
        }

        // 密码不小于8位
        if (userPassword.length() < 8) {
            return null;
        }

        // 账号不能包含特殊字符,正则判断
        boolean match = ReUtil.contains("[^a-zA-Z0-9]", userAccount);
        if (match) {
            return null;
        }

        User user = baseMapper.selectOne(lambdaQuery().eq(User::getUserAccount, userAccount).getWrapper());

        // 用户不存在
        if (user == null) {
            log.info("user login failed, {} account not found", userAccount);
            return null;
        }

        // 2.加密
        String encryptPassword = DigestUtil.md5Hex(user.getSalt() + userPassword);

        // 3.判断密码是否正确
        if (!user.getUserPassword().equals(encryptPassword)) {
            // 密码错误
            log.info("user login failed, userPassword can not match user:{}", userAccount);
            return null;
        }

        // 3.脱敏
        User safetyUser = getSafetyUser(user);

        // 4.记录用户登录状态
        request.getSession().setAttribute(UserConsist.USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }

    /**
     * 根据用户名来搜索用户
     *
     * @param username 用户名
     * @return 用户列表
     */
    @Override
    public List<User> searchUsers(String username) {
        return baseMapper.selectList(lambdaQuery()
                .like(StringUtils.isNotBlank(username), User::getUsername, username)
                .getWrapper()).stream()
                .map(this::getSafetyUser)
                .collect(Collectors.toList());
    }

    /**
     * 用户信息脱敏
     *
     * @param originUser 原始用户信息
     * @return 脱敏后的用户信息
     */
    @Override
    public User getSafetyUser(User originUser) {
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setRole(originUser.getRole());
        safetyUser.setStatus(originUser.getStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }
}

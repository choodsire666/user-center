package com.chood.usercenter.service;

import cn.hutool.core.util.ObjectUtil;
import com.chood.usercenter.model.entity.User;
import com.chood.usercenter.model.request.UserLoginRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务测试
 *
 * @author 谢金成
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void testAddUser() {
        User user = new User();

        user.setUserAccount("123");
        user.setUserPassword("123");
        user.setUsername("dogyupi");
        user.setGender(0);
        user.setAvatarUrl("");
        user.setPhone("123");
        user.setEmail("456");


        boolean result = userService.save(user);

        Assertions.assertTrue(result);

        System.out.println(user.getId());
    }

    @Test
    void userRegister() {
        // 为空
        String userAccount = "chood";
        String userPassword = "123456";
        String checkPassword = "";

        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 账号小于4位
        userAccount = "ch";
        checkPassword = "123456";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 密码小于8位
        userAccount = "chood";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 特殊字符
        userAccount = "ch ood";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 两次密码不一致
        userAccount = "chood";
        userPassword = "12345678";
        checkPassword = "12345679";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 账户已存在
        userAccount = "chood";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 成功
        userAccount = "choodsire666";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertTrue(result > 0);
    }

    @Test
    void doLogin() {
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUserAccount("choodsire666");
        userLoginRequest.setUserPassword("12345678");

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        if (ObjectUtil.isNotEmpty(request)) {
            User user = userService.doLogin(userLoginRequest.getUserAccount(), userLoginRequest.getUserPassword(), request);
            Assertions.assertNotNull(user);
        }
    }
}
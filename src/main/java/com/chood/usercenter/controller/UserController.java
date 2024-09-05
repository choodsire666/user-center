package com.chood.usercenter.controller;

import cn.hutool.core.util.ObjectUtil;
import com.chood.usercenter.constant.consist.UserConsist;
import com.chood.usercenter.model.entity.User;
import com.chood.usercenter.model.request.UserLoginRequest;
import com.chood.usercenter.model.request.UserRegisterRequest;
import com.chood.usercenter.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * 用户控制层
 *
 * @author chood
 */
@RestController
@RequestMapping("/user")
@Api(value = "用户控制层-UserController", tags = "用户控制层-UserController")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 用户注册请求
     * @return 用户id
     */
    @PostMapping("/register")
    @ApiOperation(value = "用户注册", notes = "用户注册")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            return null;
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }

        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "用户登录")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        return userService.doLogin(userAccount, userPassword, request);
    }

    @GetMapping("/search")
    @ApiOperation(value = "根据用户名搜索用户", notes = "根据用户名搜索用户")
    public List<User> searchUsers(String username, HttpServletRequest request) {
        // 仅管理员可以查看
        if (isNotAdmin(request)) {
            return Collections.emptyList();
        }

        return userService.searchUsers(username);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除用户", notes = "删除用户")
    public Boolean deleteUser(@RequestBody Long id, HttpServletRequest request) {
        // 仅管理员可以删除
        if (isNotAdmin(request)) {
            return false;
        }

        // 参数校验
        if (ObjectUtil.isEmpty(id) || id <= 0L) {
            return false;
        }

        return userService.removeById(id);
    }

    /**
     * 是否是管理员
     *
     * @param request HttpServletRequest
     * @return 是 返回 true， 否则返回 false
     */
    private boolean isNotAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(UserConsist.USER_LOGIN_STATE);

        if (ObjectUtil.isNotEmpty(userObj) && userObj instanceof User) {
            User user = (User) userObj;
            Integer role = user.getRole();

            return ObjectUtil.isNotEmpty(role) && role == UserConsist.ADMIN_ROLE;
        }

        return false;
    }
}

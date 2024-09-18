package com.chood.usercenter.controller;

import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.chood.usercenter.common.Result;
import com.chood.usercenter.common.ResultCode;
import com.chood.usercenter.constant.consist.CaptchConsist;
import com.chood.usercenter.constant.consist.UserConsist;
import com.chood.usercenter.exception.BusinessException;
import com.chood.usercenter.model.entity.User;
import com.chood.usercenter.model.request.*;
import com.chood.usercenter.model.vo.UserVO;
import com.chood.usercenter.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    public Result<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账号或密码为空!");
        }

        return userService.userRegister(userAccount, userPassword, checkPassword);
    }

    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "用户登录")
    public Result<String> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "账号或密码为空!");
        }

        return userService.doLogin(userAccount, userPassword, request);
    }

    @PostMapping("/logout")
    @ApiOperation(value = "用户退出登录", notes = "用户退出登录")
    public Result<String> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ResultCode.ERROR);
        }
        userService.userLogout(request);
        return Result.success("退出成功!");
    }

    @GetMapping("/current")
    @ApiOperation(value = "获取当前登录用户信息", notes = "获取当前登录用户信息")
    public Result<UserVO> getCurrentUser(@SessionAttribute(UserConsist.USER_LOGIN_STATE) UserVO user) {
        if (user == null) {
            throw new BusinessException(ResultCode.NOT_LOGIN);
        }

        Long id = user.getId();
        return Result.success(userService.getSafetyUser(userService.getById(id)));
    }

    @GetMapping("/search")
    @ApiOperation(value = "根据用户名搜索用户", notes = "根据用户名搜索用户")
    public Result<IPage<UserVO>> searchUsers(SearchUsersQuery searchUsersQuery, HttpServletRequest request) {
        // 仅管理员可以查看
        if (!isAdmin(request)) {
            throw new BusinessException(ResultCode.NO_AUTH, "仅管理员可查看!");
        }

        if (ObjectUtil.isEmpty(searchUsersQuery)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }

        return userService.searchUsers(searchUsersQuery);
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑用户", notes = "编辑用户")
    public Result<String> editUser(@RequestBody UserEditRequest userEditRequest, HttpServletRequest request) {
        // 仅管理员可以删除
        if (!isAdmin(request)) {
            throw new BusinessException(ResultCode.NO_AUTH, "仅管理员可删除!");
        }

        if (ObjectUtil.isEmpty(userEditRequest)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }

        User user = new User();
        BeanUtil.copyProperties(userEditRequest, user);

        if (!userService.updateById(user)) {
            throw new BusinessException(ResultCode.ERROR, "编辑失败!");
        }

        return Result.success("编辑成功!");
    }

    @PostMapping("/delete/{id}")
    @ApiOperation(value = "删除用户", notes = "删除用户")
    public Result<String> deleteUser(@PathVariable(value = "id") Long id, HttpServletRequest request) {
        // 仅管理员可以删除
        if (!isAdmin(request)) {
            throw new BusinessException(ResultCode.NO_AUTH, "仅管理员可删除!");
        }

        // 参数校验
        if (ObjectUtil.isEmpty(id) || id <= 0L) {
            throw new BusinessException(ResultCode.PARAMS_ERROR, "id为空或小于等于0");
        }

        if (!userService.removeById(id)) {
            throw new BusinessException(ResultCode.ERROR, "删除失败!");
        }

        return Result.success("删除成功!");
    }

    @PostMapping("/findMethod")
    @ApiOperation(value = "根据找回方式查找用户ID", notes = "根据找回方式查找用户ID")
    public Result<Long> findMethod(@RequestBody FindMethodRequest findMethodRequest) {
        if (ObjectUtil.isEmpty(findMethodRequest)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }

        return userService.findMethod(findMethodRequest);
    }

    @GetMapping("/verifyCode")
    @ApiOperation(value = "验证", notes = "验证")
    public Result<String> verifyCode(String code, HttpSession session) {
        String target = (String) session.getAttribute(CaptchConsist.CAPTCHA_CODE);
        boolean verify = new RandomGenerator(RandomUtil.BASE_NUMBER, 6).verify(target, code);
        if (verify) {
            session.removeAttribute(CaptchConsist.CAPTCHA_CODE);
            return Result.success("验证成功！");
        }

        return Result.error("验证码输入错误！");
    }

    @PostMapping("/resetPassword")
    @ApiOperation(value = "重置密码", notes = "重置密码")
    public Result<String> resetPassword(@RequestBody ResetPasswordRequest resetPasswordRequest) {
        if (ObjectUtil.isEmpty(resetPasswordRequest)) {
            throw new BusinessException(ResultCode.PARAMS_ERROR);
        }

        return userService.resetPassword(resetPasswordRequest);
    }

    /**
     * 是否是管理员
     *
     * @param request HttpServletRequest
     * @return 是 返回 true， 否则返回 false
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(UserConsist.USER_LOGIN_STATE);

        if (ObjectUtil.isNotEmpty(userObj) && userObj instanceof UserVO) {
            UserVO user = (UserVO) userObj;
            Integer role = user.getRole();

            return ObjectUtil.isNotEmpty(role) && role == UserConsist.ADMIN_ROLE;
        }

        return false;
    }
}

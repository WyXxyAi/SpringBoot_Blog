package com.wany.controller;

import com.wany.service.LoginService;
import com.wany.vo.Result;
import com.wany.vo.params.LoginParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("登录")
@RequestMapping("login")
public class LoginController {
//    @Autowired
//    private SysUserService sysUserService;
    @Autowired
    private LoginService loginService;

    @PostMapping
    @ApiOperation(value = "登录")
    public Result login(@RequestBody LoginParam loginParam){
        //登录 验证用户  访问用户表
        return loginService.login(loginParam);
    }
}

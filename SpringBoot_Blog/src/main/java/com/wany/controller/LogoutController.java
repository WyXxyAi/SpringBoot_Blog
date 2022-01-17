package com.wany.controller;

import com.wany.service.LoginService;
import com.wany.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("登出")
@RequestMapping("logout")
public class LogoutController {

    @Autowired
    private LoginService loginService;

    @GetMapping
    @ApiOperation(value = "登出")
    public Result logout(@RequestHeader("Authorization") String token){
        return loginService.logout(token);
    }
}

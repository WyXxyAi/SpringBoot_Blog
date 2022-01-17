package com.wany.controller;

import com.wany.service.SysUserService;
import com.wany.vo.Result;
import com.wany.vo.params.ChangeUserInfoParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api("用户相关操作")
@RequestMapping("users")
public class UsersController {

    @Autowired
    private SysUserService sysUserService;

    ///users/currentUser
    @ApiOperation(value = "获取用户信息(ByToken)")
    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("Authorization") String token){
        return sysUserService.findUserByToken(token);
    }

    ///users/changeUserInfo
    @ApiOperation(value = "修改用户信息(除头像)")
    @PostMapping("changeUserInfo")
    public Result changeUserInfo(@RequestBody ChangeUserInfoParam changeUserInfoParam){
        return sysUserService.changeUserInfo(changeUserInfoParam);
    }

    ///users/changeUserAvatar/${id}
    @ApiOperation(value = "修改用户头像")
    @PostMapping("changeUserAvatar/{id}")
    public Result changeUserAvatar(@PathVariable("id") Long id,@RequestParam("avatar") MultipartFile file){
        return sysUserService.changeUserAvatar(id,file);
    }
}

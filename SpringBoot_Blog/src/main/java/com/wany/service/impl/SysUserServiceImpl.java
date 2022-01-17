package com.wany.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wany.dao.mapper.SysUserMapper;
import com.wany.dao.pojo.SysUser;
import com.wany.service.LoginService;
import com.wany.service.SysUserService;
import com.wany.utils.QiniuUtils;
import com.wany.vo.ErrorCode;
import com.wany.vo.UserAllVo;
import com.wany.vo.Result;
import com.wany.vo.UserVo;
import com.wany.vo.params.ChangeUserInfoParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {

    private final String avatarPath = "F:/Vue/img/";

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private LoginService loginService;
    @Autowired
    private QiniuUtils qiniuUtils;
    @Override
    public UserVo findUserVoById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/static/img/logo.b3a48c0.png");
            sysUser.setNickname("MyBlog");
        }
        UserVo userVo  = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        userVo.setId(String.valueOf(sysUser.getId()));
        return userVo;
    }

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null){
            sysUser = new SysUser();
            sysUser.setNickname("用户已注销");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword,password);
        queryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname,
                SysUser::getAdmin,SysUser::getEmail,SysUser::getMobilePhoneNumber,SysUser::getDeleted);
        queryWrapper.last("limit 1");

        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        /**
         * 1. token合法性校验
         *    是否为空，解析是否成功 redis是否存在
         * 2. 如果校验失败 返回错误
         * 3. 如果成功，返回对应的结果 LoginUserVo
         */
        // token合法性校验
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }
        UserAllVo userAllVo = new UserAllVo();
        userAllVo.setId(String.valueOf(sysUser.getId()));
        userAllVo.setNickname(sysUser.getNickname());
        userAllVo.setAvatar(sysUser.getAvatar());
        userAllVo.setAccount(sysUser.getAccount());
        //个人主页新增data
        userAllVo.setAdmin(sysUser.getAdmin());
        userAllVo.setDeleted(sysUser.getDeleted());
        userAllVo.setEmail(sysUser.getEmail());
        userAllVo.setMobilePhoneNumber(sysUser.getMobilePhoneNumber());
        //md5不能反向解密
        return Result.success(userAllVo);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last("limit 1");
        return this.sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        //保存用户这 id会自动生成
        //这个地方 默认生成的id是 分布式id 雪花算法
        //mybatis-plus
        this.sysUserMapper.insert(sysUser);
    }

    @Override
    public Result changeUserInfo(ChangeUserInfoParam changeUserInfoParam) {
        sysUserMapper.changeUserInfo(changeUserInfoParam);
        return Result.success("success");
    }

    @Override
    public Result changeUserAvatar(Long id, MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        //获取后缀
        String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        //前缀用id代替
        String prefixName = id.toString();
        //唯一文件名称
        String fileName = prefixName+suffixName;
        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload){

            ChangeUserInfoParam changeUserInfoParam = new ChangeUserInfoParam();
            changeUserInfoParam.setAvatar(QiniuUtils.url+fileName);
            changeUserInfoParam.setId(id);

            sysUserMapper.changeUserInfo(changeUserInfoParam);
            return Result.success(QiniuUtils.url + fileName );
        }
        return Result.fail(-328,"上传失败");
    }


}

package com.wany.service;

import com.wany.dao.pojo.SysUser;
import com.wany.vo.Result;
import com.wany.vo.UserVo;
import com.wany.vo.params.ChangeUserInfoParam;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
public interface SysUserService {

    UserVo findUserVoById(Long id);

    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    /**
     * 根据token查询用户信息
     * @param token
     * @return
     */
    Result findUserByToken(String token);

    /**
     * 根据账户查找用户
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);

    /**
     * 保存用户
     * @param sysUser
     */
    void save(SysUser sysUser);

    /**
     * 修改用户信息
     * @param changeUserInfoParam
     * @return
     */
    Result changeUserInfo(ChangeUserInfoParam changeUserInfoParam);

    /**
     * 修改头像
     * @param id
     * @param file
     * @return
     */
    Result changeUserAvatar(Long id, MultipartFile file);
}

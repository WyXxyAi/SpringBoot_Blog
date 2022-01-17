package com.wany.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wany.dao.pojo.SysUser;
import com.wany.vo.params.ChangeUserInfoParam;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {

    void changeUserInfo(ChangeUserInfoParam changeUserInfoParam);

}

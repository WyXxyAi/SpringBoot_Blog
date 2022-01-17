package com.wany.vo.params;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ChangeUserInfoParam {
    private Long id;
    private String account;
    private String email;
    private String mobilePhoneNumber;
    private String nickname;
    private String admin;
    private String deleted;
    //头像复用
    private String avatar;
}


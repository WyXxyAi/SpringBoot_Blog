package com.wany.vo;

import lombok.Data;

@Data
public class UserAllVo {

    private String id;

    private String account;

    private String nickname;

    private String avatar;

    private Integer admin;

    private Integer deleted;

    private String email;

    private String mobilePhoneNumber;
}

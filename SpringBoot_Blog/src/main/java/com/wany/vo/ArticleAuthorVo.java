package com.wany.vo;

import lombok.Data;

@Data
public class ArticleAuthorVo {
    private String nickname;
    private String avatar;
    //删除功能验证用户，增加作者id
    private String authorId;

}

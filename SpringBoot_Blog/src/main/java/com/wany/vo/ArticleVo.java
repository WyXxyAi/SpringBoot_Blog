package com.wany.vo;

import lombok.Data;

import java.util.List;

@Data
public class ArticleVo {

//    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    private String title;

    private String summary;

    private Integer commentCounts;

    private Integer viewCounts;

    private Integer weight;
    /**
     * 创建时间
     */
    private String createDate;

    //原来为String，无法获取作者头像，改为Sysuser容易暴露作者信息，新建ArticleAuthorVo只返回作者account和avatar
    private ArticleAuthorVo author;

    private ArticleBodyVo body;

    private List<TagVo> tags;

    private CategoryVo category;

}

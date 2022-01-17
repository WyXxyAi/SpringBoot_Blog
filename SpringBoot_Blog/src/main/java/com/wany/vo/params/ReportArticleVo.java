package com.wany.vo.params;

import lombok.Data;

@Data
public class ReportArticleVo {
    private String articleId;
    private String authorId;
    private String authorName;
    private String title;
    private String userId;
    private String userName;
    private String reason;
}

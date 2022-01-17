package com.wany.service;

import com.wany.vo.Result;
import com.wany.vo.params.CommentParam;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CommentsService {
    /**
     * 根据文章id 查询所有的评论列表
     * @param id
     * @return
     */
    Result commentsByArticleId(Long id);

    Result comment(CommentParam commentParam);

    Result deleteCommentByArticleId(Long articleId);
}

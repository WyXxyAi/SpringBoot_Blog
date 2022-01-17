package com.wany.service;

import com.wany.vo.Result;
import com.wany.vo.params.ArticleParam;
import com.wany.vo.params.PageParams;
import com.wany.vo.params.ReportArticleVo;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ArticleService {
    /**
     * 分页查询 文章列表
     * @param pageParams
     * @return
     */
    Result listArticle(PageParams pageParams);

    /**
     * 最热文章
     * @param limit
     * @return
     */
    Result hotArticle(int limit);

    /**
     * 最新文章
     * @param limit
     * @return
     */
    Result newArticles(int limit);

    /**
     * 文章归档
     * @return
     */
    Result listArchives();

    /**
     * 查看文章详情
     * @param articleId
     * @return
     */
    Result findArticleById(Long articleId);

    /**
     * 文章发布服务
     * @param articleParam
     * @return
     */
    Result publish(ArticleParam articleParam);

    /**
     * 删除文章
     * @param articleId
     * @return
     */
    Result deleteArticleById(Long articleId);

    /**
     * 举报文章
     * @param reportArticleVo
     * @return
     */
    Result reportArticle(ReportArticleVo reportArticleVo);
}

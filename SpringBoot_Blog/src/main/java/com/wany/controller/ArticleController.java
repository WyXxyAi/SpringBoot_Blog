package com.wany.controller;

import com.wany.common.aop.LogAnnotation;
import com.wany.common.cache.Cache;
import com.wany.service.ArticleService;
import com.wany.vo.Result;
import com.wany.vo.params.ArticleParam;
import com.wany.vo.params.PageParams;
import com.wany.vo.params.ReportArticleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.web.bind.annotation.*;

//json数据进行交互
@RestController
@Api("文章相关")
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    /**
     * 首页 文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    @ApiOperation(value = "首页查询文章列表")
    //加上此注解 代表要对此接口记录日志
    @LogAnnotation(module="文章",operator="获取文章列表")
    @Cache(expire = 5 * 60 * 1000,name = "listArticle")
    public Result listArticle(@RequestBody PageParams pageParams){
//        int i = 10/0;
        return articleService.listArticle(pageParams);
    }

    /**
     * 首页 最热文章
     * @return
     */
    @PostMapping("hot")
    @ApiOperation(value = "最热文章")
    @Cache(expire = 5 * 60 * 1000,name = "hot_article")
    public Result hotArticle(){
        int limit = 5;
        return articleService.hotArticle(limit);
    }

    /**
     * 首页 最新文章
     * @return
     */
    @PostMapping("new")
    @ApiOperation(value = "最新文章")
    @Cache(expire = 5 * 60 * 1000,name = "news_article")
    public Result newArticles(){
        int limit = 5;
        return articleService.newArticles(limit);
    }

    @ApiOperation(value = "文章列表全部")
    @PostMapping("listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }


    @ApiOperation(value = "根据ID查询文章")
    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }
    //接口url：/articles/publish
    //
    //请求方式：POST
    @ApiOperation(value = "发布文章")
    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){

        return articleService.publish(articleParam);
    }

    //articles/deleteArticleById/${id}
    @ApiOperation(value = "删除文章")
    @LogAnnotation(module="文章",operator="删除文章")
    @PostMapping("deleteArticleById/{id}")
    public Result deleteArticleById(@PathVariable("id")Long articleId){
        return articleService. deleteArticleById(articleId);
    }
    //articles/reportArticle
    @ApiOperation(value = "举报文章")
    @PostMapping("reportArticle")
    public Result reportArticle(@RequestBody ReportArticleVo reportArticleVo){
        return articleService.reportArticle(reportArticleVo);
    }
}

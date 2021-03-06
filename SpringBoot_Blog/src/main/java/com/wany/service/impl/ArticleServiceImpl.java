package com.wany.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wany.dao.dos.Archives;
import com.wany.dao.mapper.ArticleBodyMapper;
import com.wany.dao.mapper.ArticleMapper;
import com.wany.dao.mapper.ArticleTagMapper;
import com.wany.dao.pojo.Article;
import com.wany.dao.pojo.ArticleBody;
import com.wany.dao.pojo.ArticleTag;
import com.wany.dao.pojo.SysUser;
import com.wany.service.*;
import com.wany.utils.UserThreadLocal;
import com.wany.vo.*;
import com.wany.vo.params.ArticleParam;
import com.wany.vo.params.PageParams;
import com.wany.vo.params.ReportArticleVo;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private CommentsService commentsService;
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());

        IPage<Article> articleIPage = articleMapper.listArticle(
                page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records,true,true));
    }

//    @Override
//    public Result listArticle(PageParams pageParams) {
//        /**
//         * 1. ???????????? article????????????
//         */
//        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        if (pageParams.getCategoryId() != null){
//            // and category_id=#{categoryId}
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//        List<Long> articleIdList = new ArrayList<>();
//        if (pageParams.getTagId() != null){
//            //???????????? ????????????
//            //article?????? ?????????tag?????? ???????????? ???????????????
//            //article_tag  article_id 1 : n tag_id
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            for (ArticleTag articleTag : articleTags) {
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if (articleIdList.size() > 0){
//                // and id in(1,2,3)
//                queryWrapper.in(Article::getId,articleIdList);
//            }
//        }
//        //????????????????????????
//        //order by create_date desc
//        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
//        List<Article> records = articlePage.getRecords();
//        //????????????????????? ???????????????
//        List<ArticleVo> articleVoList = copyList(records,true,true);
//        return Result.success(articleVoList);
//    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        //select id,title from article order by view_counts desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result newArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        //select id,title from article order by create_date desc desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }


    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 1. ??????id?????? ????????????
         * 2. ??????bodyId???categoryid ??????????????????
         */
        Article article = this.articleMapper.selectById(articleId);

        ArticleVo articleVo = copy(article, true, true,true,true);
        //????????????????????????????????????????????????????????????
        //??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        // ?????? ???????????????????????? ?????? ?????????????????????????????????????????? ?????????????????????
        //?????????  ????????????????????? ?????????????????????????????????????????????????????????
        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(articleVo);
    }

    @Override
    public Result publish(ArticleParam articleParam) {
        //????????? ??????????????????????????????
        SysUser sysUser = UserThreadLocal.get();
        /**
         * 1. ???????????? ?????? ??????Article??????
         * 2. ??????id  ?????????????????????
         * 3. ??????  ????????????????????? ??????????????????
         * 4. body ???????????? article bodyId
         */
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());
        article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
        //???????????? ?????????????????????id
        this.articleMapper.insert(article);
        //tag
        List<TagVo> tags = articleParam.getTags();
        if (tags != null){
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(Long.parseLong(tag.getId()));
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }
        //body
        ArticleBody articleBody  = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        Map<String,String> map = new HashMap<>();
        map.put("id",article.getId().toString());
        return Result.success(map);
    }

    @Override
    public Result deleteArticleById(Long articleId) {
        commentsService.deleteCommentByArticleId(articleId);
        articleMapper.deleteById(articleId);
        return Result.success(null);
    }

    @Override
    public Result reportArticle(ReportArticleVo reportArticleVo) {
        /**
         * ??????????????????????????????????????????
         */
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        try {
            messageHelper.setSubject("MyBlogDemo????????????");
            messageHelper.setText(
                    "<!DOCTYPE html>\n" +
                    "<html  xmlns:th=\"http://www.thymeleaf.org\">\n" +
                    "<head>\n" +
                            "    <meta charset=\"UTF-8\" />\n" +
                            "    <title>??????????????????</title>\n" +
                    "</head>\n"+
                    "<body>\n"+
                    "<table border=\"1\">\n" +
                            "  <tr>\n" +
                            "    <td>?????????ID</td>\n" +
                            "    <td>"+reportArticleVo.getUserId()+"</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td>?????????</td>\n" +
                            "    <td>"+reportArticleVo.getUserName()+"</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td>????????????</td>\n" +
                            "    <td>"+data+"</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td>???????????????Id</td>\n" +
                            "    <td>"+reportArticleVo.getArticleId()+"</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td>?????????????????????</td>\n" +
                            "    <td>"+reportArticleVo.getTitle()+"</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td>????????????Id</td>\n" +
                            "    <td>"+reportArticleVo.getAuthorId()+"</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td>????????????</td>\n" +
                            "    <td>"+reportArticleVo.getAuthorName()+"</td>\n" +
                            "  </tr>\n" +
                            "  <tr>\n" +
                            "    <td>????????????</td>\n" +
                            "    <td>"+reportArticleVo.getReason()+"</td>\n" +
                            "  </tr>\n" +
                    "</table>\n"+
                    "</body>\n" +
                    "</html>",true);
            messageHelper.setTo("1254106926@qq.com");
            messageHelper.setFrom("1254106926@qq.com");

            mailSender.send(mimeMessage);
            return Result.success(null);
        } catch (MessagingException e) {
            return Result.fail(-10001, String.valueOf(e.getCause()));
        }
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVoList;
    }
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor, boolean isBody,boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }




    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody,boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article,articleVo);

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //???????????????????????? ??????????????? ???????????????
        if (isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if (isAuthor){
            Long authorId = article.getAuthorId();
            ArticleAuthorVo authorVo = new ArticleAuthorVo();
            SysUser sysUser = sysUserService.findUserById(authorId);
            authorVo.setNickname(sysUser.getNickname());
            authorVo.setAvatar(sysUser.getAvatar());
            authorVo.setAuthorId(String.valueOf(sysUser.getId()));
            articleVo.setAuthor(authorVo);
        }
        if (isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }



    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

}

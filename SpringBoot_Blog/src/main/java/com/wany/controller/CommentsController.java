package com.wany.controller;

import com.wany.service.CommentsService;
import com.wany.vo.Result;
import com.wany.vo.params.CommentParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api("评论相关")
@RequestMapping("comments")
public class CommentsController {
    @Autowired
    private CommentsService commentsService;

    ///comments/article/{id}

    @ApiOperation(value = "根据文章id查评论")
    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long id){
        return commentsService.commentsByArticleId(id);
    }


    @ApiOperation(value = "新增评论")
    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentsService.comment(commentParam);
    }
}

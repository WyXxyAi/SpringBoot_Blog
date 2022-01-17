package com.wany.controller;

import com.wany.service.TagService;
import com.wany.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
@Api("文章标签相关")
public class TagsController {
    @Autowired
    private TagService tagService;

    //   /tags/hot
    @ApiOperation(value = "查最热标签(前6)")
    @GetMapping("hot")
    public Result hot(){
        int limit = 6;
        return tagService.hots(limit);
    }
    @GetMapping
    @ApiOperation(value = "查所有标签")
    public Result findAll(){
        return tagService.findAll();
    }

    @GetMapping("detail")
    @ApiOperation(value = "查所有标签描述")
    public Result findAllDetail(){
        return tagService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    @ApiOperation(value = "根据id查标签描述")
    public Result findDetailById(@PathVariable("id") Long id){
        return tagService.findDetailById(id);
    }

}

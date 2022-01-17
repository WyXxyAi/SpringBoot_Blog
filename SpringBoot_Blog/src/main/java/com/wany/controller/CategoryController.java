package com.wany.controller;

import com.wany.service.CategoryService;
import com.wany.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("文章类别相关")
@RequestMapping("categorys")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // /categorys
    @ApiOperation(value = "查询所有文章类别")
    @GetMapping
    public Result categories(){
        return categoryService.findAll();
    }

    @ApiOperation(value = "查询所有描述(发布的时候会调用)")
    @GetMapping("detail")
    public Result categoriesDetail(){
        return categoryService.findAllDetail();
    }

    ///category/detail/{id}
    @ApiOperation(value = "根据id查分类描述")
    @GetMapping("detail/{id}")
    public Result categoryDetailById(@PathVariable("id") Long id){
        return categoryService.categoryDetailById(id);
    }
}

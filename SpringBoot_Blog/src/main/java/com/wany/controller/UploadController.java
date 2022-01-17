package com.wany.controller;

import com.wany.utils.QiniuUtils;
import com.wany.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@Api("markdown中文件上传")
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private QiniuUtils qiniuUtils;

    @PostMapping
    @ApiOperation(value = "markdown中文件上传")
    public Result upload(@RequestParam("image") MultipartFile file){
        //原始文件名称 比如 aa.png
        String originalFilename = file.getOriginalFilename();
        //唯一的文件名称UUID+后缀
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(originalFilename, ".");

        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload){
            return Result.success(QiniuUtils.url + fileName );
        }
        return Result.fail(20001,"上传失败");
    }
}

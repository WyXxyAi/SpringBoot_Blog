package com.wany.service;

import com.wany.vo.Result;
import com.wany.vo.TagVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
public interface TagService {

    List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);

    /**
     * 查询所有的文章标签
     * @return
     */
    Result findAll();

    Result findAllDetail();

    Result findDetailById(Long id);
}

package com.wany.service;

import com.wany.vo.CategoryVo;
import com.wany.vo.Result;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface CategoryService {

    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result categoryDetailById(Long id);
}

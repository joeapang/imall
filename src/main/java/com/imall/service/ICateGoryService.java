package com.imall.service;

import com.imall.common.ServerResponse;
import com.imall.pojo.Category;

import java.util.List;

public interface ICateGoryService {
    ServerResponse<String> addCategory(String categoryName, Integer parentId);
    ServerResponse<String> checkCategory(String cateGoryName, Integer parentId);
    ServerResponse<String> updateCategory( String categoryName, Integer categoryId,Integer parentId);
    ServerResponse<List<Category>> getByKeySelective(Category category);
    ServerResponse<List<Category>> getDeepSubCategory(Category category);

}

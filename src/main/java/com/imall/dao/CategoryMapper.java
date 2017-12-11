package com.imall.dao;

import com.imall.pojo.Category;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);
    int selectCountByKeySelective(Category category);

    List<Category> selectSubCategoryByParentId(Integer parentId);
    List<Category> selectByKeySelective(Category category);
}
package com.imall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.imall.common.ServerResponse;
import com.imall.dao.CategoryMapper;
import com.imall.pojo.Category;
import com.imall.service.ICateGoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("iCateGoryService")
public class CateGoryServiceImpl implements ICateGoryService {


    private Logger logger = Logger.getLogger(CateGoryServiceImpl.class);
    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public ServerResponse<String> addCategory(String categoryName, Integer parentId) {
        if (StringUtils.isBlank(categoryName) || (parentId == null)) {
            return ServerResponse.createByErrorMessage("添加商品种类参数错误！");
        }

        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        category.setStatus(true);//此分类可用

        if (checkCategory(categoryName, parentId).isSuccess()) {
            return checkCategory(categoryName, parentId);
        }
        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加种类成功！");

        }

        return ServerResponse.createByErrorMessage("添加失败！");
    }

    @Override
    public ServerResponse<String> checkCategory(String categoryName, Integer parentId) {
        if (StringUtils.isBlank(categoryName) || parentId == null) {
            return ServerResponse.createByErrorMessage("商品种类参数错误！");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        int countResult = categoryMapper.selectCountByKeySelective(category);
        if (countResult > 0) {
            return ServerResponse.createBySuccessMessage("商品种类已存在！");

        }
        return ServerResponse.createByErrorMessage("商品种类不存在！");
    }

    @Override
    public ServerResponse<String> updateCategory(String categoryName, Integer categoryId, Integer parentId) {
        if (StringUtils.isBlank(categoryName) || categoryId == null) {
            return ServerResponse.createByErrorMessage("商品种类参数错误！");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setId(categoryId);
        ServerResponse checkResult = checkCategory(categoryName, parentId);
        if (checkResult.isSuccess()) {
            return checkResult;
        }
        int countResult = categoryMapper.updateByPrimaryKeySelective(category);
        if (countResult > 0) {
            return ServerResponse.createBySuccessMessage("商品种类更新成功！");

        }
        return ServerResponse.createByErrorMessage("更新失败！");
    }

    @Override
    public ServerResponse<List<Category>> getByKeySelective(Category category) {
        List<Category> categoryList = new ArrayList<>();

        categoryList = categoryMapper.selectByKeySelective(category);

        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("=====args:" + category.toString() + "数据库中没有匹配行！");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    @Override
    public ServerResponse<List<Category>> getDeepSubCategory(Category category) {
        Set<Category> categoryHashSet= Sets.newHashSet();
        getTreeChildren(categoryHashSet,category);
        List<Category> categoryList= Lists.newArrayList();
        if(categoryList!=null){
            for(Category item:categoryHashSet){
                categoryList.add(item);
            }

        }
        return ServerResponse.createBySuccess(categoryList);
    }

    private Set<Category> getTreeChildren(Set<Category> cset ,Category cg) {
        Category category=categoryMapper.selectByPrimaryKey(cg.getId());
        Category sed=new Category();
        sed.setParentId(cg.getId());
        if(category!=null){
            cset.add(category);
        }


        List<Category> categoryList=categoryMapper.selectByKeySelective(sed);
        for(Category cItem:categoryList){
            getTreeChildren(cset,cItem);
        }
        return cset;
    }



}

package com.imall.dao;

import com.imall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    List<Product> getList(@Param("product") Product product, @Param("fromPrice") BigDecimal fromPrice, @Param("toPrice") BigDecimal toPrice);
}
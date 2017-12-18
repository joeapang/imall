package com.imall.service;

import com.github.pagehelper.PageInfo;
import com.imall.common.ServerResponse;
import com.imall.pojo.Product;
import com.imall.vo.ProductDetail;

import java.math.BigDecimal;

public interface IProductService {

    ServerResponse<String> saveOrUpdateProduct(Product product);
    ServerResponse<ProductDetail> managerProductDetails(Integer productId);
    ServerResponse<PageInfo<ProductDetail>> managerList(Product product, BigDecimal fromPrice, BigDecimal toPrice, Integer pageNum, Integer pageSize);
    public ServerResponse<PageInfo<ProductDetail>> getList(Product product, String categoryName,BigDecimal fromPrice, BigDecimal toPrice, Integer pageNum, Integer pageSize) ;

    }

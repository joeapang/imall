package com.imall.service;

import com.github.pagehelper.PageInfo;
import com.imall.common.ServerResponse;
import com.imall.pojo.Product;
import com.imall.vo.ProductDetail;

public interface IProductService {

    ServerResponse<String> saveOrUpdateProduct(Product product);
    ServerResponse<ProductDetail> getProductDetails(Integer productId);
    ServerResponse<PageInfo<ProductDetail>> getList(Product product, Integer pageNum, Integer pageSize);
}

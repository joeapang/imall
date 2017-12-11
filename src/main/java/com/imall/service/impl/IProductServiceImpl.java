package com.imall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imall.common.ResponseCode;
import com.imall.common.ServerResponse;
import com.imall.dao.CategoryMapper;
import com.imall.dao.ProductMapper;
import com.imall.pojo.Category;
import com.imall.pojo.Product;
import com.imall.service.IProductService;
import com.imall.utils.DateUtil;
import com.imall.utils.PropertiesUtils;
import com.imall.vo.ProductDetail;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("iProductService")
public class IProductServiceImpl implements IProductService {

    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    ProductMapper productMapper;

    @Override
    public ServerResponse<String> saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (StringUtils.isBlank(product.getMainImage())) {
                if (!StringUtils.isBlank(product.getSubImages())) {
                    String[] subImages = product.getSubImages().split(",");
                    if (subImages.length > 0) {
                        product.setMainImage(subImages[0]);
                    }
                }

            }

            if (product.getId() != null) {
                int result = productMapper.updateByPrimaryKey(product);
                if (result > 0)
                    return ServerResponse.createBySuccessMessage("更新产品成功！");

            } else {
                int result = productMapper.insert(product);
                if (result > 0)
                    return ServerResponse.createBySuccessMessage("更新添加成功！");
            }
        }
        return ServerResponse.createByErrorMessage("产品信息错误！");
    }

    @Override
    public ServerResponse<ProductDetail> getProductDetails(Integer productId) {

        if (StringUtils.isBlank(productId.toString())) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDes());
        }
        ProductDetail productDetail;
        Product product=productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerResponse.createBySuccessMessage("产品不存在！");
        }
        productDetail= assembleProductDetail(product);
        return ServerResponse.createBySuccess(productDetail);
    }

    @Override
    public ServerResponse<PageInfo<ProductDetail>> getList(Product product, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize,false);
        List<Product> products=productMapper.getList(product);
        List<ProductDetail> resultList=new ArrayList<>();
        for(Product item:products){
            resultList.add(assembleProductDetail(item));
        }

        PageInfo<ProductDetail> pageResult=new PageInfo<>(resultList);
        return ServerResponse.createBySuccess(pageResult);

    }

    private ProductDetail assembleProductDetail(Product product) {
        ProductDetail productDetail = new ProductDetail();
        productDetail.setProductId(product.getId());
        productDetail.setSubImage(product.getSubImages());
        productDetail.setMainImage(product.getMainImage());
        productDetail.setSubtitle(product.getSubtitle());
        productDetail.setPrice(product.getPrice());
        productDetail.setCategoryId(product.getCategoryId());
        productDetail.setStock(product.getStock());
        product.setStatus(product.getStatus());


        productDetail.setCreateTime(DateUtil.dateToStr(product.getCreateTime()));
        productDetail.setUpdateTime(DateUtil.dateToStr(product.getUpdateTime()));
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            productDetail.setParentCategory(0);
        } else {
            productDetail.setParentCategory(category.getParentId());
        }
        productDetail.setImageHost(PropertiesUtils.getProperties("ftp.server.http.prefix", ""));
        return productDetail;
    }
}

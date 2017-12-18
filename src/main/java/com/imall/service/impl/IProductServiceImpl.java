package com.imall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.imall.common.Const;
import com.imall.common.ResponseCode;
import com.imall.common.ServerResponse;
import com.imall.dao.CategoryMapper;
import com.imall.dao.ProductMapper;
import com.imall.pojo.Category;
import com.imall.pojo.Product;
import com.imall.service.ICateGoryService;
import com.imall.service.IProductService;
import com.imall.utils.DateUtil;
import com.imall.utils.PropertiesUtils;
import com.imall.vo.ProductDetail;
import net.sf.jsqlparser.schema.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service("iProductService")
public class IProductServiceImpl implements IProductService {

    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    ICateGoryService cateGoryService;

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
    public ServerResponse<ProductDetail> managerProductDetails(Integer productId) {

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
    public ServerResponse<PageInfo<ProductDetail>> managerList(Product product, BigDecimal fromPrice, BigDecimal toPrice, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize,false);
        List<Product> products=productMapper.getList(product,fromPrice,toPrice);
        if(products.isEmpty()){
            return ServerResponse.createByErrorMessage("产品不存在！");
        }
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

    @Override
    public ServerResponse<PageInfo<ProductDetail>> getList(Product product, String categoryName,BigDecimal fromPrice, BigDecimal toPrice, Integer pageNum, Integer pageSize) {


        PageHelper.startPage(pageNum,pageSize,true);
        List<Product> products=productMapper.getList(product,fromPrice,toPrice);
        List<Category> categoryList=Lists.newArrayList();
        Category category=new Category();
        category.setName(categoryName);
        if(StringUtils.isNotEmpty(categoryName)){
            List<Category> categoryResult=categoryMapper.selectByKeySelective(category);
            if(categoryResult==null){
                return ServerResponse.createByErrorMessage("没有该分类！");
            }

            //categoryList=cateGoryService.getDeepSubCategory(categoryResult).getData();
        }


        if(products.isEmpty()){
            return ServerResponse.createByErrorMessage("产品不存在！");
        }
        List<ProductDetail> resultList=new ArrayList<>();
        for(Product item:products){
            if(Objects.equals(item.getStatus(),Const.ProductStatusEnum.ON_SALE.getCode()))
                resultList.add(assembleProductDetail(item));
        }
        if(resultList.isEmpty()){
            return ServerResponse.createByErrorMessage("产品已经售罄或者已经下架！");
        }
        PageInfo<ProductDetail> pageResult=new PageInfo<>(resultList);
        return ServerResponse.createBySuccess(pageResult);

    }
}

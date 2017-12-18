package com.imall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.imall.common.ServerResponse;
import com.imall.pojo.Product;
import com.imall.service.IProductService;
import com.imall.vo.ProductDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@Controller
@RequestMapping("/product/*")
public class ProductController {

    @Autowired
    private IProductService productService;

    @RequestMapping("detail")
    @ResponseBody
    public ServerResponse<PageInfo<ProductDetail>> detail(Product product,
                                                         @RequestParam(required = false,value = "categoryName") String categoryName,
                                                         @RequestParam(required = false, value = "fromPrice") String fromP,
                                                         @RequestParam(required = false, value = "toPrice") String toP,
                                                         @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum,
                                                         @RequestParam(defaultValue = "10", value = "pageSize") Integer pageSize) {

        BigDecimal fromPrice = null;
        BigDecimal toPrice = null;
        try {
            if (fromP != null && toP != null) {
                toPrice = BigDecimal.valueOf(Double.valueOf(toP));

                fromPrice = BigDecimal.valueOf(Double.valueOf(fromP));
            }
        } catch (NumberFormatException e) {
            return ServerResponse.createByErrorMessage("数据转化异常!");
        }
        return productService.managerList(product, fromPrice, toPrice, pageNum, pageSize);
    }
}

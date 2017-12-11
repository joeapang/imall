package com.imall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.imall.common.ResponseCode;
import com.imall.common.ServerResponse;
import com.imall.pojo.Product;
import com.imall.pojo.User;
import com.imall.service.IProductService;
import com.imall.service.IUserService;
import com.imall.vo.ProductDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/manage/products/")
@SessionAttributes("currentUser")
public class ProductManagerController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @RequestMapping(value = "productSave", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> productSave(Model model, Product product) {


        User user = (User) model.asMap().get("currentUser");
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录！");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.createBySuccessMessage("无权限操作！");
        }
    }

    @RequestMapping(value = "getProductDetail", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<ProductDetail> getProductDetail(Model model, Integer productId) {
        User user = (User) model.asMap().get("currentUser");
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录！");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.getProductDetails(productId);
        } else {
            return ServerResponse.createBySuccessMessage("无权限操作！");
        }


    }

    @RequestMapping(value = "getList", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo<ProductDetail>> getList(Model model,
                                                  @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum,
                                                  @RequestParam(defaultValue = "10", value = "pageSize") Integer pageSize, Product product) {
        User user = (User) model.asMap().get("currentUser");
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录！");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.getList(product, pageNum, pageSize);
        } else {
            return ServerResponse.createBySuccessMessage("无权限操作！");
        }


    }


}

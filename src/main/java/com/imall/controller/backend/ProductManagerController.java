package com.imall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.imall.common.ResponseCode;
import com.imall.common.ServerResponse;
import com.imall.pojo.Product;
import com.imall.pojo.User;
import com.imall.service.IFileService;
import com.imall.service.IProductService;
import com.imall.service.IUserService;
import com.imall.utils.PropertiesUtils;
import com.imall.vo.ProductDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("/manage/products/")
@SessionAttributes("currentUser")
public class ProductManagerController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

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
            return iProductService.managerProductDetails(productId);
        } else {
            return ServerResponse.createBySuccessMessage("无权限操作！");
        }


    }

    @RequestMapping(value = "getList", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<PageInfo<ProductDetail>> getList(Model model,
                                                           @RequestParam(required = false, value = "fromPrice") String fromP,
                                                           @RequestParam(required = false, value = "toPrice") String toP,
                                                           @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum,
                                                           @RequestParam(defaultValue = "10", value = "pageSize") Integer pageSize, Product product) {
        User user = (User) model.asMap().get("currentUser");
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录！");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            BigDecimal fromPrice = null;
            BigDecimal toPrice = null;
            try {
                if (fromP != null && toP != null) {
                    toPrice = BigDecimal.valueOf(Double.valueOf(toP));

                    fromPrice = BigDecimal.valueOf(Double.valueOf(fromP));
                }
            } catch (NumberFormatException e) {
                return ServerResponse.createByErrorMessage("数据转化异常");
            }

            return iProductService.managerList(product, fromPrice, toPrice, pageNum, pageSize);
        } else {
            return ServerResponse.createBySuccessMessage("无权限操作！");
        }


    }


    @RequestMapping(value = "getList", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload(MultipartFile file, HttpServletRequest request) {

        String path = request.getSession().getServletContext().getRealPath("upload");

        String targetFileName=iFileService.upload(file,path);
        String url= PropertiesUtils.getProperties("ftp.server.http.prefix")+targetFileName;
        Map fileMap= Maps.newHashMap();
        fileMap.put("uri",targetFileName);
        fileMap.put("url",url);

        return ServerResponse.createBySuccess(fileMap);
    }


}

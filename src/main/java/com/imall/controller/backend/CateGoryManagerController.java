package com.imall.controller.backend;

import com.imall.common.ServerResponse;
import com.imall.pojo.Category;
import com.imall.pojo.User;
import com.imall.service.ICateGoryService;
import com.imall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/manage/category/")
@SessionAttributes("currentUser")
public class CateGoryManagerController {
    @Autowired
    IUserService iUserService;
    @Autowired
    ICateGoryService iCateGoryService;

    @RequestMapping(value = "addCategory", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(Model model, String categoryName,
                                      @RequestParam(value = "parentID", defaultValue = "0") int parentId) {

        User user = (User) model.asMap().get("currentUser");
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录！");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {

            return iCateGoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作！");
        }
    }

    @RequestMapping(value = "updateCategory", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse updateCategory(Model model, String categoryName, Integer categoryId, Integer parentId) {
        User user = (User) model.asMap().get("currentUser");
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录！");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCateGoryService.updateCategory(categoryName, categoryId, parentId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作！");
        }
    }

    @RequestMapping(value = "getSubParallelCategory", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<List<Category>> getSubParallelCategory(Model model,
                                                                 @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {

        User user = (User) model.asMap().get("currentUser");
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录！");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            Category category = new Category();
            category.setParentId(parentId);
            return iCateGoryService.getByKeySelective(category);
        } else {

            return ServerResponse.createByErrorMessage("无权限操作！");
        }
    }

    @RequestMapping(value = "getDeepSubCategory", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<List<Category>> getDeepSubCategory(Model model,
                                                             @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) model.asMap().get("currentUser");
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录！");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            Category category = new Category();
            category.setId(categoryId);
            return iCateGoryService.getDeepSubCategory(category);
        } else {

            return ServerResponse.createByErrorMessage("无权限操作！");
        }
    }
}

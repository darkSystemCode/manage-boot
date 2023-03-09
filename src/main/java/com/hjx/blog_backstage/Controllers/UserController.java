package com.hjx.blog_backstage.Controllers;

import com.hjx.blog_backstage.Entitys.Perms;
import com.hjx.blog_backstage.Entitys.Roles;
import com.hjx.blog_backstage.Entitys.User;
import com.hjx.blog_backstage.Services.UserService;
import com.hjx.blog_backstage.Utils.Result;
import com.hjx.blog_backstage.Utils.ResultUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 用户管理类，处理登录，注册，校验等
 */
@RestController
@RequestMapping("/user")
@Api(value = "用户管理接口", tags = "UserController")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/getUser")
    @ApiOperation(value = "获取全部用户信息", notes = "获取信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页页码", required = true, dataType = "int", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "当前页展示记录数", required = true, dataType = "int", example = "10")
    })
    public Result getUser(@RequestParam("page") Integer page,
                          @RequestParam("pageSize") Integer pageSize) {
        Map<String, Object> allUser = userService.getUser(page, pageSize);
        if (allUser.size() > 0) {
            return ResultUtil.success(allUser);
        }
        return ResultUtil.fail();
    }

    @GetMapping(value = "/sendCheckCode")
    public Result getCheckCode(String email, HttpServletRequest request) {
        Integer checkCode = userService.setCheckCode(email, request);
        if (checkCode == 200) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @RequestMapping(value = "/getCheckCode", method = RequestMethod.GET)
    public Result checkCode(String code, HttpServletRequest request) {
        Integer resultCode = userService.getCheckCode(code, request);
        if (resultCode == 200) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    /*
     * 注册账号
     * username
     * password
     * email
     * */
    @PostMapping(value = "/toRegister")
    @ApiOperation(value = "用户注册接口", notes = "注册用户账号信息")
    public Result register(@RequestBody @ApiParam(name = "User", value = "用户账号实体类", required = true) User user) {
        Integer integer = userService.registerUser(user);
        if (integer != 0) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    /**
     * 修改密码
     */
    @RequestMapping(value = "/updatePassword/{username}", method = RequestMethod.POST)
    @ApiOperation(value = "修改用户账号密码", notes = "通过用户名修改当前用户账号的密码")
    public Result updatePassword(@PathVariable(value = "username") @ApiParam(name = "username", value = "用户名", required = true) String username,
                                 @RequestBody @ApiParam(name = "User", value = "用户账号实体类", required = true) User user) {
        //新旧密码不能一致
        if (user.getPassword().equals(user.getOldPassword())) {
            return ResultUtil.fail(201, "新旧密码不能一致！");
        } else {
            //校验旧密码是否正确
            Integer checkRes = userService.checkOldPassW(user.getOldPassword());
            if (checkRes == 201) {
                return ResultUtil.fail(201, "旧密码不存在，无法修改密码！");
            } else if (checkRes == 0) {
                //修改密码
                Integer integer = userService.updatePassword(user.getPassword(), username);
                if (integer != 0) {
                    return ResultUtil.success(200, "密码修改成功！");
                } else {
                    return ResultUtil.fail(201, "密码修改失败");
                }
            }
        }
        return ResultUtil.fail(201, "密码修改失败");
    }

    @RequestMapping(value = "/editUserInfo/{userID}", method = RequestMethod.POST)
    @ApiOperation(value = "修改用户账号信息", notes = "通过用户id，修改用户账号信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页页码", required = true, dataType = "int", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "当前页展示记录数", required = true, dataType = "int", example = "3")
    })
    public Result editUserInfo(@PathVariable("userID") @ApiParam(name = "userID", value = "用户id", required = true) String userID,
                               @RequestBody @ApiParam(name = "User", value = "用户账号实体类", required = true) User user,
                                @RequestParam("page") Integer page,
                               @RequestParam("pageSize") Integer pageSize) {
        user.setUserID(userID);
        Map<String, Object> resultMap = userService.editUserInfo(user, page, pageSize);
        if (resultMap.size() > 0) {
            return ResultUtil.success(resultMap);
        }
        return ResultUtil.fail("修改用户信息失败");
    }
    
    @GetMapping(value = "/delUserAccount/{user_id}")
    @ApiOperation(value = "删除用户账号信息", notes = "通过用户id，删除用户账号信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user_id", value = "用户id", required = true),
            @ApiImplicitParam(name = "page", value = "当前页页码", required = true, dataType = "int", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "当前页展示记录数", required = true, dataType = "int", example = "3")
    })
    public Result delUserAccount(@PathVariable("user_id") String user_id,
                                 @RequestParam Integer page,
                                 @RequestParam Integer pageSize) {
        Map<String, Object> resultMap = userService.delUser(user_id, page, pageSize);
        if(resultMap.size() > 0) {
            return ResultUtil.success(resultMap);
        }
        return ResultUtil.fail();
    }

    @GetMapping(value = "/checkEmail/{email}")
    public Result checkEmail(@PathVariable String email) {
        User user = userService.checkEmail(email);
        if (!StringUtils.isEmpty(user)) {
            return ResultUtil.fail("邮箱已存在，请重新填写！");
        }
        return ResultUtil.success();
    }

    @PostMapping(value = "/addAccount")
    @ApiOperation(value = "添加新账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页页码", required = true, dataType = "int", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "当前页展示记录数", required = true, dataType = "int", example = "3")
    })
    public Result addAccount(@RequestBody @ApiParam(name = "user", value = "用户实体类user", required = true) User user,
                             @RequestParam("page") Integer page,
                             @RequestParam("pageSize") Integer pageSize) {
        if (!StringUtils.isEmpty(user)) {
            Map<String, Object> resultMap = userService.addAccount(user, page, pageSize);
            if (resultMap.size() > 0) {
                return ResultUtil.success("添加" + user.getUsername() + "的账号成功", resultMap);
            }

        }
        return ResultUtil.fail();
    }

    @PostMapping(value = "/setPerms")
    @ApiOperation(value = "设置用户账号权限接口", notes = "添加账号权限成功，提示权限设置成功，否则，返回错误信息！")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "当前页展示记录数", required = true)
    })
    public Result setPerms(@RequestBody @ApiParam(name = "Perms", value = "权限实体类", required = true) Perms perms,
                           @RequestParam("page") Integer page,
                           @RequestParam("pageSize") Integer pageSize) {
        if (!StringUtils.isEmpty(perms)) {
            Map<String, Object> result = userService.setUserPerms(perms, page, pageSize);
            if (result.size() > 0) {
                return ResultUtil.success("添加" + perms.getPermsName() + "权限成功", result);
            }
        }
        return ResultUtil.fail();
    }

    @PostMapping(value = "/setRoles")
    @ApiOperation(value = "设置用户账号角色接口", notes = "设置用户账号角色成功，返回成功信息，否则，返回错误信息！")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "当前页展示记录数", required = true)
    })
    public Result setRoles(@RequestBody @ApiParam(name = "Roles", value = "用户角色实体类", required = true) Roles roles,
                           @RequestParam("page") Integer page,
                           @RequestParam("pageSize") Integer pageSize) {
        if (!StringUtils.isEmpty(roles)) {
            Map<String, Object> result = userService.setUserRoles(roles, page, pageSize);
            if (result.size() > 0) {
                return ResultUtil.success("添加" + roles.getRolesName() + "角色成功", result);
            }
        }
        return ResultUtil.fail();
    }

    @GetMapping(value = "/getPerms")
    @ApiOperation(value = "获取用户权限接口", notes = "通过用户id获得当前用户下的所有权限")
    public Result getPerms() {
        List<Object> permsAndRoles = userService.getPermsAndRoles();
        if (permsAndRoles.size() > 0) {
            return ResultUtil.success(permsAndRoles);
        }
        return ResultUtil.fail();
    }

    @GetMapping(value = "/getPerms/{page}/{pageSize}")
    @ApiOperation(value = "获取用户权限接口", notes = "分页形式显示全部权限，不分用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "当前页展示记录数", required = true)
    })
    public Result getPerms(@PathVariable Integer page, @PathVariable Integer pageSize) {
        Map<String, Object> allPerms = userService.getPerms(page, pageSize);
        if (allPerms.size() > 0) {
            return ResultUtil.success(allPerms);
        }
        return ResultUtil.fail();
    }

    @GetMapping(value = "/getRoles/{page}/{pageSize}")
    @ApiOperation(value = "获取用户角色接口", notes = "分页形式显示全部角色，不分用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "当前页展示记录数", required = true)
    })
    public Result getRoles(@PathVariable Integer page, @PathVariable Integer pageSize) {
        Map<String, Object> allRoles = userService.getRoles(page, pageSize);
        if (allRoles.size() > 0) {
            return ResultUtil.success(allRoles);
        }
        return ResultUtil.fail();
    }

    @GetMapping(value = "/delPerms/{permsName}")
    @ApiOperation(value = "删除权限接口", notes = "通过权限名删除权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "permsName", value = "权限名称", required = true),
            @ApiImplicitParam(name = "page", value = "当前页页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "当前页展示记录数", required = true)
    })
    public Result delPerms(@PathVariable String permsName, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        Map<String, Object> map = userService.delPerms(permsName, page, pageSize);
        if (map.size() > 0) {
            return ResultUtil.success("删除" + permsName + "权限成功！", map);
        }
        return ResultUtil.fail();
    }

    @GetMapping(value = "/delRoles/{rolesName}")
    @ApiOperation(value = "删除角色接口", notes = "通过角色名删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rolesName", value = "角色名称", required = true),
            @ApiImplicitParam(name = "page", value = "当前页页码", required = true),
            @ApiImplicitParam(name = "pageSize", value = "当前页展示记录数", required = true)
    })
    public Result delRoles(@PathVariable String rolesName, @RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        Map<String, Object> map = userService.delRoles(rolesName, page, pageSize);
        if (map.size() > 0) {
            return ResultUtil.success("删除" + rolesName + "角色成功！", map);
        }
        return ResultUtil.fail();
    }
}
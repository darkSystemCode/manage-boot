package com.hjx.blog_backstage.Controllers;

import com.hjx.blog_backstage.Entitys.ChildNav;
import com.hjx.blog_backstage.Entitys.Nav;
import com.hjx.blog_backstage.Services.NavService;
import com.hjx.blog_backstage.Utils.Result;
import com.hjx.blog_backstage.Utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NavController {
    @Autowired
    private NavService navService;

    @RequestMapping(value = "/getNav", method = RequestMethod.GET)
    public Result getNav() {
        List<Nav> navList = navService.getNav();
        if(navList != null) {
            return ResultUtil.success(navList);
        }
        return ResultUtil.fail();
    }

    @RequestMapping(value = "/setNav" ,method = RequestMethod.POST, produces = "application/json")
    public Result setNav(@RequestBody Nav nav) {
        if(nav.getTitle() != null && nav.getPath() != null) {
            Integer setNav = navService.setNav(nav);
            if(setNav != 0) {
                return ResultUtil.success(200, "添加一级导航成功", nav.getNid());
            } else {
                return ResultUtil.fail(4000, "添加一级导航失败");
            }
        }
        return ResultUtil.fail();
    }

    @RequestMapping(value = "/setChildNav", method = RequestMethod.POST, produces = "application/json")
    public Result setChildNav(@RequestBody ChildNav childNav) {
        if(childNav.getNavNumber() != 0 && childNav.getTitle() != null && childNav.getPath() != null) {
            Integer setChildNav = navService.setChildNav(childNav);
            if(setChildNav != 0) {
                return ResultUtil.success(200,"添加子导航成功",childNav.getNcid());
            } else {
                return ResultUtil.success(4000,"添加子导航失败");
            }
        }
        return ResultUtil.fail();
    }

    @RequestMapping(value = "/getChildNav", method = RequestMethod.GET, produces = "application/json")
    public Result getChildNav(@RequestParam(required = true) Integer parentIndex) {
        if(parentIndex != null) {
            List<ChildNav> childNav = navService.getChildNav(parentIndex);
            if(childNav != null && childNav.size() != 0) {
                return ResultUtil.success(childNav);
            }
        }
        return ResultUtil.fail();
    }

    @RequestMapping(value="/getHomeNav", method=RequestMethod.GET, produces = "application/json")
    public Result getHomeNav() {
        List<Nav> homeNav = navService.getHomeNav();
        if(homeNav != null) {
            return ResultUtil.success(homeNav);
        }
        return ResultUtil.fail();
    }

    @GetMapping(value = "/deleteParentNav/{id}")
    public Result deleteParentNav(@PathVariable("id") Integer id) {
        Integer integer = navService.deleteParentNav(id);
        if(integer > 0) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @GetMapping(value = "/deleteChildNav/{id}")
    public Result deleteChildNav(@PathVariable("id") Integer id) {
        Integer integer = navService.deleteChildNav(id, null);
        if(integer > 0) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @GetMapping(value = "/deleteAllNav/{parentId}")
    public Result deleteAllNav(@PathVariable("parentId") Integer parentId) {
        Integer integer = navService.deleteAllNav(parentId);
        if(integer > 0) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }
}

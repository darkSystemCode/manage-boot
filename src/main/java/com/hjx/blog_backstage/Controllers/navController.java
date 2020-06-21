package com.hjx.blog_backstage.Controllers;

import com.hjx.blog_backstage.Entitys.ChildNav;
import com.hjx.blog_backstage.Entitys.Nav;
import com.hjx.blog_backstage.Services.navService;
import com.hjx.blog_backstage.Utils.Result;
import com.hjx.blog_backstage.Utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class navController {
    private static ResultUtil result = new ResultUtil();
    @Autowired
    private navService navService;

    @RequestMapping(value = "/getNav")
    public Result getNav() {
        List<Nav> navList = navService.getNav();
        if(navList != null) {
            return result.success(navList);
        }
        return result.fail();
    }

    @RequestMapping(value = "/setNav" ,method = RequestMethod.POST, produces = "application/json")
    public Result setNav(@RequestBody Nav nav) {
        if(nav.getTitle() != null && nav.getPath() != null) {
            Integer setNav = navService.setNav(nav);
            if(setNav != 0) {
                return result.success(13,"添加一级导航标题成功");
            } else {
                return result.success(14,"添加一级导航标题失败");
            }
        }
        return result.fail();
    }

    @RequestMapping(value = "/setChildNav", method = RequestMethod.POST, produces = "application/json")
    public Result setChildNav(@RequestBody ChildNav childNav) {
        if(childNav.getNavNumber() != 0 && childNav.getChildTitle() != null && childNav.getChildPath() != null) {
            Integer setChildNav = navService.setChildNav(childNav);
            if(setChildNav != 0) {
                return result.success(15,"添加子导航成功");
            } else {
                return result.success(16,"添加子导航失败");
            }
        }
        return result.fail();
    }

    @RequestMapping(value = "/getChildNav", method = RequestMethod.GET, produces = "application/json")
    public Result getChildNav(@RequestParam(required = true) Integer parentIndex) {
        if(parentIndex != null) {
            List<ChildNav> childNav = navService.getChildNav(parentIndex);
            if(childNav != null && childNav.size() != 0) {
                return result.success(childNav);
            }
        }
        return result.fail();
    }

    @RequestMapping(value="/getHomeNav", method=RequestMethod.GET, produces = "application/json")
    public Result getHomeNav() {
        List<Nav> homeNav = navService.getHomeNav();
        if(homeNav != null) {
            return result.success(homeNav);
        }
        return result.fail();
    }
}

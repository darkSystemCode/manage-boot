package com.hjx.blog_backstage.Controllers;

import com.hjx.blog_backstage.Entitys.Mine;
import com.hjx.blog_backstage.Services.mineService;
import com.hjx.blog_backstage.Utils.Result;
import com.hjx.blog_backstage.Utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class mineController {
    private static ResultUtil result = new ResultUtil();
    @Autowired
    private mineService mineService;

    @RequestMapping(value = "/setMine", method = RequestMethod.POST, produces = "application/json")
    public Result setMine(@RequestBody Mine mine) {
        if(mine.getAuthor() != null && mine.getSkill() != null && mine.getIntroduce() != null) {
            Integer setMine = mineService.setMine(mine);
            if(setMine != 0) {
                return result.success(17, "保存个人信息成功");
            } else {
                return result.success(18, "保存个人信息失败");
            }
        }
        return result.fail();
    }

    @RequestMapping(value = "/saveAvatar", method = RequestMethod.POST)
    public Result saveAvatar(String avatar) {
        return result.success();
    }

    @RequestMapping(value = "/getProfile", method = RequestMethod.GET, produces = "application/json")
    public Result getProfile(String author) {
        if(author != null) {
            //获得个人信息
            Mine profile = mineService.getProfile(author);
            //获得汇总的文章、评论、点赞、阅读总数

            if (profile != null) {
                return result.success(profile);
            }
        }
        return result.fail();
    }

}

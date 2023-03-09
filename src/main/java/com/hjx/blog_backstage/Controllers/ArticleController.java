package com.hjx.blog_backstage.Controllers;

import com.hjx.blog_backstage.Entitys.Article;
import com.hjx.blog_backstage.Entitys.ArticleOther;
import com.hjx.blog_backstage.Services.ArticleService;
import com.hjx.blog_backstage.Utils.Result;
import com.hjx.blog_backstage.Utils.ResultUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hjx
 * @remark 文章类
 * @since 2020-03-27
 */
@RestController
public class ArticleController {
    private static ResultUtil result = new ResultUtil();

    @Autowired
    private ArticleService articleService;

    /**
     * 保存后台编写的文章
     *
     * @return 如果文章保存成功 返回 “011”
     * 否则返回 “012”
     * 如果图片失败，则全部失败 返回“010”
     * @params Article: 文章实体类
     */
    @RequestMapping(value = "/submitArticle", method = RequestMethod.POST)
    public Result setFuillE(@RequestBody Article article) {
        if (article.getTitle() != null && article.getContent() != null && article.getType() != null && article.getCategory() != null) {
            Integer resultCode = articleService.setFEditor(article);
            if (resultCode == 11) {
                return result.success(resultCode, "文章发布成功");
            } else if (resultCode == 12) {
                return result.success(resultCode, "文章发布失败,请联系后台");
            } else if (resultCode == 10) {
                return result.success(resultCode, "插入图片失败");
            }
        }
        return result.fail();
    }

    /**
     * 文章的修改编辑
     *
     * @return 如果文章保存成功 返回 “011”
     * 否则返回 “012”
     * 如果图片失败，则全部失败 返回“010”
     * @params Article: 文章实体类
     */
    @PostMapping(value = "/updateArticle")
    public Result updateArticle(@RequestBody Article article) {
        Integer resultCode = articleService.updateArticle(article);
        return result.success(resultCode, "");
    }

    /**
     * 获得文章，只抽取文章的摘要，不是原文
     *
     * @return 如果article不为空 则返回查询到的文章（内容为摘要）
     * 状态码：”200“
     * @params category: 前端传过来的文章分类，如果没有则为空
     * start: 分页的开始位置
     * end: 分页查询数据条数，结尾
     */
    @RequestMapping(value = "/getHomeArticle", method = RequestMethod.GET, produces = "application/json")
    public Result getHomeArticle(String category, Integer start, Integer end) {
        List<Article> article = articleService.getHomeArticle(category, start, end);
        Integer articleTotal = articleService.getArticleTotal();
        if (article != null && articleTotal != 0) {
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("article", article);
            resultMap.put("length", articleTotal);
            result.success(resultMap);
        }
        return result.fail();
    }

    /**
     * 获得文章的原文
     *
     * @params articleNum: 文章的索引编号
     */
    @RequestMapping(value = "/getArticle/{articleNum}", method = RequestMethod.GET)
    public Result getArticle(@PathVariable("articleNum") String articleNum) {
        if (articleNum != null && articleNum != "") {
            Article article = articleService.getArticle(articleNum);
            if (article != null) {
                return result.success(article);
            }
        }
        return result.fail();
    }

    /**
     * 添加文章的阅读量
     */
    @RequestMapping(value = "/addReadNum", method = RequestMethod.POST)
    public Result addReadNum(@RequestBody ArticleOther articleOther) {
        String code = articleService.addReadNum(articleOther);
        if (code == "200") {
            return result.success();
        }
        return result.fail();
    }

    /**
     * 添加文章的点赞量
     */
    @RequestMapping(value = "/addLike", method = RequestMethod.POST)
    public Result addLike(@RequestBody ArticleOther articleOther) {
        Integer integer = articleService.addLike(articleOther.getArticleNum());
        if (integer != 0) {
            return result.success();
        } else {
            return result.fail();
        }
    }


    /**
     * 排名热榜
     */
    @RequestMapping(value = "/hotOrder", method = RequestMethod.GET, produces = "application/json")
    public Result hotOrder() {
        List<Article> article = articleService.hotOrder();
        if (article != null) {
            return result.success(article);
        } else {
            return result.fail();
        }
    }

    /**
     * 文章管理
     * 返回全部文章数据
     */
    @RequestMapping(value = "/getArticle", method = RequestMethod.GET)
    @ApiOperation(value = "获取文章，以分页形式展示")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "文章类型", required = true),
            @ApiImplicitParam(name = "articleNum", value = "文章编号", required = true),
            @ApiImplicitParam(name = "startTime", value = "文章编写时间", required = true),
            @ApiImplicitParam(name = "endTime", value = "文章结束时间", required = true),
            @ApiImplicitParam(name = "page", value = "当前页页码", required = true, dataType = "int", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "当前页展示记录数", required = true, dataType = "int", example = "6")
    })
    public Result getArticle(String type,
                                String articleNum,
                                String startTime,
                                String endTime,
                                @RequestParam("page") Integer page,
                                @RequestParam("pageSize") Integer pageSize) {
        Map<String, Object> articleMap = articleService.getAllArticle(type, articleNum, startTime, endTime, page, pageSize);

        if (articleMap.size() > 0) {
            return result.success(articleMap);
        }
        return result.fail();
    }

    @GetMapping("/getNewArticle")
    @ApiOperation(value = "获取最新发布的文章")
    public Result getNewArticle(@RequestParam(value = "size", required = false) @ApiParam(name = "size" ,value = "查询记录条数", defaultValue = "6") Integer size) {
        List<Article> newArticle = articleService.getNewArticle(size);
        if(newArticle.size() > 0) {
            return ResultUtil.success(newArticle);
        }
        return ResultUtil.fail();
    }

    @GetMapping("/getFeturedArticle")
    @ApiOperation(value="获取精选文章")
    public Result getFeturedArticle(@RequestParam("size") @ApiParam(name = "size" ,value = "查询记录条数") Integer size) {
        List<Article> feturedArticle = articleService.getFeturedArticle(size);
        if(feturedArticle.size() > 0) {
            return ResultUtil.success(feturedArticle);
        }
        return ResultUtil.fail();
    }

}

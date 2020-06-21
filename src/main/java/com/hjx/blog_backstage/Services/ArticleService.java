package com.hjx.blog_backstage.Services;

import com.hjx.blog_backstage.Entitys.ArtImg;
import com.hjx.blog_backstage.Entitys.Article;
import com.hjx.blog_backstage.Entitys.ArticleOther;
import com.hjx.blog_backstage.Mappers.ArticleMapper;
import com.hjx.blog_backstage.Utils.regularUtil;
import com.hjx.blog_backstage.Utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Transactional
    public Integer setFEditor(Article article) {
        String articleNum = UUIDUtil.get_32UUID();
        article.setCategory(article.getCategory().toLowerCase());
        article.setArticleNum(articleNum);
        article.setCreateTime(Calendar.getInstance().getTimeInMillis());
        //获得抽离img内容的HTML内容
        Map<String, ArrayList<String>> map = regularUtil.getImgContent(article.getContent(), articleNum);
        article.setContent(map.get("content").get(0));
        //开启事务 二者皆要成功才返回
        Integer setFEditor = 0;
        try{
            setFEditor = articleMapper.setFEditor(article);
        }catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        //判断是否需要插入图片
        if(map.size() == 1) { //不需要插入图片
            if(setFEditor != 0) {
                return 11;
            } else {
                return 12;
            }
        } else { //插入图片
            //存储图片
            ArtImg artImg = new ArtImg();
            artImg.setArticleNum(articleNum);
            Integer saveArtImg = 0;
            try{
                for (Map.Entry<String, ArrayList<String>> item: map.entrySet()) {
                    //循环得到抽离出的img资源
                    if(item.getKey() != "content") {
                        artImg.setImgKey(item.getKey());
                        artImg.setImg(item.getValue().get(0));
                        saveArtImg = articleMapper.saveArtImg(artImg);
                    }
                }
                if(saveArtImg != 0 && setFEditor !=0) {
                    return 11;
                } else {
                    return 12;
                }
            }catch (Exception e) {
                e.printStackTrace();
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return 10;
            }
        }
    }

    public List<Article> getHomeArticle(String category, Integer start, Integer end) {
        List<Article> article = articleMapper.getHomeArticle(category, start, end);
        for(Article item: article) {
            String regResult = regularUtil.filterH(item.getContent());
            item.setContent(regResult);
        }
        return article;
    }

    public Integer getArticleTotal() {
        return articleMapper.getArticleTotal();
    }

    @Transactional
    public Article getArticle(String articleNum) {
        //这里得到的文章原文，如果原文包含图片，图片的内容是保存文章前的文章索引编号，所以需要拿到图片的资源覆盖上去
        Article article = articleMapper.getArticle(articleNum);
        /*
        * 得到图片资源，并需要把当前图片的索引匹配文章的src内容，
        * 如果匹配上，则把索引对应的图片资源覆盖到原文的img-src
        * */
        List<ArtImg> articleImg = articleMapper.getArticleImg(articleNum);
        if(articleImg.size() != 0) {
            LinkedHashMap<String, String> imgs = new LinkedHashMap<>();
            for (ArtImg item: articleImg) {
                imgs.put(item.getImgKey(), item.getImg());
            }
            String replaceContent = regularUtil.replaceImg(article.getContent(), imgs);
            article.setContent(replaceContent);
        }
        return article;
    }
    
    public String addReadNum(ArticleOther articleOther) {
        Integer updateReadNum = 0;
        Integer addReadNum = 0;
        Integer isExist = articleMapper.isArticleOther(articleOther.getArticleNum());
        //存在
        if(isExist != 0) {
            //存在则直接阅读量自加1
            updateReadNum = articleMapper.updateReadNum(articleOther.getArticleNum());
        } else {
            //不存在则先创建数据 默认值为1
            addReadNum = articleMapper.addReadNum(articleOther);
        }

        if(updateReadNum != 0 || addReadNum != 0) {
            return "200";
        }
        return "0";
    }

    public Integer addLike(String articleNum) {
        return articleMapper.addLike(articleNum);
    }

    public List<Article> hotOrder() {
        List<Article> article = articleMapper.hotOrder();
        return article;
    }

    public List<Article> getAllArticle(String type,
                                       String articleNum,
                                       String startTime,
                                       String endTime,
                                       Integer currPage) {
        //每次查询数据总数
        Integer size;
        if(currPage == 0 || currPage == 1) {
            size = 0;
        } else {
            size = currPage * 10;
        }
        List<Article> allArticle = articleMapper.getAllArticle(type, articleNum, startTime, endTime, size);
        for (Article item: allArticle) {
            String resultText = regularUtil.filterH(item.getContent());
            item.setContent(resultText);
        }
        return allArticle;
    }

    @Transactional
    public Integer updateArticle(Article article) {
        //修改时间
        long timeInMillis = Calendar.getInstance().getTimeInMillis();
        //抽离图片
        Map<String, ArrayList<String>> imgContent = regularUtil.getImgContent(article.getContent(), article.getArticleNum());

        article.setUpdateTime(timeInMillis);
        article.setCategory(article.getCategory().toLowerCase());
        article.setContent(imgContent.get("content").get(0));

        Integer updateArticle = 0;
        try{
            updateArticle = articleMapper.updateArticle(article);
        }catch (Exception e) {
            e.printStackTrace();
            //回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        //判断是否需要插入图片
        if(imgContent.size() == 1) { //不需要插入图片

            //判断当前文章是否有图片存储在数据库
            Integer existImg = articleMapper.existImg(article.getArticleNum());

            /*
             * 1.存在修改前是有图片，修改后把之前的图片删除了，所以需要删除之前存在数据库的图片
             * 2.修改前后都有照片，在修改前，把之前的图片全部删除，插入新的图片
             * */
            HashMap<String, ArrayList<String>> imgMap = new HashMap<>();
            for (Map.Entry<String, ArrayList<String>> item: imgContent.entrySet()) {
                if(item.getKey() != "content") {
                    imgMap.put(item.getKey(), item.getValue());
                }
            }
            Integer delArImg =0;
            if(existImg != 0) {
                if(imgMap.size() != 0) {
                    //删除图片
                    delArImg = articleMapper.delArImg(article.getArticleNum());
                }
            }

            if(updateArticle != 0) {
                return 19;
            } else {
                return 20;
            }
        } else { //插入图片
            /*
            * 得到图片的资源和数量，如果当前图片数量等于数据库数量，则全部修改，如果大于数据库记录量，则等于前的修改，后的添加
            * */
            Integer delImg = articleMapper.delArImg(article.getArticleNum());
            if(delImg != 0) {
                //插入新的图片资源
                ArtImg artImg = new ArtImg();
                artImg.setArticleNum(article.getArticleNum());
                Integer saveArtImg = 0;
                try{
                    for (Map.Entry<String, ArrayList<String>> item: imgContent.entrySet()) {
                        //循环得到抽离出的img资源
                        if(item.getKey() != "content") {
                            artImg.setImgKey(item.getKey());
                            artImg.setImg(item.getValue().get(0));
                            saveArtImg = articleMapper.saveArtImg(artImg);
                        }
                    }
                    if(saveArtImg != 0 && updateArticle !=0) {
                        return 19;
                    } else {
                        return 20;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return 10;
                }
            }
        }
        return 0;
    }
}


package com.hjx.blog_backstage.Mappers;

import com.hjx.blog_backstage.Entitys.ArtImg;
import com.hjx.blog_backstage.Entitys.Article;
import com.hjx.blog_backstage.Entitys.ArticleOther;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper {

    public Integer setFEditor(Article article);

    public Integer saveArtImg(ArtImg artImg);

    public List<Article> getHomeArticle(@Param(value = "category") String category, @Param(value="start") Integer start, @Param(value="end") Integer end);

    public Integer getArticleTotal();

    public Article getArticle(String articleNum);

    public List<ArtImg> getArticleImg(String articleNum);

    public Integer addReadNum(ArticleOther articleOther);

    public Integer isArticleOther(String articleNum);

    public Integer updateReadNum(String articleNum);

    public Integer addLike(String articleNum);

    public List<Article> hotOrder();

    public List<Article> getAllArticle(@Param(value = "type") String type,
                                       @Param(value = "articleNum") String articleNum,
                                       @Param(value = "startTime") String startTime,
                                       @Param(value = "endTime") String endTime,
                                       @Param(value = "page") Integer page,
                                        Integer pageSize);

    public Integer updateArticle(Article article);

    public Integer existImg(String articleNum);

    public Integer delArImg(String articleNum);

    List<Article> getNewArticle(@Param("size") Integer size);

    List<Article> getFeturedArticle(Integer size);
}

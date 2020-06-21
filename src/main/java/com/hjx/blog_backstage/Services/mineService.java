package com.hjx.blog_backstage.Services;

import com.hjx.blog_backstage.Entitys.ArticleOther;
import com.hjx.blog_backstage.Entitys.Mine;
import com.hjx.blog_backstage.Mappers.mineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class mineService {
    @Autowired
    private mineMapper mineMapper;

    public Integer setMine(Mine mine) {
        return mineMapper.setMine(mine);
    }

    public Mine getProfile(String author) {
        //获得文章总数
        Integer articleTotal = mineMapper.getArticleTotal();
        //获得全部文章的阅读、点赞总数
        Mine otherTotal = mineMapper.getOtherTotal();
        //获得个人信息
        Mine profile = mineMapper.getProfile(author);
        profile.setArticleTotal(articleTotal);
        profile.setReadTotal(otherTotal.getReadTotal());
        profile.setLikeTotal(otherTotal.getLikeTotal());
        return profile;
    }
}

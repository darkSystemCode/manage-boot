package com.hjx.blog_backstage.Mappers;

import com.hjx.blog_backstage.Entitys.Mine;
import org.springframework.stereotype.Repository;

@Repository
public interface mineMapper {

    public Integer setMine(Mine mine);

    public Mine getProfile(String author);

    public Integer getArticleTotal();

    public Mine getOtherTotal();
}

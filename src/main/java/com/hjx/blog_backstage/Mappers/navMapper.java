package com.hjx.blog_backstage.Mappers;

import com.hjx.blog_backstage.Entitys.ChildNav;
import com.hjx.blog_backstage.Entitys.Nav;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface navMapper {

    public Integer setNav(Nav nav);

    public List<Nav> getNav();

    public Integer setChildNav(ChildNav childNav);

    public List<ChildNav> getChildNav(Integer parentIndex);

    public List<Nav> getHomeNav();

    public List<ChildNav> getHomeNavChild();
}

package com.hjx.blog_backstage.Services;

import com.hjx.blog_backstage.Entitys.ChildNav;
import com.hjx.blog_backstage.Entitys.Nav;
import com.hjx.blog_backstage.Mappers.navMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class navService {
    @Autowired
    private navMapper navMapper;

    public Integer setNav(Nav nav) {
        return navMapper.setNav(nav);
    }

    public List<Nav> getNav() {
        return navMapper.getNav();
    }

    public Integer setChildNav(ChildNav childNav) {
        return navMapper.setChildNav(childNav);
    }

    public List<ChildNav> getChildNav(Integer parentIndex) {
        return navMapper.getChildNav(parentIndex);
    }

    public List<Nav> getHomeNav(){
        //得到一级导航
        List<Nav> homeNav = navMapper.getHomeNav();
        //如果当前一级导航存在子导航，通过一级导航ID得到二级导航
        for(Nav item: homeNav) {
            Integer parentId = item.getNid();
            List<ChildNav> childNav = navMapper.getChildNav(parentId);
            item.setChildren(childNav);
        }
        return homeNav;
    }
}

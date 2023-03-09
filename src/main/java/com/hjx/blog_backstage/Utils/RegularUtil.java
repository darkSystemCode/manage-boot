package com.hjx.blog_backstage.Utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 提取富文本的img标签的src内容，并替换成当前文章的索引
 * @since 2020-03-27
 * @author hjx
 */
public class RegularUtil {
    private static Integer INDEX = 0;
    /**
     * 把富文本内容的img src内容抽离出来，单独存放到数据库中，并生成对应的图片索引到原文的src
     * @params
     *  content； 富文本HTML内容
     *  articleNum: 存储图片索引，该索引会在src抽离了图片资源后写到src，作为后面处理图片的唯一索引
     * @return
     *  返回图片存储的索引和内容
     *  抽离后的内容
     * */
    public static Map<String, ArrayList<String>> getImgContent(String content, String articleNum) {
        Map<String, ArrayList<String>> map = new LinkedHashMap<>();
        //目前img标签标示有3种表达式
        //<img alt="" src="1.jpg"/>   <img alt="" src="1.jpg"></img>     <img alt="" src="1.jpg">
        //开始匹配content中的<img />标签
        Pattern p_img = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
        Matcher m_img = p_img.matcher(content);
        boolean result_img = m_img.find();
        String str_src = "";
        if (result_img) {
            while (result_img) {
                //获取到匹配的<img />标签中的内容
                String str_img = m_img.group(2);

                //开始匹配<img />标签中的src
                Pattern p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                Matcher m_src = p_src.matcher(str_img);
                if (m_src.find()) {
                    INDEX++;
                    str_src = m_src.group(3);
                    ArrayList<String> srcList = new ArrayList<>();
                    srcList.add(str_src);
                    map.put(articleNum+"_"+INDEX, srcList);
                }
                //结束匹配<img />标签中的src 并替换已提取src的内容为文章的索引
                content = content.replace(str_src, articleNum+"_"+INDEX);
                //匹配content中是否存在下一个<img />标签，有则继续以上步骤匹配<img />标签中的src
                result_img = m_img.find();
            }
        }
        ArrayList<String> contentMap = new ArrayList<>();
        contentMap.add(content);
        map.put("content", contentMap);
        INDEX = 0;
        return map;
    }

    /**
     * 提出富文本HTML内容的文字摘要
     * @params：
     *  content：html内容
     * */
    public static String filterH(String content) { //<(img)(.*?)>|<pre(.*?)>(.*?)</pre>
        //<img src="...">的规则
        String regImg = "<(img)(.*?)>";
        Pattern pImg = Pattern.compile(regImg);
        Matcher mImg = pImg.matcher(content);
        String replace_Img = "";
        String gImg = "";
        String gPre = "";
        String replace_Pre = "";
        while (mImg.find()) {
            gImg = mImg.group();
            //把匹配到的内容全部剔除了
            replace_Img = content.replaceAll(gImg, "");
            content = replace_Img;
        }
        //匹配<pre>...</pre>规则
        String regPre = "<(pre)(.*?)>(.*?)</pre>";
        Pattern pPre = Pattern.compile(regPre);
        Matcher mPre = pPre.matcher(content);
        while (mPre.find()) {
            gPre = mPre.group();
            replace_Pre = content.replace(gPre, "");
            content = replace_Pre;
        }
        //去除全部的HTML标签
        String reg = "<[^>]+>";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(content);
        String result = "";
        while (matcher.find()) {
            String group = matcher.group();
            result = content.replaceAll(group, "");
            content = result;
        }
        if(content.toString().length() >=170) {
            return content.toString().trim().substring(0, 170)+"...";
        } else {
            return content;
        }
    }


    /**
     * 替换图片资源，把原来的图片资源替换了图片索引
     * */
    public static String replaceImg(String content,Map<String, String> imgs) {
        //目前img标签标示有3种表达式
        //<img alt="" src="1.jpg"/>   <img alt="" src="1.jpg"></img>     <img alt="" src="1.jpg">
        //开始匹配content中的<img />标签
        Pattern p_img = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
        Matcher m_img = p_img.matcher(content);
        boolean result_img = m_img.find();
        String str_src = "";
        if (result_img) {
            while (result_img) {
                //获取到匹配的<img />标签中的内容
                String str_img = m_img.group(2);

                //开始匹配<img />标签中的src
                Pattern p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                Matcher m_src = p_src.matcher(str_img);
                if (m_src.find()) {
                    //得到img标签的src内容
                    str_src = m_src.group(3);
                }
                for(Map.Entry<String, String> item: imgs.entrySet()) {
                    if(str_src.equalsIgnoreCase(item.getKey())) {
                        content = content.replace(str_src, item.getValue());
                    }
                }
                //结束匹配<img />标签中的src 并替换已提取src的内容为文章的索引
                //匹配content中是否存在下一个<img />标签，有则继续以上步骤匹配<img />标签中的src
                result_img = m_img.find();
            }
        }
        return content;
    }
}

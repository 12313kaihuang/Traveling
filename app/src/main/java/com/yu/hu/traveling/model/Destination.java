package com.yu.hu.traveling.model;

/**
 * @author Hy
 * created on 2020/04/15 10:38
 * <p>
 * 对应destnation.json的一个Destination
 **/
@SuppressWarnings("unused")
public class Destination {

    /**
     * isFragment : true
     * asStarter : true
     * needLogin : false
     * pageUrl : main/tabs/home
     * className : com.yu.hu.traveling.ui.home.HomeFragment
     * id : 1846910234
     */

    public int id;
    public String pageUrl;
    public String className;
    public boolean isFragment;
    public boolean tabPage; //是否是tab页面 即（四）个导航页面 首页 消息什么的
    public int iconRes; //icon资源id
    public boolean asStarter;
    public boolean needLogin;
}

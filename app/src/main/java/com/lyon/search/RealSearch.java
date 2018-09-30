package com.lyon.search;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:具体查询类，充当真实主题角色，它实现查询功能，提供方法search()来查询信息<br>
 * Date 9/26/2018 9:02 PM                    <br>
 */
public class RealSearch implements ISearch {
    private Map<String, String> map = new HashMap<>();

    public RealSearch() {
        map.put("a", "absolute");
        map.put("b", "book");
        map.put("c", "car");
        map.put("d", "dark");
    }

    /**
     * 搜索方法接口
     *
     * @param user    用户名
     * @param keyword 关键词
     * @return 结果
     */
    @Override
    public String search(String user, String keyword) {
        System.out.println(String.format("用户%s使用关键词%s查询信息...", user, keyword));
        return user + "搜索" + keyword + "结果是：" + map.get(keyword);
    }
}

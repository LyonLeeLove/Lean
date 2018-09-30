package com.lyon.search;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:抽象查询类，充当抽象主题角色     <br>
 * Date 9/26/2018 8:59 PM                    <br>
 */
public interface ISearch {
    /**
     * 搜索方法接口
     * @param user 用户名
     * @param keyword 关键词
     * @return 结果
     */
    String search(String user, String keyword);
}

package com.lyon.search;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:日志记录                      <br>
 * Date 9/27/2018 2:43 PM                    <br>
 */
public class Logger {
    public void log(String user) {
        System.out.println(String.format("更新数据库，用户'%s'查询次数加1", user));
    }
}

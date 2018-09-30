package com.lyon.weather.normal;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:抽象主题类                    <br>
 * Date 9/27/2018 5:25 PM                    <br>
 */
public interface WeatherSubject {
    /**
     * 请求天气接口
     * @param request 请求
     * @return 结果
     */
    String invoke(String request);
}

package com.lyon.weather.normal;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/27/2018 5:27 PM                    <br>
 */
public class RealWeatherSubject implements WeatherSubject {
    /**
     * 请求天气接口
     *
     * @param request 请求
     * @return 结果
     */
    @Override
    public String invoke(String request) {
        System.out.println(String.format("正在查询%s天气...", request));
        return request + "天气晴朗";
    }
}

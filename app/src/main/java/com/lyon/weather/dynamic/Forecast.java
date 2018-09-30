package com.lyon.weather.dynamic;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/27/2018 5:50 PM                    <br>
 */
public interface Forecast {
    /**
     * 天气
     * @param request 请求
     * @return 天气
     */
    String getWeather(String request);

    /**
     * 紫外线
     * @param request 请求
     * @return 紫外线
     */
    String getUltravioletLight(String request);

    /**
     * 时间
     * @return 时间
     */
    String getCurrentTime();

}

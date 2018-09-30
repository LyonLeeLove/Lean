package com.lyon.weather.dynamic;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/27/2018 5:53 PM                    <br>
 */
public class RealForecast implements Forecast {
    /**
     * 天气
     *
     * @param request 请求
     * @return 天气
     */
    @Override
    public String getWeather(String request) {
        System.out.println(String.format("正在查询%s天气...", request));
        return request + "天气晴朗";
    }

    /**
     * 紫外线
     *
     * @param request 请求
     * @return 紫外线
     */
    @Override
    public String getUltravioletLight(String request) {
        System.out.println(String.format("正在查询%s紫外线状况...", request));
        return request + "紫外线强烈";
    }

    /**
     * 时间
     *
     * @return 时间
     */
    @Override
    public String getCurrentTime() {
        System.out.println("正在查询当前时间...");
        return timeStamp2Time(System.currentTimeMillis());
    }

    public static String timeStamp2Time(long timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        Long time = timeStamp;
        return format.format(time);
    }
}

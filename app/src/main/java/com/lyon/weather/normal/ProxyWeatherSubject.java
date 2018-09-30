package com.lyon.weather.normal;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/27/2018 5:30 PM                    <br>
 */
public class ProxyWeatherSubject implements WeatherSubject{
    private RealWeatherSubject realWeatherSubject = new RealWeatherSubject();

    /**
     * 请求天气接口
     *
     * @param request 请求
     * @return 结果
     */
    @Override
    public String invoke(String request) {
        String cityCode = getCode(request);
        return getWeather(realWeatherSubject.invoke(cityCode));
    }

    private String getCode(String request) {
        return request;
    }

    private String getWeather(String weatherCode) {
        return weatherCode;
    }
}

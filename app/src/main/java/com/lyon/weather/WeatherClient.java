package com.lyon.weather;

import com.lyon.weather.dynamic.DynamicHandler;
import com.lyon.weather.dynamic.Forecast;
import com.lyon.weather.dynamic.RealForecast;
import com.lyon.weather.normal.ProxyWeatherSubject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/27/2018 5:23 PM                    <br>
 */
public class WeatherClient {
    public static void main(String[] args) throws IOException {
//        staticProxy();
        dynamicProxy();
    }

    private static void staticProxy() throws IOException {
        ProxyWeatherSubject weatherSubject = new ProxyWeatherSubject();
        String city = inputCity();
        String result = weatherSubject.invoke(city);
        System.out.print("预报：" + result);
    }

    /**
     * 通过代理类的创建方法newProxyInstance，我们可以看到一个代理对象的创建需要三个步骤和三个核心参数，它们分别为：
     * 三个步骤为：
     * 1，通过类加载器和接口创建代理类，方法为getProxyClass0(loader,interfaces)
     * 2，通过反射获取代理类的构造，且构参为InvocationHandler类型。
     * 3，通过构造函数生成代理对象的实例。
     * 三个参数为:
     * 1，类加载器：ClassLoader 将代理类的字节码转换成class类的对象
     * 2，被代理类的接口列表：Class<?>[] interfaces，通过接口动态生成代理类字节码
     * 3，InvocationHandler接口：被代理类的直接调用者
     * 代理类实例创建的步骤最重要的是第一步代理类的创建，后面两步是最基本的反射。
     */
    private static void dynamicProxy() throws IOException {
        Forecast forecast = new RealForecast();
        InvocationHandler handler = new DynamicHandler(forecast);

        System.out.print("开始创建代理类...");
        Forecast proxyForcast = (Forecast) Proxy.newProxyInstance(forecast.getClass().getClassLoader()
                , forecast.getClass().getInterfaces()
                , handler);
        System.out.print("代理类实例创建成功...");

        String city = inputCity();
        System.out.print(city + "预报：" +
                proxyForcast.getWeather(city) + ", " +
                proxyForcast.getUltravioletLight(city) + ", " +
                proxyForcast.getCurrentTime());
    }

    private static String inputCity() throws IOException {
        System.out.print("查询城市：");
        return (new BufferedReader(new InputStreamReader(System.in))).readLine();
    }

}

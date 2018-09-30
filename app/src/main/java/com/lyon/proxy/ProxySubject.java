package com.lyon.proxy;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description: 代理类，继承抽象主题类，维持一个对真实主题对象的引用    <br>
 * Date 9/25/2018 5:28 PM                    <br>
 */
public class ProxySubject extends Subject {
    private RealSubject realSubject = new RealSubject();

    public void preRequest() {
        // ...
    }

    @Override
    public void Request() {
        preRequest();
        // 调用真实主题对象的方法
        realSubject.Request();
        postRequest();
    }

    public void postRequest() {
        // ...
    }
}

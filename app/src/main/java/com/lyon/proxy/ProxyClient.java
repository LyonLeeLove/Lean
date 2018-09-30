package com.lyon.proxy;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description: 客户类                       <br>
 * Date 9/25/2018 5:36 PM                    <br>
 */
public class ProxyClient {
    public static void main(String[] args) {
        ProxySubject proxy = new ProxySubject();
        proxy.Request();
    }
}

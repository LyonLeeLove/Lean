package com.lyon.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:客户端，对保护代理和智能引用代理的应用 <br>
 * Date 9/27/2018 3:58 PM                    <br>
 */
public class SearchClient {
    public static void main(String[] args) throws IOException {
        ProxySearch proxySearch = new ProxySearch();
        String result = proxySearch.search(getName(), getKey());
        System.out.print("查询结果：" + result);
    }

    private static String getName() throws IOException {
        System.out.print("登录名：");
        return (new BufferedReader(new InputStreamReader(System.in))).readLine();
    }

    public static String getKey() throws IOException {
        System.out.print("查询：");
        return (new BufferedReader(new InputStreamReader(System.in))).readLine();
    }
}

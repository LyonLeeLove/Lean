package com.lyon.search;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/25/2018 5:49 PM                    <br>
 */
public class AccessValidator {
    private List<String> users = new ArrayList<>();

    public AccessValidator() {
        users.add("Tom");
        users.add("Jack");
        users.add("LiLei");
        users.add("HanMeiMei");
    }

    public boolean validate(String user) {
        System.out.print(String.format("登陆中，验证用户'%s'是否合法...", user));
        if (users.contains(user)) {
            System.out.println(user + "登录成功...");
            return true;
        } else {
            System.out.println(user + "登录失败...");
            return false;
        }
    }
}

package com.lyon.calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:计算终端                      <br>
 * Date 9/5/2018 8:13 PM                    <br>
 */
public class CalculatorClient {
    public static void main(String[] args) throws IOException {
        String exp = getExp();

        HashMap<String, Integer> var = getValue(exp);
        Calculator calculator = new Calculator(exp);

        System.out.print("运算结果为：" + exp + " = " + calculator.run(var));

    }

    public static String getExp() throws IOException {
        System.out.print("请输入表达式：");
        return (new BufferedReader(new InputStreamReader(System.in))).readLine();
    }

    public static HashMap<String, Integer> getValue(String exp) throws IOException {
        HashMap<String, Integer> map = new HashMap<>();

        for (char ch:exp.toCharArray()) {
            if (ch != '+' && ch != '-') {
                if (!map.containsKey(String.valueOf(ch))) {
                    System.out.print("请输入" + ch + "的值：");
                    String in = (new BufferedReader(new InputStreamReader(System.in))).readLine();
                    map.put(String.valueOf(ch), Integer.valueOf(in));
                }
            }
        }

        return map;
    }
}

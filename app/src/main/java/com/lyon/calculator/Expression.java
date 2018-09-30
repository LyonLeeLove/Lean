package com.lyon.calculator;

import java.util.HashMap;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description: 抽象表达式：抽象的算术运算器，所有解释器共性的提取 <br>
 * Date 9/5/2018 7:48 PM                    <br>
 */
public abstract class Expression {
    /**
     * 抽象的解析方法
     * 具体的解析逻辑由具体的子类实现
     * @param var key是公式中的参数，value是具体的数字
     * @return 解析得到的具体的值
     */
    public abstract int interpreter(HashMap<String, Integer> var);
}

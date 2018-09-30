package com.lyon.calculator;

import java.util.HashMap;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description: 变量解释器，终结符表达式       <br>
 * Date 9/5/2018 7:48 PM                    <br>
 */
public class VarExpression extends Expression {
    private String key;

    public VarExpression(String key) {
        this.key = key;
    }

    @Override
    public int interpreter(HashMap<String, Integer> var) {
        return var.get(key);
    }
}

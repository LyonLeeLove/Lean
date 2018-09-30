package com.lyon.calculator;

import java.util.HashMap;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description: 加法解释器，非终结符表达式   <br>
 * Date 9/5/2018 7:54 PM                    <br>
 */
public class AddExpression extends SymbolExpression {
    public AddExpression(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * 左右两个表达式的结果相加
     * @param var key是公式中的参数，value是具体的数字
     * @return 结果
     */
    @Override
    public int interpreter(HashMap<String, Integer> var) {
        return left.interpreter(var) + right.interpreter(var);
    }
}

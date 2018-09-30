package com.lyon.calculator;

import java.util.HashMap;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:减法解释器，非终结符表达式      <br>
 * Date 9/5/2018 7:56 PM                    <br>
 */
public class SubExpression extends SymbolExpression {
    public SubExpression(Expression left, Expression right) {
        super(left, right);
    }

    /**
     * 左右两个表达式相减
     * @param var key是公式中的参数，value是具体的数字
     * @return 结果
     */
    @Override
    public int interpreter(HashMap<String, Integer> var) {
        return left.interpreter(var) - right.interpreter(var);
    }
}

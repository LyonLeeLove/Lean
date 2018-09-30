package com.lyon.calculator;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description: 操作符解析，非终结符表达式     <br>
 * Date 9/5/2018 7:51 PM                    <br>
 */
public abstract class SymbolExpression extends Expression {
    protected Expression left;
    protected Expression right;

    /**
     * 解析公式只关注自己左右两个表达式的结果
     * @param left 左值
     * @param right 右值
     */
    public SymbolExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }
}

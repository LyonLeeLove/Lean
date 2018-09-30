package com.lyon.calculator;

import java.util.HashMap;
import java.util.Stack;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:运算解析                      <br>
 * Date 9/5/2018 7:37 PM                    <br>
 */
public class Calculator {
    /**
     * 定义的表达式
     */
    private Expression expression;

    /**
     * 构造传参并解析
     * @param exp 表达式
     */
    public Calculator(String exp) {
        // 定义堆栈，保证运算的先后顺序
        Stack<Expression> stack = new Stack<Expression>();

        // 表达式拆分卫字符数组
        char[] chars = exp.toCharArray();

        Expression left = null;
        Expression right = null;
        for (int i=0; i<chars.length; i++) {
            switch (chars[i]){
                case '+':
                    left = stack.pop();
                    right = new VarExpression(String.valueOf(chars[++i]));
                    // 加法结果放入堆栈中
                    stack.push(new AddExpression(left, right));
                    break;
                case '-':
                    left = stack.pop();
                    right = new VarExpression(String.valueOf(chars[++i]));
                    // 减法结果放入堆栈中
                    stack.push(new SubExpression(left, right));
                    break;
                default:
                    // 公式中的变量
                    stack.push(new VarExpression(String.valueOf(chars[i])));
            }
        }

        // 将运算结果抛出去
        this.expression = stack.pop();
    }

    /**
     * 开始运算
     * @param var 输入值
     * @return 运算结果
     */
    public int run(HashMap<String, Integer> var) {
        return this.expression.interpreter(var);
    }
}

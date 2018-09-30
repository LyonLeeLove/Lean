package com.lyon.robot;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description: And解释：非终结符表达式        <br>
 * Date 9/5/2018 8:45 PM                    <br>
 */
public class AndNode extends AbstractNode {
    /**
     * And的左表达式
     */
    private AbstractNode left;

    /**
     * And的右表达式
     */
    private AbstractNode right;

    public AndNode(AbstractNode left, AbstractNode right) {
        this.left = left;
        this.right = right;
    }

    /**
     * And表达式解释操作
     * @return 操作结果
     */
    @Override
    public String interpreter() {
        return left.interpreter() + "再" + right.interpreter();
    }
}

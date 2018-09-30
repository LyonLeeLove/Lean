package com.lyon.robot;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:简单句子解释：非终结符表达式     <br>
 * Date 9/5/2018 8:45 PM                    <br>
 */
public class SentenceNode extends AbstractNode{
    private AbstractNode direction;
    private AbstractNode action;
    private AbstractNode distance;

    public SentenceNode(AbstractNode direction, AbstractNode action, AbstractNode distance) {
        this.direction = direction;
        this.action = action;
        this.distance = distance;
    }

    /**
     * 简单句子的解释操作
     * @return 操作结果
     */
    @Override
    public String interpreter() {
        return direction.interpreter() + action.interpreter() + distance.interpreter();
    }
}

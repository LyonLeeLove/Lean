package com.lyon.robot;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:方向解释：终结符表达式          <br>
 * Date 9/5/2018 8:47 PM                    <br>
 */
public class DirectionNode extends AbstractNode {
    private String direction;

    public DirectionNode(String direction) {
        this.direction = direction;
    }

    /**
     * 方向表达式的解释操作
     * @return 操作结果
     */
    @Override
    public String interpreter() {
        if (direction.equalsIgnoreCase("up")) {
            return "向上";
        }  else if (direction.equalsIgnoreCase("down")) {
            return "向下";
        }  else if (direction.equalsIgnoreCase("left")) {
            return "向左";
        }  else if (direction.equalsIgnoreCase("right")) {
            return "向右";
        }  else {
            return "无效指令";
        }
    }
}

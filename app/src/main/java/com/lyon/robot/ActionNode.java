package com.lyon.robot;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:动作解释：终结符表达式          <br>
 * Date 9/5/2018 8:47 PM                    <br>
 */
public class ActionNode extends AbstractNode {
    private String action;

    public ActionNode(String action) {
        this.action = action;
    }

    /**
     * 动作（移动方式）表达式的解释操作
     * @return 操作结果
     */
    @Override
    public String interpreter() {
        if (action.equalsIgnoreCase("move")) {
            return "移动";
        }  else if (action.equalsIgnoreCase("run")) {
            return "快速移动";
        }  else {
            return "无效指令";
        }
    }
}

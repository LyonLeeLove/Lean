package com.lyon.robot;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:距离解释：终结符表达式          <br>
 * Date 9/5/2018 8:47 PM                    <br>
 */
public class DistanceNode extends AbstractNode {
    private String distance;

    public DistanceNode(String distance) {
        this.distance = distance;
    }


    /**
     * 距离表达式的解释操作
     * @return 操作结果
     */
    @Override
    public String interpreter() {
        return this.distance;
    }
}

package com.lyon.tetris;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:命令的接收者，负责具体执行请求  <br>
 * Date 9/6/2018 3:34 PM                    <br>
 */
public class TetrisMachine {
    public void toLeft(){
        System.out.println("向左");
    }

    public void toRigth(){
        System.out.println("向右");
    }

    public void fastToBottom(){
        System.out.println("快速向下");
    }
    public void transform(){
        System.out.println("变换形状");
    }
}


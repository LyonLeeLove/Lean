package com.lyon.robot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description: 操作端                       <br>
 * Date 9/5/2018 8:51 PM                    <br>
 */
public class RobotClient {
    public static void main(String args[]) throws IOException {
        // up move 5 and down run 10 and left move 5
        // down run 10 and left move 20
        // left move 2 and down run 4 and right run 5 and up move 8
        System.out.print("请输入指令：");
        String instruction = (new BufferedReader(new InputStreamReader(System.in))).readLine();
        Robot robot = new Robot();
        robot.handle(instruction);
        String outString;
        outString = robot.output();
        System.out.print("控制结果：" + outString);
    }
}

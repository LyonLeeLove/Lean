package com.lyon.tetris;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/6/2018 3:40 PM                    <br>
 */
public class TerisClient {
    public static void main(String[] args){
        TetrisMachine tetrisMachine = new TetrisMachine();

        LeftCommand leftCommand = new LeftCommand(tetrisMachine);
        RightCommand rightCommand = new RightCommand(tetrisMachine);
        FallCommand fallCommand = new FallCommand(tetrisMachine);
        TransformCommand transformCommand = new TransformCommand(tetrisMachine);

        leftCommand.execute();
        leftCommand.execute();
        fallCommand.execute();
        transformCommand.execute();
        rightCommand.execute();

    }

}

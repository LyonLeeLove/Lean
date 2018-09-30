package com.lyon.tetris;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description: 向左命令                    <br>
 * Date 9/6/2018 3:37 PM                    <br>
 */
public class LeftCommand implements Command {
    private TetrisMachine tetrisMachine;

    public LeftCommand(TetrisMachine tetrisMachine) {
        this.tetrisMachine = tetrisMachine;
    }

    @Override
    public void execute() {
        tetrisMachine.toLeft();
    }
}

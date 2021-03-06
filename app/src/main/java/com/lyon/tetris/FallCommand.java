package com.lyon.tetris;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:快速向下命令                     <br>
 * Date 9/6/2018 3:38 PM                    <br>
 */
public class FallCommand implements Command {
    private TetrisMachine tetrisMachine;

    public FallCommand(TetrisMachine tetrisMachine) {
        this.tetrisMachine = tetrisMachine;
    }

    @Override
    public void execute() {
        tetrisMachine.fastToBottom();
    }
}


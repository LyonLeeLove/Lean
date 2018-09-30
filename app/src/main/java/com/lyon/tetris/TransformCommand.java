package com.lyon.tetris;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:变换形状命令                   <br>
 * Date 9/6/2018 3:39 PM                    <br>
 */
public class TransformCommand implements Command {
    private TetrisMachine tetrisMachine;
    public TransformCommand(TetrisMachine tetrisMachine) {
        this.tetrisMachine = tetrisMachine;
    }

    @Override
    public void execute() {
        tetrisMachine.transform();
    }
}


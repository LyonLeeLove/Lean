package com.lyon.command;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/5/2018 9:00 PM                    <br>
 */

/**
 * 具体命令角色类
 */
public class SinaCommand implements Command {

    private Receiver receiver;

    public SinaCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.sinaLogin();
    }
}

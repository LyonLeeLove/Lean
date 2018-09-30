package com.lyon.command;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/5/2018 9:01 PM                    <br>
 */

/**
 * 具体命令角色类
 */
public class WeChatCommand implements Command{

    private Receiver receiver;

    public WeChatCommand(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.weChatLogin();
    }
}

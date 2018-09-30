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
public class TencentCommamd implements Command {

    private Receiver receiver;

    public TencentCommamd(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void execute() {
        receiver.tencentLogin();
    }
}

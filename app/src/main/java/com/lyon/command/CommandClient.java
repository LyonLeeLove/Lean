package com.lyon.command;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/5/2018 8:57 PM                    <br>
 */
public class CommandClient {
    public static void main(String[] args) throws Exception{
        Receiver receiver = new Receiver();
        Command tencentcommand = new TencentCommamd(receiver);
        Command weChatcommand = new WeChatCommand(receiver);
        Command sinaCommand = new SinaCommand(receiver);

        Invoker invoker = Invoker.getInstance();
        invoker.addAction(tencentcommand);
        invoker.addAction(weChatcommand);
        invoker.addAction(sinaCommand);

        invoker.excuteAction();

        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }
}

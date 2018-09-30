package com.lyon.command;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/5/2018 8:58 PM                    <br>
 */
public class Invoker {
    public static final String TAG = Invoker.class.getSimpleName();

    private List<Command> list = new ArrayList<>();

    private Invoker() {
    }

    public static Invoker getInstance() {
        return InstanceHolder.invoker;
    }

    static class InstanceHolder{
        private static Invoker invoker = new Invoker();
    }

    /**
     * 执行方法
     * @param command
     */
    public synchronized void action(Command command) {
        command.execute();
    }

    /**
     * 添加角色执行者　
     * @param command　
     */
    public synchronized void addAction(Command command) {
        if (command == null) {
            throw new NullPointerException("command is null");
        }

        if (list.contains(command)) {
            throw new RuntimeException("command is contains");
        } else {
            list.add(command);
        }
    }

    /**
     * 移除角色执行者
     * @param command
     */
    public synchronized void remoceAction(Command command) {
        if (command == null) {
            throw new NullPointerException("command is null");
        }

        if (list.contains(command)) {
            list.remove(command);
        } else {
            throw new RuntimeException("command not have in list");
        }
    }

    /**
     * 执行所有命令
     */
    public synchronized void excuteAction() {
        if (list.size() > 0) {
            for (Command command : list) {
                command.execute();
            }
        } else {
            throw new RuntimeException("list is null");
        }
    }
}

package com.lyon.observer.built;

import java.util.Observable;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/13/2018 6:02 PM                    <br>
 */
public class TargetObservable extends Observable {

    //要观察的数据：消息发生改变时，所有添加的观察者都能收到通知
    private String message;

    public String getContent() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        //被观察者数据发生变化时，通过以下的代码通知所有的观察者
        this.setChanged();
        this.notifyObservers(message);
    }

}

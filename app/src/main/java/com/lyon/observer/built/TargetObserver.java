package com.lyon.observer.built;

import java.util.Observable;
import java.util.Observer;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/13/2018 6:02 PM                    <br>
 */
public class TargetObserver implements Observer {
    //定义观察者名称
    private String name;

    public String getObserverName() {
        return name;
    }

    public void setObserverName(String observerName) {
        this.name = observerName;
    }

    @Override
    public void update(Observable o, Object arg) {
        //更新收到的消息数据
        System.out.print(name + "收到了发生变化的数据内容是：" + ((TargetObservable)o).getContent());
    }

}

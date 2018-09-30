package com.lyon.observer.built;

import java.util.Observable;
import java.util.Observer;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/13/2018 6:03 PM                    <br>
 */
public class TargetObserver1 implements Observer {
    //定义观察者名称
    private String name1;

    public String getObserverName() {
        return name1;
    }

    public void setObserverName(String observerName) {
        this.name1 = observerName;
    }

    @Override
    public void update(Observable o1, Object arg) {
        //更新收到的消息数据
        System.out.print(name1 + "收到了发生变化的数据内容是：" + ((TargetObservable)o1).getContent());
    }
}

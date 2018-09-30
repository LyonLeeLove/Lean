package com.lyon.observer.built;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/13/2018 6:02 PM                    <br>
 */
public class TargetClient {
    public static void main(String[] args) {
        //创建一个具体的被观察者
        TargetObservable observable = new TargetObservable();
        //创建第一个观察者
        TargetObserver one = new TargetObserver();
        one.setObserverName("我是观察者");
        //创建第二个观察者
        TargetObserver1 two = new TargetObserver1();
        two.setObserverName("我是观察者B");
        //注册观察者
        observable.addObserver(one);
        observable.addObserver(two);
        //目标跟新数据
        observable.setMessage("***我要更新数据***");
    }
}

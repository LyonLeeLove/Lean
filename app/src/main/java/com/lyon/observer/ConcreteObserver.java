package com.lyon.observer;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/13/2018 6:01 PM                    <br>
 */
public class ConcreteObserver implements IObserver {
    @Override
    public void update(Object object) {
        System.out.print("已经更新完成");
    }
}

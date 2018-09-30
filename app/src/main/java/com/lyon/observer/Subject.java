package com.lyon.observer;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/13/2018 6:01 PM                    <br>
 */
public interface Subject {
    void registerObserver(IObserver observer);

    void removeObserver(IObserver observer);

    void notifyObserver(Object object);
}

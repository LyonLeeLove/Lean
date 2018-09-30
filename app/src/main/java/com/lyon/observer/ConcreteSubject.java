package com.lyon.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyon                             <br>
 * @version V0.0.1                          <br>
 * Description:                             <br>
 * Date 9/13/2018 6:01 PM                    <br>
 */
public class ConcreteSubject implements Subject{
    private List<IObserver> observerList = new ArrayList<IObserver>();

    @Override
    public void registerObserver(IObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("The observer is null");
        }

        synchronized (observerList) {
            if (observerList.contains(observer)) {
                throw new IllegalStateException("Observer " + observer + " is already registered");
            }
            observerList.add(observer);
        }

    }
    @Override
    public void removeObserver(IObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("The observer is null");
        }

        synchronized (observerList) {
            if (observerList.contains(observer)) {
                observerList.remove(observer);
            } else {
                throw new IllegalStateException("Observer " + observer + " was not registered");
            }
        }

    }

    @Override
    public void notifyObserver(Object object) {
        synchronized (observerList) {
            if (observerList.size() > 0) {
                for (IObserver observer : observerList) {
                    observer.update(object);
                }
            }
        }

    }
}

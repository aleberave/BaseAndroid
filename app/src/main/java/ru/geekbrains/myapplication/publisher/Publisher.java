package ru.geekbrains.myapplication.publisher;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.myapplication.repository.CardData;

public class Publisher {

    private List<Observer> observers;

    public Publisher() {
        observers = new ArrayList<>();
    }

    public void sendCardDate(CardData cardData) {
        for (Observer observer : observers) {
            observer.receiveCardData(cardData);
        }
    }

    /**
     * добавляет подписчика
     *
     * @param observer
     */
    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    /**
     * отписывает подписчика
     *
     * @param observer
     */
    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }
}


package ru.geekbrains.myapplication.publisher;

import ru.geekbrains.myapplication.repository.CardData;

public interface Observer {

    void receiveCardData(CardData cardData);
}

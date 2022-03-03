package ru.geekbrains.myapplication.repository;

import java.util.List;

public interface CardsSource {
    int size();

    List<CardData> getAllCardDAta();

    CardData getCardDAta(int position);

    void clearCardsData();

    void addCardData(CardData cardData);

    void deleteCardDAta(int position);

    void updateCardDAta(int position, CardData newCardData);
}

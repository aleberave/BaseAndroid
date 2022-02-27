package ru.geekbrains.myapplication.repository;

import java.util.List;

public interface CardSource {
    int size();

    List<CardData> getAllCardDAta();

    CardData getCardDAta(int position);
}

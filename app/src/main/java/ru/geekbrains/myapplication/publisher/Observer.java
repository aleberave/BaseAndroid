package ru.geekbrains.myapplication.publisher;

import ru.geekbrains.myapplication.repository.NoteData;

public interface Observer {

    void receiveCardData(NoteData noteData);
}

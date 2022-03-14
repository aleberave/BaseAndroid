package ru.geekbrains.myapplication.repository;


import javax.sql.DataSource;

public interface RemoteFirestoreResponse {
// Будет сообщать о том что Firestore инициализировался и получил коллекцию

    void initialized(NotesSource notesSource);
}


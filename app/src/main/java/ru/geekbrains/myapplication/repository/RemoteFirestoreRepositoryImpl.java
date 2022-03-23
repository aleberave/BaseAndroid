package ru.geekbrains.myapplication.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RemoteFirestoreRepositoryImpl implements NotesSource {

    private static final String NOTES_COLLECTION = "notes";
    private final List<NoteData> dataSource;

    // создаем и инициализируем удаленную БД
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    // создаем ссылку на коллекцию документов внутри удаленной БД
    // уровень вложенности документа в документ более 100
    private final CollectionReference collectionReference = firebaseFirestore.collection(NOTES_COLLECTION);

    public RemoteFirestoreRepositoryImpl() {
        dataSource = new ArrayList<>();
    }

    public RemoteFirestoreRepositoryImpl init(RemoteFirestoreResponse remoteFirestoreResponse) {
        // запрашиваем все записи в коллекции,
        // задаем по полю дата (Date) будем сортировать
        // ASCENDING - возвращает: в алфавитном порядке/по возрастанию
        // DESCENDING - возвращает: в обратном алфавитному порядку/по уменьшению
        // Т.к. запрос на получение данных делается в интернет и всё происходит
        // не моментально, то нужен listener на асинхронный запрос, т.е. dataSource
        // вызовется не во время загрузки приложения, а через какое-то время пока
        // сделается запрос на сервер -> вернется ответ -> передастся ответ из этого
        // Callback'а в Callback во фрагменте с помощью/через RemoteFirestoreResponse
        collectionReference.orderBy(NoteDataMapping.Fields.DATE,
                Query.Direction.ASCENDING).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        // если получен положительный ответ
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) { // task-лист заметок
                                Map<String, Object> document = queryDocumentSnapshot.getData(); // наша заметка-карточка
                                String id = queryDocumentSnapshot.getId(); // id нашей заметки-карточки
                                dataSource.add(NoteDataMapping.toNoteData(id, document));
                            }
                        }
                        remoteFirestoreResponse.initialized(RemoteFirestoreRepositoryImpl.this);
                    }
                });
        return this;
    }

    @Override
    public int size() {
        return dataSource.size();
    }

    @Override
    public List<NoteData> getAllNoteData() {
        return dataSource;
    }

    @Override
    public NoteData getNoteData(int position) {
        return dataSource.get(position);
    }

    @Override
    public void clearNotesData() {
        for (NoteData noteData : dataSource) {
            collectionReference.document(noteData.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
        }

        dataSource.clear();
    }

    @Override
    public void addNoteData(NoteData noteData) {
        // добавление в удаленную БД Firestore обязательно,
        // а данный блок CallBack'а не обязательный ID обновляется автоматически,
        // тут для примера изменение ID
        collectionReference.add(NoteDataMapping.toDocument(noteData))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        noteData.setId(documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
        dataSource.add(noteData);
//        }
    }

    @Override
    public void deleteNoteData(int position) {
        collectionReference.document(dataSource.get(position).getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

        dataSource.remove(position);
    }

    @Override
    public void updateNoteData(int position, NoteData newNoteData) {
        collectionReference.document(dataSource.get(position).getId()).update(NoteDataMapping.toDocument(newNoteData)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

        dataSource.set(position, newNoteData);
    }
}


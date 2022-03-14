package ru.geekbrains.myapplication.repository;

import com.google.firebase.Timestamp;

import java.util.HashMap;
import java.util.Map;


public class NoteDataMapping {

    /**
     * Поля для хранения в ячейках внутри БД Firestore
     * (например для сортировки)
     */
    public static class Fields {
        public final static String TITLE = "title";
        public final static String DESCRIPTION = "description";
        public final static String PICTURE = "picture";
        public final static String PICTURE_COLOR = "pictureColor";
        public final static String LIKE = "like";
        public final static String DATE = "date";
    }

    /**
     * Приводим полученный документ из БД Firestore
     * из Map<String, Object> к NoteData
     *
     * @param id
     * @param doc
     * @return answer
     */
    public static NoteData toNoteData(String id, Map<String, Object> doc) {
        long indexPic = (long) doc.get(Fields.PICTURE);
        long indexClr = (long) doc.get(Fields.PICTURE_COLOR);
        Timestamp timestamp = (Timestamp) doc.get(Fields.DATE);
        NoteData answer = new NoteData((String) doc.get(Fields.TITLE),
                (String) doc.get(Fields.DESCRIPTION),
                PictureOrColorIndexConverter.getPictureByIndex((int) indexPic),
                PictureOrColorIndexConverter.getColorByIndex((int) indexClr),
                (boolean) doc.get(Fields.LIKE), timestamp.toDate());
        answer.setId(id);
        return answer;
    }

    /**
     * Приводим NoteData к Map<String, Object>
     *
     * @param noteData
     * @return answer
     */
    public static Map<String, Object> toDocument(NoteData noteData) {
        Map<String, Object> answer = new HashMap<>();
        answer.put(Fields.TITLE, noteData.getTitle());
        answer.put(Fields.DESCRIPTION, noteData.getDescription());
        answer.put(Fields.PICTURE, PictureOrColorIndexConverter.getIndexByPicture(noteData.getPicture()));
        answer.put(Fields.PICTURE_COLOR, PictureOrColorIndexConverter.getIndexByColor(noteData.getPictureColor()));
        answer.put(Fields.LIKE, noteData.isLike());
        answer.put(Fields.DATE, noteData.getDate());
        return answer;
    }

}


package ru.geekbrains.myapplication;

//Создайте класс данных со структурой заметок: название заметки,
// описание заметки, дата создания и т. п.

public class MyNotes {

    private String nameNote;
    private String descriptionNote;
    private String dateNote;

    public String getNameNote() {
        return nameNote;
    }

    public void setNameNote(String nameNote) {
        this.nameNote = nameNote;
    }

    public String getDescriptionNote() {
        return descriptionNote;
    }

    public void setDescriptionNote(String descriptionNote) {
        this.descriptionNote = descriptionNote;
    }

    public String getDateNote() {
        return dateNote;
    }

    public void setDateNote(String dateNote) {
        this.dateNote = dateNote;
    }

    public MyNotes(String nameNote, String descriptionNote, String dateNote) {
        this.nameNote = nameNote;
        this.descriptionNote = descriptionNote;
        this.dateNote = dateNote;
    }
}


package com.example.googleintegration;

public class Note {
    private String DocumentId;
    private String noteTitle;
    private String noteText;
    private String dateModified;

    public Note(){

    }

    public Note(String DocumentId, String noteTitle, String noteText, String dateModified){
        this.DocumentId = DocumentId;
        this.noteTitle = noteTitle;
        this.noteText = noteText;
        this.dateModified = dateModified;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNoteText() {
        return noteText;
    }

    public String getDateModified() {
        return dateModified;
    }

    public String getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }
}



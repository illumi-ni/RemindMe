package com.example.googleintegration;

public class Note {
    private String DocumentId;
    private String noteTitle;
    private String noteText;

    public Note(){

    }

    public Note(String DocumentId, String noteTitle, String noteText){
        this.DocumentId = DocumentId;
        this.noteTitle = noteTitle;
        this.noteText = noteText;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNoteText() {
        return noteText;
    }

    public String getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }
}



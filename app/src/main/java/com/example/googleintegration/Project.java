package com.example.googleintegration;

public class Project {
    private String DocumentId;
    private String projectTitle;
    private String dateCreated;

    public Project(){

    }

    public Project(String DocumentId, String projectTitle, String dateCreated){
        this.DocumentId = DocumentId;
        this.projectTitle = projectTitle;
        this.dateCreated = dateCreated;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }
}

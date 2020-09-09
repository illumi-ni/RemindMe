package com.example.googleintegration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CreateUpdateNote extends AppCompatActivity {
    private EditText txtTitle, txtNote;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Create a note");

        txtTitle = findViewById(R.id.noteTitle);
        txtNote = findViewById(R.id.noteText);
        Button btnSaveNote = findViewById(R.id.btnSaveNote);

        Intent in = getIntent();

        final String docID = in.getStringExtra("id");
        final String title = in.getStringExtra("title");
        final String note = in.getStringExtra("note");

        if (in != null) {
            txtTitle.setText(title);
            txtNote.setText(note);
        }

        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteTitle = txtTitle.getText().toString();
                String noteText = txtNote.getText().toString();

                if(docID == null){
                    saveNote(noteTitle, noteText);
                }
                else{
                    updateNote(docID, noteTitle, noteText);
                }
            }
        });
    }

    public void saveNote(String noteTitle, String noteText){
        if (noteTitle.length() == 0 || noteText.length() == 0) {
            Toast.makeText(getApplicationContext(), "Cannot create empty note", Toast.LENGTH_SHORT).show();
        }
        else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DocumentReference docRef = db.collection("note").document();
            String noteId = docRef.getId();

            String dateModified = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            assert user != null;
            userId = user.getUid();

            Map<String, Object> note = new HashMap<>();
            note.put("Id", noteId);
            note.put("userId", userId);
            note.put("noteTitle", noteTitle);
            note.put("noteText", noteText);
            note.put("dateModified", dateModified);

            db.collection("note").add(note).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed to save", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public void updateNote(String noteID, String noteTitle, String noteText){
        if(noteTitle.length() == 0 || noteText.length() == 0){
            Toast.makeText(getApplicationContext(), "Cannot create empty note", Toast.LENGTH_SHORT).show();
        }
        else {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DocumentReference docRef = db.collection("note").document(noteID);

            assert user != null;
            userId = user.getUid();

            String dateModified = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            Map<String, Object> note = new HashMap<>();
            note.put("userId", userId);
            note.put("noteTitle", noteTitle);
            note.put("noteText", noteText);
            note.put("dateModified", dateModified);

            docRef.update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed to update", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
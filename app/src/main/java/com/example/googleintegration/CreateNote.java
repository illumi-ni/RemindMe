package com.example.googleintegration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateNote extends AppCompatActivity {
    EditText txtTitle, txtNote;
    Button btnSaveNote;
    String userId, noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setTitle("Create a note");

        txtTitle = findViewById(R.id.noteTitle);
        txtNote = findViewById(R.id.noteText);

        btnSaveNote = findViewById(R.id.btnSaveNote);
        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteTitle = txtTitle.getText().toString();
                String noteText = txtNote.getText().toString();

                if(noteTitle.length() == 0 || noteText.length() == 0){
                    Toast.makeText(getApplicationContext(), "Cannot create empty note", Toast.LENGTH_SHORT).show();
                }
                else{
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DocumentReference docRef = db.collection("note").document();
                    noteId = docRef.getId();

                    assert user != null;
                    userId = user.getUid();

                    Map<String, Object> note = new HashMap<>();
                    note.put("Id", noteId);
                    note.put("userId", userId);
                    note.put("noteTitle", noteTitle);
                    note.put("noteText", noteText);

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
        });
    }
}
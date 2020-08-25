package com.example.googleintegration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.googleintegration.models.ProjectNote;
import com.example.googleintegration.models.ProjectTaskNote;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.Objects;

public class Project extends AppCompatActivity implements ProjectActivity {
    private TextView prtname;
    private TextView prtdesc;
    private Button btnprt;
    private RecyclerView mProjectlist;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    private CollectionReference projectRef;
    private ImageView imgdel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        setTitle("Project");
        imgdel = (ImageView)findViewById(R.id.deleteimg);
        mProjectlist = findViewById(R.id.projectlist);

        firebaseFirestore = FirebaseFirestore.getInstance();
        projectRef = firebaseFirestore.collection("projects");

        Query query = projectRef.whereEqualTo("user_id", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .orderBy("project_id", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ProjectList> options = new FirestoreRecyclerOptions.Builder<ProjectList>()
                .setQuery(query, ProjectList.class).build();
        adapter = new FirestoreRecyclerAdapter<ProjectList, ProjectViewHolder>(options) {
            @NonNull
            @Override
            public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_project_list, parent, false);
                return new ProjectViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProjectViewHolder holder, int position, @NonNull ProjectList model) {

                holder.listprojectname.setText(model.getProjectname());
                holder.listprojectdesc.setText(model.getProjectdesc());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ProjectTask.class);
                        startActivity(intent);
                    }
                });

            }
        };

        mProjectlist.setHasFixedSize(true);
        mProjectlist.setLayoutManager(new LinearLayoutManager(this));
        mProjectlist.setAdapter(adapter);

        prtname = (TextView) findViewById(R.id.txtprjtname);
        prtdesc = (TextView) findViewById(R.id.txtprjtdesc);


        //opens create project dialog box
        Button btnpr = findViewById(R.id.btnproject);
        btnpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openDialog();
            }
        });
    }
    public void openDialog(){
        CreateProject createProject = new CreateProject();
        createProject.show(getSupportFragmentManager(), "create project");
    }
    //projects collection added in firestore,
    //when project is created from dialog box project data is stored in firestore
    @Override
    public void createproject(String projectname, String projectdesc) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference newProjectRef = db.collection("projects").document();
//        DocumentReference newProjectRef = db.collection("projects").document()
//                .collection("ProjectTasks").document();

        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ProjectNote projectNote = new ProjectNote();
        projectNote.setProjectname(projectname);
        projectNote.setProjectdesc(projectdesc);
        projectNote.setProject_id(newProjectRef.getId());
        projectNote.setUser_id(user_id);

        newProjectRef.set(projectNote).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Project.this, "Created new project", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Project.this, "Failed to create project", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class ProjectViewHolder extends RecyclerView.ViewHolder {
        private TextView listprojectname;
        private TextView listprojectdesc;
        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            listprojectname = itemView.findViewById(R.id.txtprojectname);
            listprojectdesc = itemView.findViewById(R.id.txtprojectdesc);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
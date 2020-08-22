package com.example.googleintegration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateProject extends AppCompatDialogFragment {
    private static final String TAG = "CreateProject";

    private EditText txtprojectname;
    private EditText txtxprojectdesc;
    private ProjectActivity mprojectActivity;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_create_project, null);


        builder.setView(view)
                .setTitle("Create Project")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String projectname = txtprojectname.getText().toString();
                        String projectdesc = txtxprojectdesc.getText().toString();
                        if (!projectname.equals("")){
                            mprojectActivity.createproject(projectname, projectdesc);
                            getDialog().dismiss();
                        }
                        else {
                            Toast.makeText(getActivity(), "Enter project name", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        txtprojectname = view.findViewById(R.id.txtprjtname);
        txtxprojectdesc = view.findViewById(R.id.txtprjtdesc);
        return  builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mprojectActivity = (ProjectActivity) getActivity();
    }
}
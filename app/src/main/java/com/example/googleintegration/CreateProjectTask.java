package com.example.googleintegration;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CreateProjectTask extends AppCompatDialogFragment {
    private static final String TAG = "CreateProjectTask";

    private EditText editprtaskdesc;
    private EditText editprtaskdesc2;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_create_project_task, null);
        builder.setView(view).setTitle("Add Task")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String TaskDesc = editprtaskdesc.getText().toString();
                        String TaskDesc2 = editprtaskdesc2.getText().toString();
                        if (!TaskDesc.equals("")){

                        }
                        else {
                            Toast.makeText(getActivity(), "Enter Task description", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        editprtaskdesc = view.findViewById(R.id.txtprjtskdesc);
        editprtaskdesc2 = view.findViewById(R.id.txtprjtskdesc2);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }
}

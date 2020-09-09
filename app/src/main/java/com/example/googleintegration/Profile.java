package com.example.googleintegration;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.*;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

public class Profile extends AppCompatActivity{
    private GoogleSignInClient mGoogleSignInClient;
    Button btnedit;
    private TextView lblname, lbldob, lblemail;
    private ImageView imgprofile;
    private EditText txtname, txtdob, txtemail;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Your Profile");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
//            String personDob = acct.get;
//            String personId = acct.getId();
            //Url personPhoto = acct.getPhotoUrl();

            Toast.makeText(Profile.this, "Welcome "+ personName, Toast.LENGTH_SHORT).show();

            txtname = (EditText)findViewById(R.id.txtname);
            txtname.setText(personName);

            txtemail = (EditText)findViewById(R.id.txtemail);
            txtemail.setText(personEmail);

            imgprofile = findViewById(R.id.imgprofile);
            Picasso.get().load(acct.getPhotoUrl()).placeholder(R.drawable.ic_user).into(imgprofile);

        }
    }
}
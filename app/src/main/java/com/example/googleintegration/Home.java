package com.example.googleintegration;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
//import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private GoogleSignInClient mGoogleSignInClient;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private TextView txtView;
    private ImageView img;
    private CalendarView calenderView;
    private NavigationView nav;
    AlertDialog.Builder builder;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        mDrawerLayout = findViewById(R.id.draw);
//        calenderView=findViewById(R.id.calendar);
//        nav = findViewById(R.id.nav);
//        Load_settings();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.draw);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
//            String personGivenName = acct.getGivenName();
//            String personFamilyName = acct.getFamilyName();
//            String personEmail = acct.getEmail();
//            String personId = acct.getId();
//            Uri personPhoto = acct.getPhotoUrl();

            Toast.makeText(Home.this, "Welcome "+ personName, Toast.LENGTH_SHORT).show();
            nav = findViewById(R.id.nav);
            View header = nav.getHeaderView(0);

            img = header.findViewById(R.id.headerProfImg);
//            Picasso.get().load(acct.getPhotoUrl()).placeholder(R.drawable.userimg).into(img);

            txtView = header.findViewById(R.id.name);
            txtView.setText(personName);
        }

        calenderView= findViewById(R.id.calendar);
        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String date= i + "/" + i1 + "/" + i2 + "/";
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddNew.class);
                startActivity(i);
            }
        });
        setNavigationViewListener();
    }
//    public void Load_settings(){
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//        boolean chk_night = sp.getBoolean("NIGHT", false);
//        if (chk_night) {
//            mDrawerLayout.setBackgroundColor(Color.parseColor("555555"));
//        } else {
//            mDrawerLayout.setBackgroundColor(Color.parseColor("ffffff"));
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Home.this, "Logged Out!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Home.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_profile:
                Intent i = new Intent(this, Profile.class);
                this.startActivity(i);
                break;

            case R.id.menu_today:
                //code
                break;

            case R.id.menu_upcoming:
                //code
                break;

            case R.id.menu_achievements:
                //code
                break;

            case R.id.menu_settings:
//                Intent se = new Intent(this, Settings.class);
//                this.startActivity(se);
                startActivity(new Intent(this,Settings.class));

                break;

            case R.id.menu_logout:
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                signOut();
                                Toast.makeText(getApplicationContext(), "Logged out!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("RemindMe");
                alert.show();
        }
        return super.onOptionsItemSelected(item);
        //Hello
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(this);
    }

//    @Override
//    protected void onResume() {
//        Load_settings();
//        super.onResume();
//    }
}
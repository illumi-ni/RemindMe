package com.example.googleintegration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class ProjectTaskMember extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_task_member);

        Intent intent = getIntent();
        String projectId = intent.getStringExtra("projectId");
        String projectTitle = intent.getStringExtra("projectTitle");
        String dateCreated = intent.getStringExtra("dateCreated");
        setTitle(projectTitle);

        TabLayout tabLayout = findViewById(R.id.tabLayoutProject);
        final ViewPager viewPager = findViewById(R.id.viewPagerProject);

        tabLayout.addTab(tabLayout.newTab().setText("Task"));
        tabLayout.addTab(tabLayout.newTab().setText("Members"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final TabAdapterProject adapter = new TabAdapterProject(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
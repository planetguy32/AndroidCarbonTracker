package org.conifr.carbontracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ProfilePage extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Button aboutBtn = (Button) findViewById(R.id.aboutBtn);

        // launch the about button within the app
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startAbout = new Intent(getApplicationContext(), ProfileAbout.class);

                startActivity(startAbout);
            }
        });


        Button clearBtn = (Button) findViewById(R.id.clearBtn);

        // launch the clear button within the app
        // clears input fields for name, email, and college
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
                nameEditText.setText("");

                EditText emailEditText = (EditText) findViewById(R.id.emailEditText);
                emailEditText.setText("");

                EditText collegeEditText = (EditText) findViewById(R.id.collegeEditText);
                collegeEditText.setText("");
            }
        });


        Button contactBtn = (Button) findViewById(R.id.contactBtn);

        // launch the contact button within the app
        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startContact = new Intent(getApplicationContext(), ProfileContact.class);

                startActivity(startContact);
            }
        });

        Button legalBtn = (Button) findViewById(R.id.legalBtn);

        // launch the legal button within the app
        legalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startLegal = new Intent(getApplicationContext(), ProfileLegal.class);

                startActivity(startLegal);
            }
        });
    }

}

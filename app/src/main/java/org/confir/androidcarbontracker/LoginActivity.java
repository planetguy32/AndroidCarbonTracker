package org.confir.androidcarbontracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText user_Name;
    private EditText password;
    private Button login;
    private Button create_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user_Name = (EditText)findViewById(R.id.username_edit_text);
        password = (EditText)findViewById(R.id.password_edit_text);
        login = (Button)findViewById(R.id.login_button);
        create_account = (Button)findViewById(R.id.create_account_button);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(( user_Name.getText().toString().length() != 0) && ( password.getText().toString().length() != 0) ){
                    // Firebase Validation
                    launchMain();
                }
                else{
                    Toast.makeText(getBaseContext(), "Please enter a valid username and password." , Toast.LENGTH_LONG).show();
                }
            }
        });
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(( user_Name.getText().toString().length() != 0) && ( password.getText().toString().length() != 0) ){
                    // Firebase Account Creation
                    launchMain();
                }
                else{
                    Toast.makeText(getBaseContext(), "Please enter a valid username and password." , Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void launchMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}

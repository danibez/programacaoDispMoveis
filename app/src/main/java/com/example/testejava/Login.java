package com.example.testejava;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInBtn();
    }

    public void signInBtn() {
        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        TextView usuario = (TextView) findViewById(R.id.emailField);
        TextView senha = (TextView) findViewById(R.id.senhaField);

        Intent intent = new Intent(this, CameraLoadPicture.class);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);

            }
        });
    }


}
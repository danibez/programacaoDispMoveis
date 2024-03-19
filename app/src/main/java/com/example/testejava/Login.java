package com.example.testejava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInBtn();
    }

    private void signInBtn() {
        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        TextView usuario = (TextView) findViewById(R.id.emailField);
        TextView senha = (TextView) findViewById(R.id.senhaField);

        Intent intent = new Intent(this, TelaVideo.class);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);

            }
        });
    }


}
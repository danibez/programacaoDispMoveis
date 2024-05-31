package com.example.testejava;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MinhaClasse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_classe);

        TextView textView = findViewById(R.id.hello);
        String message = getWelcomeMessage("World");
        textView.setText(message);

        clicarBotao();

    }

    public void clicarBotao() {
        Button b = (Button) findViewById(R.id.bHello);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLIQUE","CLICOU!!!");
            }
        });
    }

    public String getWelcomeMessage(String name) {
        return "Welcome, " + name + "!";
    }
}
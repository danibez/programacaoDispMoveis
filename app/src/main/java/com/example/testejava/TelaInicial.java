package com.example.testejava;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.net.Uri;

public class TelaInicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        incrementaValor();
    }

    private void incrementaValor() {
        Button button = (Button) findViewById(R.id.incrementa);
        Button tele = (Button) findViewById(R.id.tele);
        TextView texto = (TextView) findViewById(R.id.texto);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = texto.getText().toString();
                int newVal = Integer.parseInt(temp);
                texto.setText(String.valueOf(++newVal));

            }
        });

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, "ACORDA!!!")
                .putExtra(AlarmClock.EXTRA_HOUR, 20)
                .putExtra(AlarmClock.EXTRA_MINUTES, 0);

        tele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }
}
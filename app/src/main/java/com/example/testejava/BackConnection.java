package com.example.testejava;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class BackConnection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_connection);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());

        buttonAction();
    }

    private void buttonAction() {

        Button button = (Button) findViewById(R.id.bConn);
        Button buttonGet = (Button) findViewById(R.id.buttonGet);
        EditText msg = (EditText) findViewById(R.id.mensagem);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject firstObject = new ParseObject("FirstClass");
                firstObject.put("msg",msg.getText().toString());
                firstObject.saveInBackground(e -> {
                    if (e != null){
                        Log.e("BackConn", e.getLocalizedMessage());

                    }else{
                        Log.d("BackConn","Object saved.");
                    }
                });
            }
        });

        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("FirstClass");
                query.orderByDescending("createdAt");
                query.setLimit(1);
                query.findInBackground((objects, e) -> {
                    if (e == null) {
                        ParseObject obj = objects.get(0);
                        msg.setText(obj.get("msg").toString());
                    } else {

                    }
                });
            }
        });

    }


}
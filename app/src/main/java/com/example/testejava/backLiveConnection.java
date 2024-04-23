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
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

public class backLiveConnection extends AppCompatActivity {

    ParseLiveQueryClient parseLiveQueryClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_live_connection);

        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId(getString(R.string.back4app_app_id))
                        .clientKey(getString(R.string.back4app_client_key))
                        .server(getString(R.string.back4app_server_url))
                        .build());



        parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();

        SubscriptionHandling subscriptionHandling = parseLiveQueryClient.subscribe(new ParseQuery<>("liveClass"));
        subscriptionHandling.handleSubscribe(q -> {
            subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, (query, object) -> {
                backLiveConnection.this.runOnUiThread(() -> {
                    EditText msg = (EditText) findViewById(R.id.mensagem);
                    msg.setText(object.get("msg").toString());
                });
            });
        });

        buttonAction();
    }

    private void buttonAction() {

        Button button = (Button) findViewById(R.id.bConn);
        EditText msg = (EditText) findViewById(R.id.mensagem);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject firstObject = new ParseObject("liveClass");
                firstObject.put("msg", msg.getText().toString());
                firstObject.saveInBackground(e -> {
                    if (e != null){
                        Log.e("MainActivity", e.getLocalizedMessage());

                    }else{
                        Log.d("MainActivity","Object saved.");
                    }
                });
            }
        });



    }


}
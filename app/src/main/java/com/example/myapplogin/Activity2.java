package com.example.myapplogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplogin.ui.login.LoginActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Activity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);

        Button btn_shorten = (Button) findViewById(R.id.btn_shorten);
        btn_shorten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAPI();
                Toast.makeText(getApplicationContext(), "API LLAMADA", Toast.LENGTH_SHORT).show();
            }
        });

        //Get data from intent
        Intent intent = getIntent();
        String EXTRA_personEmail = intent.getStringExtra("EXTRA_personEmail");
        String EXTRA_personFamilyName = intent.getStringExtra("EXTRA_personFamilyName");
        String EXTRA_personGivenName = intent.getStringExtra("EXTRA_personGivenName");
        String EXTRA_personName = intent.getStringExtra("EXTRA_personName");

        //get TextViews
        TextView tv_personEmail = (TextView) findViewById(R.id.EXTRA_personEmail);
        //TextView tv_personFamilyName = (TextView) findViewById(R.id.EXTRA_personFamilyName);
        //TextView tv_personGivenName = (TextView) findViewById(R.id.EXTRA_personGivenName);
        TextView tv_personName = (TextView) findViewById(R.id.EXTRA_personName);

        //Assign texts
        tv_personEmail.setText(EXTRA_personEmail);
        //tv_personFamilyName.setText(EXTRA_personFamilyName);
        //tv_personGivenName.setText(EXTRA_personGivenName);
        tv_personName.setText(EXTRA_personName);

        Toast.makeText(getApplicationContext(), "BIENVENIDO", Toast.LENGTH_LONG).show();

    }

    private void callAPI() {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

        EditText urlE = (EditText) findViewById(R.id.url);
        String urlR = urlE.getText().toString();
        RequestBody body = RequestBody.create("url="+urlR, mediaType);

        Request request = new Request.Builder()
                .url("https://url-shortener-service.p.rapidapi.com/shorten")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("X-RapidAPI-Host", "url-shortener-service.p.rapidapi.com")
                .addHeader("X-RapidAPI-Key", "de55640607mshbeaf3b0a051d0d3p11fc5ajsn33631b8b4e37")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "CALL ERROR", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String myResponse = response.body().string();

                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(myResponse);
                        String r_url= jsonObject.optString("result_url");
                        String error_api= jsonObject.optString("error");

                        if(r_url != ""){
                            EditText url_short = (EditText) findViewById(R.id.url_short);
                            url_short.setText(r_url);
                        }else{
                            EditText url_short = (EditText) findViewById(R.id.url_short);
                            url_short.setText(error_api);
                            Toast.makeText(getApplicationContext(), "URL Inválida", Toast.LENGTH_LONG).show();
                        }
                    }catch (JSONException err){
                        Log.d("Error", err.toString());
                        Toast.makeText(getApplicationContext(), "PARSE JSON ERROR", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }


}
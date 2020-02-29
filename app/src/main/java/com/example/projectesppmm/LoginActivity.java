package com.example.projectesppmm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {
    private EditText usuariET;
    private EditText contrasenyaET;
    private String usuari;
    private String contrasenya;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLoginButtonClicked(View v) {
        inicialitzaET();
        new RequestAsync().execute();
    }

    private void inicialitzaET() {
        usuariET = findViewById(R.id.usuariEditText);
        contrasenyaET = findViewById(R.id.contrasenyaEditText);
        usuari = usuariET.getText().toString();
        contrasenya = contrasenyaET.getText().toString();
    }

    private void anarMainActivity() {
        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
        //myIntent.putExtra("key", value);
        LoginActivity.this.startActivity(myIntent);
    }

    public class RequestAsync extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                // POST Request
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("email", usuari);
                postDataParams.put("password", contrasenya);

                return RequestHandler.sendPost("http://52.44.95.114/quepassaeh/server/public/login/",postDataParams);
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null){
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}

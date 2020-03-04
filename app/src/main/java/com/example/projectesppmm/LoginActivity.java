package com.example.projectesppmm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        comprovaLogin();
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
                String nom;
                Boolean resultat;
                try {
                    JSONObject convertedObject = new JSONObject(s);
//                    System.out.println(s);
                    resultat = convertedObject.getBoolean("correcta");
                    //Si el login es correcta acaba l'activitat, si no fa un toast dient que les dades son incorrectes
                    if(resultat) {
                        guardarDades();
                        nom = convertedObject.getJSONObject("dades").getString("nom");
                        Toast.makeText(getApplicationContext(), "Hola " + nom, Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Dades incorrectes", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void guardarDades(){
        //Cream la instància del Shared preferences amb un nom i de tipus privat per a que només hi pugui accedir la nostra app.
        SharedPreferences spref = getSharedPreferences("DadesGuardades", MODE_PRIVATE);
        //També crearem el nostre editor per a poder "escriure" a les nostres shared preferences i guardar nova informació
        SharedPreferences.Editor editor = spref.edit();
        Gson gson = new Gson();
        //Després de crear el gson, amb una String json hi guardam la nostra array d'objectes que serà la que anirà a les SharedPreferences
        String usuariJson = gson.toJson(usuari);
        String contrasenyaJson = gson.toJson(contrasenya);
        //Ficam l'String a les SharedPreferences amb un referència a través de l'editor.
        editor.putString("email", usuariJson);
        editor.putString("password", contrasenyaJson);
        //Finalment confirmam els canvis realitzats.
        editor.apply();
    }

    private void comprovaLogin(){
        SharedPreferences spref = getSharedPreferences("DadesGuardades", MODE_PRIVATE);
        Gson gson = new Gson();
        Type type = new TypeToken<String>(){}.getType();
        String jsonE = spref.getString("email", null);
        jsonE = gson.fromJson(jsonE, type);
        System.out.println(jsonE);
        String jsonP = spref.getString("password", null);
        jsonP = gson.fromJson(jsonP, type);

        if (jsonE != null){
            usuari = jsonE;
            contrasenya = jsonP;
            new RequestAsync().execute();
        }
    }
}

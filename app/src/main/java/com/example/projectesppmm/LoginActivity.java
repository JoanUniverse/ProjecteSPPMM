package com.example.projectesppmm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.widget.Button;
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
    private LoginActivity.ReceptorXarxa receptor;
    private EditText usuariET;
    private EditText contrasenyaET;
    private String usuari;
    private String contrasenya;
    private String codiusuari;
    private Button botoLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receptor = new ReceptorXarxa();this.registerReceiver(receptor, filter);
        botoLogin = findViewById(R.id.loginButton);
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
//        usuariET.setText("");
//        contrasenyaET.setText("");
    }

    private void anarMainActivity() {
        Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
        myIntent.putExtra("key", codiusuari);
        LoginActivity.this.startActivity(myIntent);
    }

    public void comprovaConnectivitat(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            comprovaLogin();
            botoLogin.setEnabled(true);
            Toast.makeText(this,"Xarxa ok", Toast.LENGTH_LONG).show();
        } else {
            botoLogin.setEnabled(false);
            Toast.makeText(this,"Xarxa no disponible", Toast.LENGTH_LONG).show();
        }
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean connectat4G = networkInfo.isConnected();
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean connectatWifi = networkInfo.isConnected();
        if (connectat4G) {
            Toast.makeText(this,"Connectat per 4G", Toast.LENGTH_LONG).show();
        }if(connectatWifi){
            Toast.makeText(this,"Connectat per Wi-Fi", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receptor != null) {
            this.unregisterReceiver(receptor);
        }
    }

    public class ReceptorXarxa extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            comprovaConnectivitat();
        }
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
                    codiusuari = convertedObject.getJSONObject("dades").getString("codiusuari");
                    //Si el login es correcta acaba l'activitat, si no fa un toast dient que les dades son incorrectes
                    if(resultat) {
                        guardarDades();
                        nom = convertedObject.getJSONObject("dades").getString("nom");
                        Toast.makeText(getApplicationContext(), "Hola " + nom, Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(), codiusuari, Toast.LENGTH_LONG).show();
                        anarMainActivity();
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

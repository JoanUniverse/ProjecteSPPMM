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
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Missatge> missatges = new ArrayList<>();
    private ListView list;
    private String idUsuari;
    private String msg;
    EditText missatgeEditText;
    //final Handler handler = new Handler();
    //private Runnable getResponceAfterInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.listView);
        new RequestAsync().execute();
        //anarLoginActivity();
        Bundle data = getIntent().getExtras();
        idUsuari = data.getString("key");
        missatgeEditText = findViewById(R.id.enviaMissatgeEditText);
//        getResponceAfterInterval = new Runnable() {
//            public void run() {
//                try {
//                    new RequestAsync().execute();
//                } catch (Exception e) {
//                }
//                handler.postDelayed(this, 1000 * 10);
//            }
//        };
//        handler.post(getResponceAfterInterval);
    }

    private void anarLoginActivity() {
        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        //myIntent.putExtra("key", value);
        MainActivity.this.startActivity(myIntent);
    }

    public void onEviarButtonClick(View v)
    {
        msg = missatgeEditText.getText().toString();
        msg = msg.trim();
        if(msg.isEmpty() || msg == null) {
            Toast.makeText(this,"Missatge buit", Toast.LENGTH_LONG).show();
            missatgeEditText.setText("");
        } else {
            new RequestAsyncEnvia().execute();
        }
    }


    public class RequestAsync extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                //GET Request
                return RequestHandler.sendGet("http://52.44.95.114/quepassaeh/server/public/provamissatge/");
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null){
                try {
                    dbQuePasaAux db;
                    db = new dbQuePasaAux(getApplicationContext());
                    //db.borraMissatges();
                    JSONObject convertedObject = new JSONObject(s);
                    JSONArray jsonArray = convertedObject.getJSONArray("dades");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);
                        db.isEmpty();
                        db.guardarRecibido(explrObject);
                        System.out.println(explrObject);
                        Missatge missatge = new Gson().fromJson(explrObject.toString(), Missatge.class);
                        missatges.add(missatge);
                        System.out.println(missatge);
                    }
                    mostraMissatges();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        }
    }

    public class RequestAsyncEnvia extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                // POST Request
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("msg", msg);
                postDataParams.put("codiusuari", idUsuari);

                return RequestHandler.sendPost("http://52.44.95.114/quepassaeh/server/public/provamissatge/",postDataParams);
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null){
                missatgeEditText.setText("");
                new RequestAsync().execute();
            }
        }
    }

    public void mostraMissatges() {
        ArrayMissatge adapter = new ArrayMissatge(this, R.layout.missatge, missatges);
        list.setAdapter(adapter);
        list.setSelection(adapter.getCount() - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                borraPreferencies();
                Toast.makeText(getApplicationContext(), "Adeu", Toast.LENGTH_LONG).show();
                anarLoginActivity();
                finish();
                return true;
            case R.id.refresca:
                new RequestAsync().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void borraPreferencies(){
        SharedPreferences spref = getSharedPreferences("DadesGuardades", MODE_PRIVATE);
        SharedPreferences.Editor editor = spref.edit();
        editor.putString("email", null);
        editor.putString("password", null);
        editor.apply();
    }
}

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
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
    private ReceptorXarxa receptor;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.listView);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receptor = new ReceptorXarxa();this.registerReceiver(receptor, filter);
        anarLoginActivity();
        new RequestAsync().execute();
    }

    private void anarLoginActivity() {
        Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
        //myIntent.putExtra("key", value);
        MainActivity.this.startActivity(myIntent);
    }

    public void comprovaConnectivitat(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Toast.makeText(this,"Xarxa ok", Toast.LENGTH_LONG).show();
        } else {
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
                    JSONObject convertedObject = new JSONObject(s);
                    JSONArray jsonArray = convertedObject.getJSONArray("dades");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);
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
                finish();
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

package com.example.projectesppmm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ReceptorXarxa receptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receptor = new ReceptorXarxa();this.registerReceiver(receptor, filter);
        anarLoginActivity();
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
}

package com.example.projectesppmm;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class Auxiliar {
    public static String interacionPost(String arg1, String arg2, boolean login){
        URL url;
        StringBuilder text = new StringBuilder();
        try {
            if(login){
                url = new URL("http://52.44.95.114/quepassaeh/server/public/login/");
            } else{
                url = new URL("http://52.44.95.114/quepassaeh/server/public/provamissatge/");
            }
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setChunkedStreamingMode(25000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);httpURLConnection.setDoOutput(true);
            OutputStream out = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, Charset.forName("UTF-8")));
            if(login){
                writer.write("email=" + arg1 + "&password=" + arg2);
            }else{
                writer.write("msg=" + arg1 + "&codiusuari=" + Integer.parseInt(arg2));
            }
            writer.flush();
            writer.close();
            out.close();
            int responseCode = httpURLConnection.getResponseCode();
            Log.d("RUN", "Descarrega "+responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String liniatxt;while ((liniatxt = in.readLine()) != null) {
                    text.append(liniatxt);
                }
                in.close();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }
//    public static void mostrarMensajes(ListView lv, Context context, int ultimoMensaje, String idUser){
//        ArrayList<Mensaje> mensajes = dbQuePasaAux.listarMensajes(ultimoMensaje);
//        AdapterQuePasa adapter = new AdapterQuePasa(context, R.layout.mensaje_izquierda, mensajes, idUser);
//        lv.setAdapter(adapter);
//    }
}
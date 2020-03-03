package com.example.projectesppmm;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ArrayMissatge extends ArrayAdapter<Missatge> {
    private Context context;
    private List<Missatge> missatges;

    public ArrayMissatge(@NonNull MainActivity context, int resource, ArrayList<Missatge> missatges) {
        super(context, resource, missatges);
        this.context = context;
        this.missatges = missatges;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View getView(final int position, final View convertView, ViewGroup parent) {
        //agafam el Item per posició a l'Array
        final Missatge missatge = missatges.get(position);
        //agafam "l'inflater" per "inflar" el layout per a cada item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.missatge, null);

        //instanciam cada element del layout a utilitzar

        TextView nomUsuari = view.findViewById(R.id.nomUsuari);
        TextView dataMissatge = view.findViewById(R.id.dataMissatge);
        TextView textMissatge = view.findViewById(R.id.missatge);
        //omplim les dades
        nomUsuari.setText(missatge.getNom());
        SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long horaMissatge = 0;
        long dies = 0;
        long hores = 0;
        try {
            Date hora = formatter6.parse(missatge.getDatahora());
            Date now = new Date();
            horaMissatge = now.getTime() - hora.getTime();
            dies = TimeUnit.DAYS.convert(horaMissatge, TimeUnit.MILLISECONDS);
            hores = TimeUnit.HOURS.convert(horaMissatge, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(dies >= 1) {
            dataMissatge.setText("Fa " + dies + " dies");
        } else {
            dataMissatge.setText("Fa " + hores + " hores");
        }

        textMissatge.setText(missatge.getMsg());

        return view;
    }
}

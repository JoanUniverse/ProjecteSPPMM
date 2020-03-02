package com.example.projectesppmm;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ArrayMissatge extends ArrayAdapter<Missatge> {
    private Context context;
    private List<Missatge> missatges;

    public ArrayMissatge(@NonNull MainActivity context, int resource, ArrayList<Missatge> missatges) {
        super(context, resource, missatges);
        this.context = context;
        this.missatges = missatges;
    }

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
        //dataMissatge.setText("" + missatge.getDatahora());
        textMissatge.setText(missatge.getMsg());

        return view;
    }
}

package com.example.projectesppmm;

import java.util.Date;

public class Missatge {
    private int codimissatge;
    private String msg;
    //private Date datahora;
    private String nom;

    public Missatge(int codimissatge, String msg, String nom) {
        this.codimissatge = codimissatge;
        this.msg = msg;
//        this.datahora = datahora;
        this.nom = nom;
    }

    public int getCodimissatge() {
        return codimissatge;
    }

    public void setCodimissatge(int codimissatge) {
        this.codimissatge = codimissatge;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

//    public Date getDatahora() {
//        return datahora;
//    }
//
//    public void setDatahora(Date datahora) {
//        this.datahora = datahora;
//    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Missatge{" +
                "codimissatge=" + codimissatge +
                ", msg='" + msg + '\'' +
//                ", datahora=" + datahora +
                ", codiusuari='" + nom + '\'' +
                '}';
    }
}

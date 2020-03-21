package com.academy.shoplist.bean;

import android.graphics.Bitmap;

public class Immagine {

    private String id;
    private Bitmap contenuto;

    public Immagine(String id, Bitmap  contenuto) {
        this.id = id;
        this.contenuto = contenuto;
    }

    public Immagine() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bitmap  getContenuto() {
        return contenuto;
    }

    public void setContenuto(Bitmap contenuto) {
        this.contenuto = contenuto;
    }
}

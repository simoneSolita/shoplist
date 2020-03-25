package com.academy.shoplist.bean;

public class Immagine {

    private String id;
    private String contenuto;

    public Immagine(String id, String  contenuto) {
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

    public String  getContenuto() {
        return contenuto;
    }

    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }
}

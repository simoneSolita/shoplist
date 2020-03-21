package com.academy.shoplist.bean;

public class Prodotto {
    private String idImmagine;
    private String nome;
    private String descrizione;
    private String quantita;
    private String id;

    public Prodotto() {
    }

    public Prodotto(String id,String immagine, String nome, String descrizione,String quantita) {
        this.idImmagine = immagine;
        this.nome = nome;
        this.descrizione = descrizione;
        this.quantita = quantita;
        this.id = id;
    }

    public String getIdImmagine() {
        return idImmagine;
    }

    public void setIdImmagine(String idImmagine) {
        this.idImmagine = idImmagine;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getQuantita() {
        return quantita;
    }

    public void setQuantita(String quantita) {
        this.quantita = quantita;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

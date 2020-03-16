package com.academy.shoplist.singleton;

import com.academy.shoplist.bean.Prodotto;

import java.util.ArrayList;

public class ShoplistApplication {

//    private static ArrayList<Prodotto> prodotti = new ArrayList<>();

    private ShoplistApplication (){
    }

    //Instance
    private static ShoplistApplication instance;
    public static synchronized ShoplistApplication getInstance(){

        if(instance == null){
            synchronized (ShoplistApplication.class) {
                if(instance == null){
                    instance = new ShoplistApplication();
                }
            }
        }
        return instance;
    }

//    public void addProdottoToList(Prodotto prodottoToAdd){
//        prodotti.add(prodottoToAdd);
//    }
//
//    public ArrayList<Prodotto> getProdotti (){
//        return prodotti;
//    }
//
//    public Prodotto getProdottoAtPosition (int position){
//        if (prodotti != null) {
//            return prodotti.get(position);
//        }
//        return null;
//    }
//
//    public void removeProdottoAtPosition (int position){
//        if (prodotti != null) {
//            prodotti.remove(position);
//        }
//    }



}

package com.academy.shoplist.singleton;

public class ShoplistApplication {

    public static float widthScreen;
    public static float heightScreen;

    public static float density;
    public static float dpHeight;
    public static float dpWidth;

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



}

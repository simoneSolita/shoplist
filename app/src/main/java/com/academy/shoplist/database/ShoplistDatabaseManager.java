package com.academy.shoplist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.academy.shoplist.bean.Immagine;
import com.academy.shoplist.bean.Prodotto;
import com.academy.shoplist.constants.DbConstants;
import com.academy.shoplist.utils.AllegatiUtils;
import com.academy.shoplist.utils.Utility;

import java.util.ArrayList;

public class ShoplistDatabaseManager extends DatabaseManager {

    //Instance
    private static ShoplistDatabaseManager instance;

    private ShoplistDatabaseManager(Context context) {
        super(context);
    }

    public static synchronized ShoplistDatabaseManager getInstance(Context context) {

        if (instance == null) {
            synchronized (ShoplistDatabaseManager.class) {
                if (instance == null) {
                    instance = new ShoplistDatabaseManager(context);
                    instance.open();
                }
            }
        }
        return instance;
    }

    //Tabella prodotto

    public ArrayList<Prodotto> getProdottiByCursor(Cursor cursore) {
        ArrayList<Prodotto> listaProdotti = new ArrayList<>();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (cursore != null && cursore.getCount() != 0) {
            while (cursore.moveToNext()) {
                Prodotto prodotto = new Prodotto();
                prodotto.setNome(cursore.getString(cursore.getColumnIndex(DbConstants.PRODOTTI_TABLE_NOME)));
                prodotto.setDescrizione(cursore.getString(cursore.getColumnIndex(DbConstants.PRODOTTI_TABLE_DESCRIZIONE)));
                prodotto.setIdImmagine(cursore.getString(cursore.getColumnIndex(DbConstants.PRODOTTI_TABLE_ID_IMMAGINE)));
                prodotto.setQuantita(cursore.getString(cursore.getColumnIndex(DbConstants.PRODOTTI_TABLE_QUANTITA)));
                listaProdotti.add(prodotto);
            }
            cursore.close();
        } else if (cursore != null) {
            cursore.close();
        }
        return listaProdotti;
    }

    public void addProdotto(Prodotto prodotto) {
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.PRODOTTI_TABLE_NOME, prodotto.getNome());
            values.put(DbConstants.PRODOTTI_TABLE_ID, prodotto.getId());
            values.put(DbConstants.PRODOTTI_TABLE_DESCRIZIONE, prodotto.getDescrizione());
            values.put(DbConstants.PRODOTTI_TABLE_ID_IMMAGINE, prodotto.getIdImmagine());
            values.put(DbConstants.PRODOTTI_TABLE_QUANTITA, prodotto.getQuantita());
            database.insert(DbConstants.PRODOTTI_TABLE, null, values);
            Log.i("Elemento inserito ", "Prodotto con nome : " + prodotto.getNome());
            database.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }

    public Cursor getProdottiToShow() {
        return database.query(DbConstants.PRODOTTI_TABLE, null, null, null, null, null, null);
    }

    public Cursor getProdottiById(String id) {
        return database.query(DbConstants.PRODOTTI_TABLE, null, DbConstants.PRODOTTI_TABLE_ID + "=?", new String[]{id}, null, null, null);
    }

    public void deleteProdottoById(String id) {
        Log.d("Prodotti eliminati", ": " + database.delete(DbConstants.PRODOTTI_TABLE, DbConstants.PRODOTTI_TABLE_ID + " = ?", new String[]{id}));
    }

    public void updateProdottoByID(Prodotto prodotto, String idProdotto) {
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.PRODOTTI_TABLE_NOME, prodotto.getNome());
            values.put(DbConstants.PRODOTTI_TABLE_DESCRIZIONE, prodotto.getDescrizione());
            values.put(DbConstants.PRODOTTI_TABLE_QUANTITA, prodotto.getQuantita());
            values.put(DbConstants.PRODOTTI_TABLE_ID_IMMAGINE, prodotto.getIdImmagine());
            Log.d("Prodotti modificati"," Prodotti modificati : " +database.update(DbConstants.PRODOTTI_TABLE, values, DbConstants.PRODOTTI_TABLE_ID + " = ?", new String[]{idProdotto}));
            database.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }

    //Tabella Immagine

    public ArrayList<Immagine> getImmaginiByCursor(Cursor cursore) {
        ArrayList<Immagine> listaImmagini = new ArrayList<>();
        if (cursore != null && cursore.getCount() != 0) {
            while (cursore.moveToNext()) {
                Immagine immagine = new Immagine();
                immagine.setId(cursore.getString(cursore.getColumnIndex(DbConstants.IMMAGINE_TABLE_ID)));
                byte[] byteContenutoImmagine = cursore.getBlob(cursore.getColumnIndex(DbConstants.IMMAGINE_TABLE_CONTENUTO));
                if (byteContenutoImmagine != null && byteContenutoImmagine.length > 0) {
                    immagine.setContenuto(BitmapFactory.decodeByteArray(byteContenutoImmagine, 0, byteContenutoImmagine.length));
                } else {
                    Log.e("Error immage", "Errore conversione contenuto immagine con ID " + immagine.getId());
                }
                listaImmagini.add(immagine);
            }
            cursore.close();
        } else if (cursore != null) {
            cursore.close();
        }
        return listaImmagini;
    }

    public void addImmagine(Immagine immagine) {
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(DbConstants.IMMAGINE_TABLE_ID, immagine.getId());
            byte[] byteContenuto = AllegatiUtils.getBitmapAsByteArray(immagine.getContenuto());
            values.put(DbConstants.IMMAGINE_TABLE_CONTENUTO, byteContenuto);
            database.insert(DbConstants.IMMAGINE_TABLE, null, values);
            Log.i("Elemento inserito ", "Immagine con id : " + immagine.getId());
            database.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }

    public Cursor getImmagini() {
        return database.query(DbConstants.IMMAGINE_TABLE, null, null, null, null, null, null);
    }

    public Cursor getImmagineById(String id) {
        return database.query(DbConstants.IMMAGINE_TABLE, null, DbConstants.IMMAGINE_TABLE_ID + "=?", new String[]{id}, null, null, null);
    }

    public void deleteImmagineById(String id) {
        Log.d("Immagini eliminate", ": " + database.delete(DbConstants.IMMAGINE_TABLE, DbConstants.IMMAGINE_TABLE_ID + " = ?", new String[]{id}));
    }

    public void updateImmagineByID(Immagine immagine, String idImmagine) {
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            byte[] byteContenuto = AllegatiUtils.getBitmapAsByteArray(immagine.getContenuto());
            values.put(DbConstants.IMMAGINE_TABLE_CONTENUTO, byteContenuto);
            Log.d("Immagini modificate","Immagini modificate : " +database.update(DbConstants.IMMAGINE_TABLE, values, DbConstants.IMMAGINE_TABLE_ID + " = ?", new String[]{idImmagine}));
            database.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }
}

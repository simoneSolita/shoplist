package com.academy.shoplist.database;

import com.academy.shoplist.constants.DbConstants;

public class DatabaseTables {

    public static final String SQL_CREATE_PRODOTTO = " CREATE TABLE IF NOT EXISTS " + DbConstants.PRODOTTI_TABLE + " (" +
            DbConstants.PRODOTTI_TABLE_ID                 + " TEXT, " +
            DbConstants.PRODOTTI_TABLE_NOME               + " TEXT, "+
            DbConstants.PRODOTTI_TABLE_DESCRIZIONE        + " TEXT, "+
            DbConstants.PRODOTTI_TABLE_QUANTITA           + " TEXT, "+
            DbConstants.PRODOTTI_TABLE_ID_IMMAGINE + " INTEGER"+
            " );";

    public static final String SQL_CREATE_IMMAGINE = " CREATE TABLE IF NOT EXISTS " + DbConstants.IMMAGINE_TABLE + " (" +
            DbConstants.IMMAGINE_TABLE_ID              + " TEXT, " +
            DbConstants.IMMAGINE_TABLE_CONTENUTO       + " BLOB"+
            " );";

}

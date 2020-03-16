package com.academy.shoplist.database;

import com.academy.shoplist.constants.DbConstants;

public class DatabaseTables {

    public static final String SQL_CREATE_PRODOTTO = " CREATE TABLE IF NOT EXISTS " + DbConstants.PRODOTTI_TABLE + " (" +
            DbConstants.PRODOTTI_TABLE_ID                 + " TEXT, " +
            DbConstants.PRODOTTI_TABLE_NOME               + " TEXT, "+
            DbConstants.PRODOTTI_TABLE_DESCRIZIONE        + " TEXT, "+
            DbConstants.PRODOTTI_TABLE_IMG                + " INTEGER"+
            " );";

}

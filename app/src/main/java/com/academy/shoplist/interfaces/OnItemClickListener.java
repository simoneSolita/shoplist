package com.academy.shoplist.interfaces;

public interface OnItemClickListener {

    public void onItemClick(String idProdotto);

    public void onItemDelete(String idProdotto);

    public void onItemEdit(String idProdotto);
}

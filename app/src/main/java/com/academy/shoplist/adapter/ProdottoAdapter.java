package com.academy.shoplist.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.academy.shoplist.R;
import com.academy.shoplist.bean.Immagine;
import com.academy.shoplist.bean.Prodotto;
import com.academy.shoplist.database.ShoplistDatabaseManager;
import com.academy.shoplist.interfaces.OnItemClickListener;
import com.academy.shoplist.singleton.ShoplistApplication;
import com.academy.shoplist.utils.AllegatiUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;

public class ProdottoAdapter extends RecyclerView.Adapter<ProdottoAdapter.ProdottoViewHolder> {

    private ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();
    private OnItemClickListener listener;
    private Context context;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ProdottoViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_immagine_prodotto;
        public ImageView img_elimina_prodotto;
        public ImageView img_modifica_prodotto;
        public TextView textView_nomeProdotto;
        public TextView textView_descrizioneProdotto;
        public TextView textView_quantitaProdotto;
        public TextView textView_idProdotto;

        public ProdottoViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            img_immagine_prodotto = itemView.findViewById(R.id.immagine_prodotto);
            img_modifica_prodotto = itemView.findViewById(R.id.modifica_prodotto);
            textView_nomeProdotto = itemView.findViewById(R.id.nome_prodotto);
            textView_descrizioneProdotto = itemView.findViewById(R.id.descrizione_prodotto);
            textView_quantitaProdotto = itemView.findViewById(R.id.quantita_prodotto);
            textView_idProdotto = itemView.findViewById(R.id.id_prodotto);
            img_elimina_prodotto = itemView.findViewById(R.id.elimina_prodotto);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(textView_idProdotto.getText().toString());
                        }
                    }
                }
            });

            img_elimina_prodotto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemDelete(textView_idProdotto.getText().toString());
                        }
                    }
                }
            });
            img_modifica_prodotto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemEdit(textView_idProdotto.getText().toString());
                        }
                    }
                }
            });
        }
    }

    public ProdottoAdapter(ArrayList<Prodotto> listaProdotti, Context context) {
        this.prodotti = listaProdotti;
        this.context = context;
    }


    @NonNull
    @Override
    public ProdottoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.prodotto_item, parent, false);
        return new ProdottoViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdottoViewHolder holder, int position) {
        Prodotto prodottoCorrente = prodotti.get(position);
        try {
            Immagine immagineProdotto = new Immagine();
            if (!TextUtils.isEmpty(prodottoCorrente.getIdImmagine())) {
                ArrayList<Immagine> immagini = ShoplistDatabaseManager.getInstance(context).getImmaginiByCursor(ShoplistDatabaseManager.getInstance(context).getImmagineById(prodottoCorrente.getIdImmagine()));
                if (immagini != null && !immagini.isEmpty()) {
                    immagineProdotto = immagini.get(0);
                    Bitmap bitmapAllegato = AllegatiUtils.getBitmapByBase64(immagineProdotto.getContenuto());


                    Uri uri = Uri.fromFile(File.createTempFile("temp_file_name", ".jpg", context.getCacheDir()));
                    OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
                    bitmapAllegato.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.close();


                    Picasso.with(context).load(uri).resize(68, 68).
                            centerCrop().into(holder.img_immagine_prodotto);
                    holder.img_immagine_prodotto.setImageBitmap(bitmapAllegato);
                }
            }
            holder.textView_nomeProdotto.setText(prodottoCorrente.getNome());
            holder.textView_descrizioneProdotto.setText(prodottoCorrente.getDescrizione());
            holder.textView_quantitaProdotto.setText(prodottoCorrente.getQuantita());
            holder.textView_idProdotto.setText(prodottoCorrente.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return prodotti.size();
    }


}

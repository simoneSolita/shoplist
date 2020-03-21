package com.academy.shoplist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.academy.shoplist.R;
import com.academy.shoplist.bean.Prodotto;
import com.academy.shoplist.interfaces.OnItemClickListener;

import java.util.ArrayList;

public class ProdottoAdapter extends RecyclerView.Adapter<ProdottoAdapter.ProdottoViewHolder> {

    private ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();
    private OnItemClickListener listener;

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

    public ProdottoAdapter(ArrayList<Prodotto> listaProdotti) {
        this.prodotti = listaProdotti;
    }


    @NonNull
    @Override
    public ProdottoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.prodotto_item, parent, false);
        ProdottoViewHolder pvh = new ProdottoViewHolder(v, listener);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ProdottoViewHolder holder, int position) {
        Prodotto prodottoCorrente = prodotti.get(position);
        try {
            //TODO riattivare
//            holder.img_immagine_prodotto.setImageResource(prodottoCorrente.getImmagine());
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

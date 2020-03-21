package com.academy.shoplist.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.academy.shoplist.R;
import com.academy.shoplist.activity.MainActivity;
import com.academy.shoplist.bean.Prodotto;
import com.academy.shoplist.constants.IntentConstant;
import com.academy.shoplist.database.ShoplistDatabaseManager;
import com.academy.shoplist.interfaces.FragmentModificaListener;

import java.util.ArrayList;

public class DettaglioProdottoFragment extends Fragment {

    private TextView textview_name;
    private TextView textview_descrizione;
    private TextView textview_quantita;
    private Context context = getActivity();
    private String idProdotto;
    private Prodotto p;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_view, container, false);
        if (getArguments() != null) {
            idProdotto = getArguments().getString(IntentConstant.ID_PRODOTTO);
        }
        setView(v);
        setContent();

        return v;
    }

    private void setView(View v) {
        textview_name = v.findViewById(R.id.nome_stampa);
        textview_descrizione = v.findViewById(R.id.descrizione_stampa);
        textview_quantita = v.findViewById(R.id.quantita_valore);
    }

    private void setContent() {
        if (!TextUtils.isEmpty(idProdotto)) {
            ArrayList<Prodotto> prodotti = ShoplistDatabaseManager.getInstance(context).getProdottiByCursor(ShoplistDatabaseManager.getInstance(context).getProdottiById(idProdotto));
            if (!prodotti.isEmpty()) {
                p = prodotti.get(0);
                textview_name.setText(p.getNome());
                textview_descrizione.setText(p.getDescrizione());
                textview_quantita.setText(p.getQuantita());
            } else {
                Toast.makeText(context, context.getString(R.string.id_prodotto_non_reperito), Toast.LENGTH_LONG).show();
            }
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentModificaListener) {
            //listener = (FragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "MUST IMPLEMENT FRAGMENTVIEWLISTENER");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //listener = null;
    }
}

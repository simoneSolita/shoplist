package com.academy.shoplist.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.academy.shoplist.R;
import com.academy.shoplist.bean.Prodotto;
import com.academy.shoplist.constants.IntentConstant;
import com.academy.shoplist.database.ShoplistDatabaseManager;
import com.academy.shoplist.interfaces.FragmentModificaListener;

import java.util.ArrayList;


public class ModificaProdottoFragment extends Fragment {

    private String idProdotto;
    private Button confirm_Edit_Button;
    private FragmentModificaListener listener;
    private Context context = getActivity();
    private Prodotto p;

    private EditText editText_nome_prodotto;
    private EditText editText_descrizione_prodotto;
    private EditText editText_quantita_prodotto;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_edit, container, false);
        if (getArguments() != null) {
            idProdotto = getArguments().getString(IntentConstant.ID_PRODOTTO);
        }

        setView(v);
        setListeners();
        setContent();

        return v;
    }

    private void setView(View v){
        confirm_Edit_Button = (Button) v.findViewById(R.id.confirm_edit);
        editText_nome_prodotto = (EditText) v.findViewById(R.id.editName);
        editText_descrizione_prodotto = (EditText) v.findViewById(R.id.editDescription);
        editText_quantita_prodotto = (EditText) v.findViewById(R.id.editQuantita);
    }

    private void setListeners(){
        confirm_Edit_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO modifica immagine
                p.setQuantita(editText_quantita_prodotto.getText().toString());
                p.setNome(editText_nome_prodotto.getText().toString());
                p.setDescrizione(editText_descrizione_prodotto.getText().toString());
                ShoplistDatabaseManager.getInstance(getActivity()).updateProdottoByID(p,idProdotto);
            }
        });
    }

    private void setContent(){
        if (!TextUtils.isEmpty(idProdotto)) {
            ArrayList<Prodotto> prodotti = ShoplistDatabaseManager.getInstance(context).getProdottiByCursor(ShoplistDatabaseManager.getInstance(context).getProdottiById(idProdotto));
            if (!prodotti.isEmpty()) {
                p = prodotti.get(0);
                editText_nome_prodotto.setText(p.getNome());
                editText_descrizione_prodotto.setText(p.getDescrizione());
                editText_quantita_prodotto.setText(p.getQuantita());
            } else {
                Toast.makeText(context, context.getString(R.string.id_prodotto_non_reperito), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, context.getString(R.string.id_prodotto_non_reperito), Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentModificaListener) {
            listener = (FragmentModificaListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "MUST IMPLEMENT FRAGMENTEDITLISTENER");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}

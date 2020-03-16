package com.academy.shoplist.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.academy.shoplist.R;
import com.academy.shoplist.bean.Prodotto;
import com.academy.shoplist.constants.IntentConstant;
import com.academy.shoplist.database.ShoplistDatabaseManager;
import com.academy.shoplist.singleton.ShoplistApplication;

import java.util.ArrayList;

public class DettaglioProdottoActivity extends AppCompatActivity {

    String nomeProdotto;
    int modalitaApertura;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dettaglio_prodotto_activity);

        if (getIntent() != null){
            if (getIntent().hasExtra(IntentConstant.NOME_PRODOTTO)){
                nomeProdotto = getIntent().getStringExtra(IntentConstant.NOME_PRODOTTO);
            }
            if (getIntent().hasExtra(IntentConstant.MODALITA_APERTURA)){
                modalitaApertura = getIntent().getIntExtra(IntentConstant.MODALITA_APERTURA,0);
            }
        }
        setActionbar();
        setContent();
    }

    private void setContent() {

        TextView txtViewValoreNomeProdotto = (TextView) findViewById(R.id.textView_valore_nome_prodotto);
        TextView txtViewValoreDescrizioneProdotto = (TextView) findViewById(R.id.textView_valore_descrizione_prodotto);
        ImageView imgDettaglioProdotto = (ImageView) findViewById(R.id.img_dettaglio_img_prodotto);

        Prodotto prodottoToShow = null;
        if (!TextUtils.isEmpty(nomeProdotto)) {
            ArrayList<Prodotto> prodottiToShow = ShoplistDatabaseManager.getInstance(DettaglioProdottoActivity.this).getProdottiByCursor(ShoplistDatabaseManager.getInstance(DettaglioProdottoActivity.this).getProdottiByNome(nomeProdotto));
            if (prodottiToShow != null && !prodottiToShow.isEmpty()) {
                prodottoToShow = prodottiToShow.get(0);
                if (prodottoToShow != null) {
                    txtViewValoreNomeProdotto.setText(prodottoToShow.getNome());
                    txtViewValoreDescrizioneProdotto.setText(prodottoToShow.getDescrizione());
                    imgDettaglioProdotto.setImageResource(prodottoToShow.getImmagine());
                } else {
                    Toast.makeText(this, R.string.nessun_elemento_da_visualizzare, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    // imposta l'aspetto iniziale della actionBar
    private void setActionbar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);
        TextView textToolbar = (TextView) findViewById(R.id.textViewTitolo);
        textToolbar.setText(R.string.dettaglio_prodotto);
        ImageView imageBack = (ImageView) findViewById(R.id.back_toolbar);
        imageBack.setVisibility(View.VISIBLE);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

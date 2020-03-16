package com.academy.shoplist.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class AggiungiProdottoActivity  extends AppCompatActivity {

    String nomeProdotto;
    String descrizioneProdotto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aggiungi_prodotto_activity);
        setActionbar();
        setContent();
    }

    private void setContent(){
        final EditText editNomeProdotto = (EditText) findViewById(R.id.editText_nome_prodotto);
        final EditText editDescrizioneProdotto = (EditText) findViewById(R.id.editText_descrizione_prodotto);

        Button btnAggiungi = (Button) findViewById(R.id.btnAggiungi);
        btnAggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nomeProdotto = editNomeProdotto.getText().toString();
                descrizioneProdotto = editDescrizioneProdotto.getText().toString();

                if (TextUtils.isEmpty(nomeProdotto) || TextUtils.isEmpty(descrizioneProdotto)){
                    Toast.makeText(AggiungiProdottoActivity.this, R.string.campi_non_valorizzati,Toast.LENGTH_LONG).show();
                }else{
                    //right
                    ShoplistDatabaseManager.getInstance(AggiungiProdottoActivity.this).addProdotto(new Prodotto(R.drawable.lavatrice,nomeProdotto,descrizioneProdotto));
                    setResult(IntentConstant.RISULTATO_AGGIUNTA_OK);
                    finish();
                }
            }
        });
    }

    // imposta l'aspetto iniziale della actionBar
    private void setActionbar(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);
        TextView textToolbar = (TextView) findViewById(R.id.textViewTitolo);
        textToolbar.setText(R.string.add_product);
        ImageView imageBack = (ImageView) findViewById(R.id.back_toolbar);
        imageBack.setVisibility(View.VISIBLE);
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });
    }

    private void finishActivity(){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.attenzione))
                .setMessage(this.getString(R.string.conferma_back))
                .setCancelable(true)
                .setPositiveButton(this.getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(IntentConstant.RISULTATO_AGGIUNTA_KO);
                        finish();
                    }
                })
                .setNegativeButton(this.getString(R.string.NO), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}

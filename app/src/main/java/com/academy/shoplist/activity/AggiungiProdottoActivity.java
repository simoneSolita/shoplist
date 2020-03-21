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
import com.academy.shoplist.bean.Immagine;
import com.academy.shoplist.bean.Prodotto;
import com.academy.shoplist.constants.IntentConstant;
import com.academy.shoplist.database.ShoplistDatabaseManager;
import com.academy.shoplist.utils.Utility;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

public class AggiungiProdottoActivity extends AppCompatActivity {

    String nomeProdotto;
    String descrizioneProdotto;
    String quantita;
    String idProdotto;
    String idImmagine;
    Immagine immagine;
    boolean isImmagineSalvata;

    EditText editNomeProdotto;
    EditText editDescrizioneProdotto;
    EditText editDescrizioneQuantita;
    AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aggiungi_prodotto_activity);
        setActionbar();
        setContent();
        setValidation();
        idProdotto = Utility.createIdProdotto();
        idImmagine = Utility.createIdImmagine();
    }

    private void setContent() {
        editNomeProdotto        = (EditText) findViewById(R.id.editText_nome_prodotto);
        editDescrizioneProdotto = (EditText) findViewById(R.id.editText_descrizione_prodotto);
        editDescrizioneQuantita = (EditText) findViewById(R.id.editText_quantita);
    }

    // imposta l'aspetto iniziale della actionBar
    private void setActionbar() {
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

        Button buttonConferma = (Button) findViewById(R.id.conferma_toolbar);
        buttonConferma.setVisibility(View.VISIBLE);
        buttonConferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (awesomeValidation.validate()) {
                    nomeProdotto = editNomeProdotto.getText().toString();
                    descrizioneProdotto = editDescrizioneProdotto.getText().toString();
                    quantita = editDescrizioneQuantita.getText().toString();
                    //Salvo eventualmente l'immagine
                    if (immagine != null && !TextUtils.isEmpty(immagine.getId()) && immagine.getContenuto() != null) {
                        ShoplistDatabaseManager.getInstance(AggiungiProdottoActivity.this).addImmagine(immagine);
                        isImmagineSalvata = true;
                    }
                    // se è stata salvata un'immagine, inserisco l'id, altrimenti null
                    String idImmagineAssociata = isImmagineSalvata ? idImmagine : null;
                    ShoplistDatabaseManager.getInstance(AggiungiProdottoActivity.this).addProdotto(new Prodotto(idProdotto, idImmagineAssociata, nomeProdotto, descrizioneProdotto, quantita));
                    setResult(IntentConstant.RISULTATO_AGGIUNTA_OK);
                    finish();
                }
            }
        });
    }

    private void finishActivity() {
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

    private void setValidation() {
        awesomeValidation.addValidation(AggiungiProdottoActivity.this, R.id.editText_nome_prodotto, RegexTemplate.NOT_EMPTY, R.string.nome_non_valorizzato);
    }

}

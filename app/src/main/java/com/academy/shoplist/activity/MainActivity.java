package com.academy.shoplist.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.academy.shoplist.R;
import com.academy.shoplist.adapter.ProdottoAdapter;
import com.academy.shoplist.bean.Prodotto;
import com.academy.shoplist.constants.IntentConstant;
import com.academy.shoplist.database.ShoplistDatabaseManager;
import com.academy.shoplist.interfaces.OnItemClickListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ProdottoAdapter mAdapter;
    private LinearLayoutManager mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setActionbar();
        setContent();
    }

    private void setContent(){
        mRecyclerView = findViewById(R.id.RecycleView);
        mLayout = new LinearLayoutManager(this);
        createAdapter();

    }

    // imposta l'aspetto iniziale della actionBar
    private void setActionbar(){
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);
        ImageView imageAdd = (ImageView) findViewById(R.id.aggiungi_toolbar);
        TextView textToolbar = (TextView) findViewById(R.id.textViewTitolo);
        textToolbar.setText(R.string.your_product);
        imageAdd.setVisibility(View.VISIBLE);
        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToAddProdotto = new Intent(MainActivity.this,AggiungiProdottoActivity.class);
                startActivityForResult(intentToAddProdotto, IntentConstant.AGGIUNGI_PRODOTTO);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case IntentConstant.AGGIUNGI_PRODOTTO:
                if (resultCode == IntentConstant.RISULTATO_AGGIUNTA_OK){
                    createAdapter();
                }else if (resultCode == IntentConstant.RISULTATO_AGGIUNTA_KO){
                    Toast.makeText(this,R.string.nessun_prodotto_aggiunto,Toast.LENGTH_LONG).show();
                }
                break;
            default:
                Toast.makeText(this,R.string.errore_generico,Toast.LENGTH_LONG).show();
        }
    }

    public void createAdapter(){
        mAdapter = new ProdottoAdapter(ShoplistDatabaseManager.getInstance(MainActivity.this).getProdottiByCursor(ShoplistDatabaseManager.getInstance(MainActivity.this).getProdottiToShow()));
        mRecyclerView.setLayoutManager(mLayout);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(String idProdotto) {
                if (!TextUtils.isEmpty(idProdotto)){
                    Intent intentToDettaglio = new Intent(MainActivity.this,DettaglioProdottoActivity.class);
                    intentToDettaglio.putExtra(IntentConstant.ID_PRODOTTO,idProdotto);
                    intentToDettaglio.putExtra(IntentConstant.MODALITA_APERTURA,IntentConstant.VISUALIZZA);
                    startActivity(intentToDettaglio);
                }else{
                    Toast.makeText(MainActivity.this,MainActivity.this.getString(R.string.id_prodotto_non_reperito),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onItemDelete(final String idProdotto) {
                if (!TextUtils.isEmpty(idProdotto)){
                    final ArrayList<Prodotto> prodottiToDelete = ShoplistDatabaseManager.getInstance(MainActivity.this).getProdottiByCursor(ShoplistDatabaseManager.getInstance(MainActivity.this).getProdottiById(idProdotto));
                    if (prodottiToDelete != null && !prodottiToDelete.isEmpty()) {
                        final Prodotto prodotto = prodottiToDelete.get(0);
                        if (prodotto != null) {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle(getString(R.string.attenzione))
                                    .setMessage(MainActivity.this.getString(R.string.conferma_eliminazione) + prodotto.getNome() + "?")
                                    .setCancelable(false)
                                    .setPositiveButton(MainActivity.this.getString(R.string.OK), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            ShoplistDatabaseManager.getInstance(MainActivity.this).deleteProdottoById(prodotto.getId());
                                            createAdapter();
                                        }
                                    })
                                    .setNegativeButton(MainActivity.this.getString(R.string.NO), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        } else {
                            Toast.makeText(MainActivity.this, R.string.errore_generico, Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this,MainActivity.this.getString(R.string.id_prodotto_non_reperito),Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this,MainActivity.this.getString(R.string.id_prodotto_non_reperito),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onItemEdit(String idProdotto) {
                if (!TextUtils.isEmpty(idProdotto)){
                    Intent intentToDettaglio = new Intent(MainActivity.this,DettaglioProdottoActivity.class);
                    intentToDettaglio.putExtra(IntentConstant.ID_PRODOTTO,idProdotto);
                    intentToDettaglio.putExtra(IntentConstant.MODALITA_APERTURA,IntentConstant.MODIFICA);
                    startActivity(intentToDettaglio);
                }else{
                    Toast.makeText(MainActivity.this,MainActivity.this.getString(R.string.id_prodotto_non_reperito),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

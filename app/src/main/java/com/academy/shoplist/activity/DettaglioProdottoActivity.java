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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.academy.shoplist.R;
import com.academy.shoplist.constants.IntentConstant;
import com.academy.shoplist.fragment.DettaglioProdottoFragment;
import com.academy.shoplist.fragment.ModificaProdottoFragment;
import com.academy.shoplist.interfaces.FragmentModificaListener;

public class DettaglioProdottoActivity extends AppCompatActivity implements FragmentModificaListener {

    String idProdotto;
    int modalitaApertura;

    private ModificaProdottoFragment editFragment;
    private DettaglioProdottoFragment viewFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dettaglio_prodotto_activity);

        if (getIntent() != null) {
            if (getIntent().hasExtra(IntentConstant.ID_PRODOTTO)) {
                idProdotto = getIntent().getStringExtra(IntentConstant.ID_PRODOTTO);
            }
            if (getIntent().hasExtra(IntentConstant.MODALITA_APERTURA)) {
                modalitaApertura = getIntent().getIntExtra(IntentConstant.MODALITA_APERTURA, 0);
            }
        }
        setActionbar();

        Bundle args = new Bundle();
        args.putString(IntentConstant.ID_PRODOTTO, idProdotto);

        if (IntentConstant.VISUALIZZA == modalitaApertura) {
            viewFragment = new DettaglioProdottoFragment();
            viewFragment.setArguments(args);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, viewFragment);
            fragmentTransaction.commit();
        } else if (IntentConstant.MODIFICA == modalitaApertura) {
            editFragment = new ModificaProdottoFragment();
            editFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, editFragment)
                    .commit();
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

    @Override
    public int onItemClicked(int position) {
        //TODO modificare
        return 0;
    }
}

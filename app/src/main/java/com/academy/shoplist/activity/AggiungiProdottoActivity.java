package com.academy.shoplist.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.appcompat.app.AlertDialog;

import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.academy.shoplist.R;
import com.academy.shoplist.bean.Immagine;
import com.academy.shoplist.bean.Prodotto;
import com.academy.shoplist.constants.IntentConstant;
import com.academy.shoplist.constants.MainConstant;
import com.academy.shoplist.database.ShoplistDatabaseManager;
import com.academy.shoplist.utils.AllegatiUtils;
import com.academy.shoplist.utils.Utility;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

public class AggiungiProdottoActivity extends AppCompatActivity {

    String nomeProdotto;
    String descrizioneProdotto;
    String quantita;
    String idProdotto;
    String idImmagine;
    Immagine immagine = new Immagine();
    boolean isImmagineSalvata;
    private File photoFile;

    EditText editNomeProdotto;
    EditText editDescrizioneProdotto;
    EditText editDescrizioneQuantita;
    ImageView imgView_immagine;
    Button btn_seleziona_immagine;
    AwesomeValidation awesomeValidation = new AwesomeValidation(ValidationStyle.COLORATION);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aggiungi_prodotto_activity);
        setActionbar();
        setContent();
        setListeners();
        setValidation();
        idProdotto = Utility.createIdProdotto();
        idImmagine = Utility.createIdImmagine();
    }

    private void setContent() {
        editNomeProdotto = (EditText) findViewById(R.id.editText_nome_prodotto);
        editDescrizioneProdotto = (EditText) findViewById(R.id.editText_descrizione_prodotto);
        editDescrizioneQuantita = (EditText) findViewById(R.id.editText_quantita);
        imgView_immagine = (ImageView) findViewById(R.id.image_prodotto);
        btn_seleziona_immagine = (Button) findViewById(R.id.aggiungi_immagine_button);
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
                    // se è stata salvata un'immagine, inserisco l'id, altrimenti null
                    String idImmagineAssociata = isImmagineSalvata ? idImmagine : null;
                    ShoplistDatabaseManager.getInstance(AggiungiProdottoActivity.this).addProdotto(new Prodotto(idProdotto, idImmagineAssociata, nomeProdotto, descrizioneProdotto, quantita));
                    setResult(IntentConstant.RISULTATO_AGGIUNTA_OK);
                    finish();
                }
            }
        });
    }

    private void setValidation() {
        awesomeValidation.addValidation(AggiungiProdottoActivity.this, R.id.editText_nome_prodotto, RegexTemplate.NOT_EMPTY, R.string.nome_non_valorizzato);
    }

    private void setListeners() {
        btn_seleziona_immagine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //Permission not granted, ask them
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, IntentConstant.PERMISSION_CODE);
                    } else {
                        openDialogGetImage();
                    }
                } else {
                    openDialogGetImage();
                }
            }
        });
    }

    private void openDialogGetImage() {
        new AlertDialog.Builder(AggiungiProdottoActivity.this)
                .setTitle(getString(R.string.seleziona_da_dove_reperire_immagine))
                .setCancelable(true)
                .setPositiveButton(AggiungiProdottoActivity.this.getString(R.string.inserimento_foto_camera), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        openCameraForImmagine();
                    }
                })
                //TODO riabilitare
//                .setNegativeButton(AggiungiProdottoActivity.this.getString(R.string.inserimento_foto_galleria), new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        openGalleryForImage();
//                    }
//                })
                .show();
    }

    private void openCameraForImmagine() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = null;
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                // Create the File where the photo should go
                photoFile = AllegatiUtils.createImageFile(idImmagine);

            } catch (IOException ex) {
                Toast.makeText(AggiungiProdottoActivity.this, R.string.impossibile_salvare_immagine_riprova, Toast.LENGTH_LONG).show();
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri imageUri = FileProvider.getUriForFile(
                        AggiungiProdottoActivity.this,
                        "com.academy.shoplist.provider", //(use your app signature + ".provider" )
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, IntentConstant.IMAGE_PICK_CODE_CAMERA);
            }

        }
    }

    private void openGalleryForImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Pick an image"), IntentConstant.IMAGE_PICK_CODE_GALLERY);
    }

    private void refreshImageView() {
        ArrayList<Immagine> listaImmagini = ShoplistDatabaseManager.getInstance(AggiungiProdottoActivity.this).getImmaginiByCursor(ShoplistDatabaseManager.getInstance(AggiungiProdottoActivity.this).getImmagineById(idImmagine));
        if (listaImmagini != null && !listaImmagini.isEmpty()) {
            Bitmap bitmapAllegato = AllegatiUtils.getBitmapByBase64(listaImmagini.get(0).getContenuto());
            imgView_immagine.setImageBitmap(bitmapAllegato);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case IntentConstant.PERMISSION_CODE:
                boolean validation = true;
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            validation = false;
                            break;
                        }
                    }
                } else {
                    validation = false;
                }
                if (validation) {
                    openDialogGetImage();
                } else {
                    Toast.makeText(AggiungiProdottoActivity.this, AggiungiProdottoActivity.this.getString(R.string.permission_not_granted), Toast.LENGTH_LONG).show();
                }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IntentConstant.IMAGE_PICK_CODE_GALLERY:
                    try {
                        InputStream is = getContentResolver().openInputStream(data.getData());
                        byte[] targetArray = new byte[0];
                        try {
                            targetArray = new byte[is.available()];
                            is.read(targetArray);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Bitmap bm = BitmapFactory.decodeStream(is);
                        String base64Immagine = Base64.encodeToString(targetArray,Base64.DEFAULT);
                        immagine.setContenuto(base64Immagine);
                        immagine.setId(idImmagine);

                        //elimino l'immagine precedente
                        ShoplistDatabaseManager.getInstance(AggiungiProdottoActivity.this).deleteImmagineById(idImmagine);

                        //salvo l'immagine su DataBase
                        ShoplistDatabaseManager.getInstance(AggiungiProdottoActivity.this).addImmagine(immagine);
                        //setto a true il salvataggio dell'immagine
                        isImmagineSalvata = true;

                        refreshImageView();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                case IntentConstant.IMAGE_PICK_CODE_CAMERA:
                    String imgDecodableString = photoFile.getPath();
                    String imageName = AllegatiUtils.getLocalLowQName(photoFile.getName(), false);
                    Bitmap bm = AllegatiUtils.decodeSampledBitmapFromResource(imgDecodableString, MainConstant.MAX_IMAGE_SIDE_LENGTH);
                    immagine.setId(idImmagine);
                    String base64Image = AllegatiUtils.savePicture(bm, imageName);
                    immagine.setContenuto(base64Image);
                    //elimino l'immagine precedente
                    ShoplistDatabaseManager.getInstance(AggiungiProdottoActivity.this).deleteImmagineById(idImmagine);

                    //salvo l'immagine su DataBase
                    ShoplistDatabaseManager.getInstance(AggiungiProdottoActivity.this).addImmagine(immagine);
                    //setto a true il salvataggio dell'immagine
                    isImmagineSalvata = true;

                    photoFile = null;
                    refreshImageView();
                    break;
            }
        }
    }

    private void finishActivity() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.attenzione))
                .setMessage(this.getString(R.string.conferma_back))
                .setCancelable(true)
                .setPositiveButton(this.getString(R.string.OK), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(IntentConstant.RISULTATO_AGGIUNTA_KO);
                        //se ho salvato un immagine la elimino per non intasare il DB
                        if (isImmagineSalvata) {
                            ShoplistDatabaseManager.getInstance(AggiungiProdottoActivity.this).deleteImmagineById(idImmagine);
                        }
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

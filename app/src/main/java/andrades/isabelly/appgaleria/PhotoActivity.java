package andrades.isabelly.appgaleria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import java.io.File;

public class PhotoActivity extends AppCompatActivity {

    String photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        // encontra a barra de atividades
        Toolbar toolbar = findViewById(R.id.tbPhoto);
        // encontra a view da imagem
        ImageView imPhoto = findViewById(R.id.imPhoto);

        //define a action bar encontrada como padrao
        setSupportActionBar(toolbar);

        // "pega" a action bar padrão da activity
        ActionBar actionBar = getSupportActionBar();
        // coloca o botão de voltar na action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        // recebe a intent enviada pelo startPhotoActivity
        Intent i = getIntent();
        // recebe o endereço da imagem que estava na intent
        photoPath = i.getStringExtra("photo_path");

        // carrega o bitmap da imagem
        Bitmap bitmap = Util.getBitmap(photoPath);
        // define a imagem
        imPhoto.setImageBitmap(bitmap);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // cria o menu inflater
        MenuInflater inflater = getMenuInflater();
        // cria as opções de menu e adiciona ao menu da activity
        inflater.inflate(R.menu.photo_activity_tb, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // caso o compartilhar tenha sido selecionada, a câmera será aberta
            case R.id.opShare:
                sharePhoto();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void sharePhoto() {
        // gera um uri para a foto
        Uri photoUri = FileProvider.getUriForFile(PhotoActivity.this,
                "andrades.isabelly.appgaleria.fileprovider", new File(photoPath));
        // cria um intent para enviar a imagem
        Intent i = new Intent(Intent.ACTION_SEND);
        // adiciona o arquivo que o usuário quer compartilhar
        i.putExtra(Intent.EXTRA_STREAM, photoUri);
        // define o tipo do arquivo que vai ser compartilhado
        i.setType("image/jpeg");
        // executa a intent
        startActivity(i);
    }
}
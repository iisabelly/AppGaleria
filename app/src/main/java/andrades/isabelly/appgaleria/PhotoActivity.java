package andrades.isabelly.appgaleria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

public class PhotoActivity extends AppCompatActivity {

    String photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        // encontra a barra de atividades
        Toolbar toolbar = findViewById(R.id.tbPhoto);
        //define a action bar encontrada como padrao
        setSupportActionBar(toolbar);

        // "pega" a action bar padrão da activity
        ActionBar actionBar = getSupportActionBar();
        // coloca o botão de voltar na action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        photoPath = i.getStringExtra("photo_path");

        Bitmap bitmap = Utils.getBitmap(photoPath);
        ImageView imPhoto = findViewById(R.id.imPhoto);
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
}
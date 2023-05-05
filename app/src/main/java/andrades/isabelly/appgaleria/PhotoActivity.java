package andrades.isabelly.appgaleria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class PhotoActivity extends AppCompatActivity {

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
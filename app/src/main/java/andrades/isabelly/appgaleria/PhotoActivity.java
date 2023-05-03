package andrades.isabelly.appgaleria;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toolbar;

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
        inflater.inflate(R.menu.main_activity_tb, menu);
        return true;
    }
}
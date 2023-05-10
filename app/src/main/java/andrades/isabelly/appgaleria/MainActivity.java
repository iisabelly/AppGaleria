package andrades.isabelly.appgaleria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<String> photos = new ArrayList<>();
    MainAdapter mainAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // acessa o diretório de fotos
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = dir.listFiles();

        // adiciona as fotos ao array de fotos
        for(int i = 0; i < files.length; i++) {
            photos.add(files[i].getAbsolutePath());
        }

        // cria o main adapter
        mainAdapter = new MainAdapter(MainActivity.this, photos);

        // encontra o recycler view na activity
        RecyclerView rvGallery = findViewById(R.id.rvGallery);
        // define o adapter da recycler view como o main adapter
        rvGallery.setAdapter(mainAdapter);

        // pega a dimensão dos itens
        float w = getResources().getDimension(R.dimen.itemWidth);
        // calcula o número de itens que cabem na tela
        int numberOfColumns = Utils.calculateNoOfColumns(MainActivity.this, w);
        // cria a grid para colocar os itens
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, w);
        // define o layout da recycler view como a grid
        rvGallery.setLayoutManager(gridLayoutManager);

        // encontra a barra de atividades
        Toolbar toolbar = findViewById(R.id.tbMain);
        //define a action bar encontrada como padrao
        setSupportActionBar(toolbar);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // caso a camera tenha sido selecionada, a câmera será aberta
            case R.id.opCamera:
                dispatchTakePictureIntent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startPhotoActivity(String photoPath) {
        Intent i = new Intent(MainActivity.this, PhotoActivity.class);
        i.putExtra("photo_path", photoPath);
        startActivity(i);
    }

}
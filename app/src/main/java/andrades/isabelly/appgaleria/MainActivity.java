package andrades.isabelly.appgaleria;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    static int RESULT_TAKE_PICTURE = 1;
    String currentPhotoPath;
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
        int numberOfColumns = Util.calculateNoOfColumns(MainActivity.this, w);
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
        // cria intent pra salvar informações da activity
        Intent i = new Intent(MainActivity.this, PhotoActivity.class);
        // adiciona que essa intent deve abrir as "informações" na photoActivity
        i.putExtra("photo_path", photoPath);
        // "abre" a activity
        startActivity(i);
    }

    // dispara o aplicativo da câmera
    private void dispatchTakePictureIntent() {
        // cria a variável para o arquivo que vai salvar a imagem
        File f = null;

        // tenta criar o arquivo
        try {
            f = createImageFile();
        // caso não consiga criar o arquivo, o usuário vai ser avisado
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Não foi possível criar o arquivo",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // salva o arquivo
        currentPhotoPath = f.getAbsolutePath();

        // caso o arquivo tenha sido realmente criado
        if (f != null) {
            // gera um endereço URI para a foto
            Uri fUri = FileProvider.getUriForFile(MainActivity.this, "andrades.isabelly.appgaleria.fileprovider", f);

            // cria uma intent para abrir a câmera
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // passa o endereço URI para a intent
            i.putExtra(MediaStore.EXTRA_OUTPUT, fUri);

            // abre a câmera esperando o retorno de uma foto
            startActivityForResult(i, RESULT_TAKE_PICTURE);
        }
    }

    // cria o arquivo da imagem
    private File createImageFile() throws IOException {
        // guarda a data e a hora que a imagem foi tirada
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        // cria o nome do arquivo
        String imageFileName = "JPEG_" + timeStamp;

        // encontra o diretório para armazenar o arquivo
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // cria o arquivo
        File f = File.createTempFile(imageFileName, ".jpg", storageDir);

        // retorna o arquivo
        return f;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // caso a foto tenha sido tirada
        if(requestCode == RESULT_TAKE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                // adiciona a foto a lista de fotos
                photos.add(currentPhotoPath);
                // avisa que uma nova foto foi adicionada a lista
                mainAdapter.notifyItemInserted(photos.size()-1);
            } else {
                // caso a foto não tenha sido tirada, o arquivo criado pra guardar ela é excluido
                File f = new File(currentPhotoPath);
                f.delete();
            }
            }
        }
    }
}
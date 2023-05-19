package andrades.isabelly.appgaleria;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
    static int RESULT_REQUEST_PERMISSION = 2;
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

        // cria a lista de permissões
        List<String> permissions = new ArrayList<>();
        // adiciona a permissão para a camera na lista
        permissions.add(Manifest.permission.CAMERA);
        // verifica se as permissões que estão na lista não foram aceitas
        checkForPermissions(permissions);


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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, numberOfColumns);
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

    private void checkForPermissions(List<String> permissions) {
        List<String> permissionsNotGranted = new ArrayList<>();

        // roda a lista de permissões
        for(String permission : permissions) {
            // se a permissão não foi aceita, ela é adicionada a lista de permissões não aceitas
            if (!hasPermission(permission)) {
                permissionsNotGranted.add(permission);
            }
        }

        // as permissões não aceitas pelo usuário são solicitadas a ele novamente
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(permissionsNotGranted.size() > 0) {
                requestPermissions((permissionsNotGranted.toArray(new String[permissionsNotGranted.size()])),
                        RESULT_REQUEST_PERMISSION);
            }
        }
    }

    private boolean hasPermission(String permission) {
        // verifica se a permissão já foi aceita
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(MainActivity.this, permission) ==
                    PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        final List<String> permissionsRejected = new ArrayList<>();
        // verifica se as permissões solicitadas foram aceitas
        if(requestCode == RESULT_REQUEST_PERMISSION) {
            for(String permission : permissions) {
                // se não foram aceitas elas são adicionadas a lista de permissões rejeitadas
                if (!hasPermission(permission)) {
                    permissionsRejected.add(permission);
                }
            }
        }

        // condição para verificar se a lista de permissões rejeitadas possuir algum item
        if(permissionsRejected.size() > 0) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))){
                    // se a permissão for necessária para o uso do aplicativo um alerta aparece
                    // para o usuário aceitar a permissão
                    new AlertDialog.Builder(MainActivity.this).setMessage("Para usar essa app" +
                            "é preciso conceder essas permissões").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(permissionsRejected.toArray(new String[
                                    permissionsRejected.size()]), RESULT_REQUEST_PERMISSION);
                        }
                    }).create().show();
                }
            }
        }
    }
}

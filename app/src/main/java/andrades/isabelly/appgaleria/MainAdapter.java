package andrades.isabelly.appgaleria;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter{

    MainActivity mainActivity;
    List<String> photos;

    public MainAdapter(MainActivity mainActivity, List<String> photos) {
        this.mainActivity = mainActivity;
        this.photos = photos;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        // encontra o item para colocar a imagens
        ImageView imPhoto = holder.itemView.findViewById(R.id.imItem);

        // pega o tamanho das dimensões para as imagens na lista
        int w = (int) mainActivity.getResources().getDimension(R.dimen.itemWidth);
        int h = (int) mainActivity.getResources().getDimension(R.dimen.itemHeight);

        // coloca a imagem em um bitmap com o tamanho definido anteriormente
        Bitmap bitmap = Utils.getBitmap(photos.get(position), w,h);

        // define o imPhoto como o bitmap
        imPhoto.setImageBitmap(bitmap);

        // ação feita quando o usuário clica na imagem
        imPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // quando o usuário clicar, a imagem vai ser carregada em uma activity que irá mostrar
                // essa imagem em um tamanho maior
                mainActivity.startPhotoActivity(photos.get(position));
            }
        });
    }
}

package com.example.myapplication.Json;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class MuseumAdapter extends RecyclerView.Adapter<MuseumAdapter.ViewHolder>{

    Museums museum;
    Context context;

    @NonNull
    @Override
    public MuseumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.museums_recycler_view, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull MuseumAdapter.ViewHolder holder, int position) {

        TextView textView=holder.nameMuseumTextView;
        textView.setText(museum.getData().get(position).getData().getGeneral().getName());

        TextView textView1=holder.descriptionMuseumTextView;
        textView1.setText(html2text(museum.getData().get(position).getData().getGeneral().getDescription()));

        TextView textView2=holder.placeMuseumTextView;

        if(museum.getData().get(position).getData().getGeneral().getPlaces()!=null){
            textView2.setText((museum.getData().get(position).getData().getGeneral().getPlaces().get(0).getAddress().fullAddress));
        }
        else{

            textView2.setText((museum.getData().get(position).getData().getGeneral().getAddress().fullAddress));
        }


        ImageView imageView=holder.photoMuseumImageView;

        imageView.setClipToOutline(true);

        OkHttpClient picassoClient = getUnsafeOkHttpClient();
        Picasso picasso = new Picasso.Builder(holder.itemView.getContext()).downloader(new OkHttp3Downloader(picassoClient)).build();
        picasso.setLoggingEnabled(true);
        picasso.load(museum.getData().get(position).getData().getGeneral().getImage().url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_background)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                        System.out.println("load message success");
                    }

                    @Override
                    public void onError(Exception e) {

                        System.out.println("Error load image:"+e.getMessage());
                        System.out.println("Error load image (image resource):"+museum.getData().get(position).getData().getGeneral().getImage().url);
                    }
                });

        // обработка нажатия на элемент itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClickMuseum(museum.getData().get(position).getData().getGeneral(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return museum.getData().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameMuseumTextView;
        public ImageView photoMuseumImageView;
        public TextView placeMuseumTextView;
        public TextView descriptionMuseumTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameMuseumTextView=itemView.findViewById(R.id.txt_museum_tv_name);
            photoMuseumImageView=itemView.findViewById(R.id.imageView_museumPhoto_rv);
            placeMuseumTextView=itemView.findViewById(R.id.txt_museum_rv_place);
            descriptionMuseumTextView=itemView.findViewById(R.id.txt_museum_rv_description);
        }
    }

    // конструктор класса
    public MuseumAdapter(Context context, Museums museum, ItemClickListener clickListener) {
        this.context=context;
        this.museum = museum;
        this.clickListener=clickListener;

    }

    private ItemClickListener clickListener;

    public interface ItemClickListener{
        void onItemClickMuseum(General generalMuseumInfo, Integer position);
    }

    // изменение данных адаптера
    public void setFilterList(Museums museum){
        this.museum=museum;
        notifyDataSetChanged();
    }

    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String html2text(String html) {
        html=html.replaceAll("<[^>]*>", "");
        // \s+ - это регулярное выражение. \s соответствует пробелу, табуляции, новой строке,
        // возврату каретки, подаче формы или вертикальной табуляции, а + означает "один или несколько из них".
        // Таким образом, приведенный выше код свернет все "пробельные подстроки" длиной более одного символа с одним символом пробела.
        html=html.replaceAll("\\s+", " ");
        html=html.replaceAll("&ndsp;", " ");
        html=html.replaceAll("&nbsp;", " ");
        return html;
    }
}

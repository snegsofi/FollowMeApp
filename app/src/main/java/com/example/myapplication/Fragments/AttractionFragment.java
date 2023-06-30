package com.example.myapplication.Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.Adapters.ViewPagerAdapter;
import com.example.myapplication.Json.Gallery;
import com.example.myapplication.Json.General;
import com.example.myapplication.Json.Museums;
import com.example.myapplication.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class AttractionFragment extends Fragment {

    TextView nameAttractionTextView;
    TextView descriptionAttractionTextView;
    ImageButton backAttractionButton;
    TextView placeAttractionTextView;
    TextView isFreeTextView;
    TextView priceTextView;
    TextView datesTextView;
    ImageView websiteImageView;
    ImageView priceImageView;
    ImageView datesImageView;
    ViewPager viewPager;
    TextView websiteTxv;

    private static final String ARG_PARAM1="attractionInfo";
    private static final String ARG_PARAM2="citySearchItem";
    private static final String ARG_PARAM3="chipSelectedItem";
    private static final String ARG_PARAM4="rvPosition";
    private static final String ARG_PARAM5="name";
    private static final String ARG_PARAM6="description";
    private static final String ARG_PARAM7="address";
    private static final String ARG_PARAM8="website";
    private static final String ARG_PARAM9="price";
    private static final String ARG_PARAM10="dates";
    private static final String ARG_PARAM11="gallery";

    String name="";
    String description="";
    String address="";
    String website="";
    String price="";
    String dates="";
    ArrayList<String> gallery=new ArrayList<>();
    String citySearchItem;
    Integer chipSelectedItem;
    Integer rvPosition;


    // метод для загрузки фрагмента из другой активности
    public static AttractionFragment newInstance(General generalMuseumInfo,String citySearchItem, Integer chipSelectedItem, Integer position)
    {
        // передача данных о выбранном месте (парке, событии, музее)
        Bundle args = new Bundle();
        //args.putParcelable(ARG_PARAM1, generalMuseumInfo);
        //args.putSerializable(ARG_PARAM1, generalMuseumInfo);
        args.putString(ARG_PARAM2,citySearchItem);
        args.putInt(ARG_PARAM3,chipSelectedItem);
        args.putInt(ARG_PARAM4,position);

        AttractionFragment fragment = new AttractionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static AttractionFragment newInstance(String name,String description, String address, String website,
         String price, String dates, ArrayList<String> gallery, String citySearchItem, Integer chipSelectedItem, Integer position)
    {
        // передача данных о выбранном месте (парке, событии, музее)
        Bundle args = new Bundle();
        args.putString(ARG_PARAM2,citySearchItem);
        args.putInt(ARG_PARAM3,chipSelectedItem);
        args.putInt(ARG_PARAM4,position);
        args.putString(ARG_PARAM5,name);
        args.putString(ARG_PARAM6,description);
        args.putString(ARG_PARAM7,address);
        args.putString(ARG_PARAM8,website);
        args.putString(ARG_PARAM9,price);
        args.putString(ARG_PARAM10,dates);
        args.putStringArrayList(ARG_PARAM11,gallery);
        AttractionFragment fragment = new AttractionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // инициальзация переменных
    public void initialization(View view){
        nameAttractionTextView=view.findViewById(R.id.attraction_name);
        descriptionAttractionTextView=view.findViewById(R.id.attraction_description);
        backAttractionButton=view.findViewById(R.id.back_attraction_imageButton);
        placeAttractionTextView=view.findViewById(R.id.attraction_place);
        isFreeTextView=new TextView(getContext());
        priceTextView=new TextView(getContext());
        datesTextView=new TextView(getContext());
        priceImageView=new ImageView(getContext());
        datesImageView=new ImageView(getContext());
        priceImageView.setBackgroundResource(R.drawable.ic_baseline_currency_ruble_24);
        datesImageView.setBackgroundResource(R.drawable.ic_baseline_calendar_month_24);
        viewPager=view.findViewById(R.id.attraction_view_pager);
        websiteTxv=view.findViewById(R.id.attraction_web);
        websiteImageView=view.findViewById(R.id.attraction_iv_web);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // получение информации о выбранном музее
        Bundle bundle = getArguments();
        if(bundle!=null){
            //generalMuseumInfo= (General) bundle.getSerializable(ARG_PARAM1);
            citySearchItem=bundle.getString(ARG_PARAM2);
            chipSelectedItem=bundle.getInt(ARG_PARAM3);
            rvPosition=bundle.getInt(ARG_PARAM4);
            name=bundle.getString(ARG_PARAM5);
            description=bundle.getString(ARG_PARAM6);
            address=bundle.getString(ARG_PARAM7);
            website=bundle.getString(ARG_PARAM8);
            price=bundle.getString(ARG_PARAM9);
            dates=bundle.getString(ARG_PARAM10);
            gallery=bundle.getStringArrayList(ARG_PARAM11);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.attraction_activity,container,false);

        initialization(view);

        nameAttractionTextView.setText(name);
        descriptionAttractionTextView.setText(html2text(description));
        if(!address.isEmpty()){
            placeAttractionTextView.setText(address);
        }
        if(!website.isEmpty()){
            websiteTxv.setVisibility(View.VISIBLE);
            websiteImageView.setVisibility(View.VISIBLE);
            websiteTxv.setText(website);
            //addViewToLayout(websiteTextView,websiteImageView, view,R.id.attraction_ll_website,website);
        }
        if(!price.isEmpty()){
            addViewToLayout(priceTextView,priceImageView, view,R.id.attraction_ll_price,price);
        }
        if(!dates.isEmpty()){
            addViewToLayout(datesTextView,datesImageView,view,R.id.attraction_ll_dates,dates);
        }
        if(!gallery.isEmpty()){
            // добавление изображений в ViewPagerAdapter для возможности пролистывания фотографий из списка
            ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(view.getContext(),gallery, "AttractionFragment");
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.setCurrentItem(0);
            viewPager.setVisibility(View.VISIBLE);
            viewPagerAdapter.notifyDataSetChanged();
        }

        // кнопка возврата к списку туров
        backAttractionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(HomeFragment.newInstance(citySearchItem,chipSelectedItem, rvPosition));
            }
        });

        return view;
    }


    // преобразование html текста в string
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

    // загрузка другого фрагмента
    private void loadFragment(Fragment fragment){
        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    // метод добавления компонентов на активность
    public void addViewToLayout(TextView textView,ImageView imageView, View view, int id, String text){
        LinearLayout newView = (LinearLayout) view.findViewById(id);
        LinearLayout a = new LinearLayout(getContext());

        // атрибуты textView
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(Html.fromHtml(text));
        textView.setTextSize(18);
        textView.setTextColor(Color.BLACK);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.my_font);
        textView.setTypeface(typeface);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(13, 0, 5, 0);
        params.gravity=Gravity.CENTER;
        textView.setLayoutParams(params);
        imageView.setLayoutParams(params);
        a.addView(imageView);
        a.addView(textView);
        newView.addView(a);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}

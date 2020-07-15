package com.example.in_class_05;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String[] keywords;
    EditText e1;
    String url;
    String url1;
    String[] image_url;
    int count;
    ProgressBar image_progress;
    TextView Loading;
    Button go;
    ImageView prev_button;
    ImageView next_button;
    ImageView main_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);
        e1 = (EditText)findViewById(R.id.editText);
        url = "http://dev.theappsdr.com/apis/photos/keywords.php";
        url1 = "http://dev.theappsdr.com/apis/photos/index.php";
        image_progress = (ProgressBar)findViewById(R.id.progressBar);
        Loading = (TextView)findViewById(R.id.textView);
        image_progress.setVisibility(View.INVISIBLE);
        Loading.setVisibility(View.INVISIBLE);
        go = findViewById(R.id.Go_button);
        prev_button = findViewById(R.id.imageView2);
        next_button = findViewById(R.id.imageView3);
        main_image = findViewById(R.id.imageView);
        prev_button.setClickable(false);
        next_button.setClickable(false);
        go.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if(isConnected()){
                        new keywordAsync(MainActivity.this).execute(url);}

                    }
                });


            next_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    next_button.setClickable(true);
                    if(isConnected()){

                    //Toast.makeText(MainActivity.this, "Clicked next", Toast.LENGTH_SHORT).show();

                    count = count + 1;
                    get_image(count);
                    }


                }
            });
            prev_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    prev_button.setClickable(true);
                    if(isConnected()){
                    Toast.makeText(MainActivity.this, "Clicked prev", Toast.LENGTH_SHORT).show();
                    count = count - 1;
                    get_image(count);

                    Log.d("pos", "prev Image");
                    }
                }
            });
        }





    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public void handlekeywords(String[] s1){
        this.keywords = s1;
        Log.d("keyword",keywords[0]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Choose a Keyword")
                .setSingleChoiceItems(keywords, keywords.length, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ListView lw = ((AlertDialog) dialogInterface).getListView();
                        Object checkedItem = lw.getAdapter().getItem(i);
                      if(isConnected()) {
                          e1.setText(checkedItem.toString());
                          //Toast.makeText(MainActivity.this, e1.getText().toString(), Toast.LENGTH_SHORT).show();
                          get_image_url();
                      }

                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void  get_image_url(){
        String s = e1.getText().toString();
        new imageurlAsync(MainActivity.this).execute(s);

    }
    public void  get_image_url_data(String[] image_url){

        this.image_url = image_url;
        if(image_url.length==1) {
            if (image_url[0].length() == 0) {
                Toast.makeText(MainActivity.this, "No images found", Toast.LENGTH_SHORT).show();
            } else {
                prev_button.setClickable(false);
                next_button.setClickable(false);
                count = 0;
                get_image(count);
            }
        }
        else{
            count = 0;
            get_image(count);
        }

        }

    public void  get_image(int image_url_no) {
        if (image_url_no == (image_url.length) - 1) {
            //Toast.makeText(MainActivity.this, "Lastpos", Toast.LENGTH_SHORT).show();
            count=-1;
            System.out.println(image_url.length);
            //next_button.setClickable(false);
            new photo(MainActivity.this).execute(image_url[image_url_no]);
            main_image.setVisibility(View.INVISIBLE);
            image_progress.setVisibility(View.VISIBLE);
            Loading.setVisibility(View.VISIBLE);


        } else if (image_url_no == 0) {
            //Log.d("pos", "first");
            Toast.makeText(MainActivity.this, "Firstpos", Toast.LENGTH_SHORT).show();
            prev_button.setClickable(false);
            new photo(MainActivity.this).execute(image_url[image_url_no]);
            main_image.setVisibility(View.INVISIBLE);
            image_progress.setVisibility(View.VISIBLE);
            Loading.setVisibility(View.VISIBLE);

        }
        else if (image_url_no > 0 && image_url_no < image_url.length-1) {
            prev_button.setClickable(true);
            next_button.setClickable(true);
            new photo(MainActivity.this).execute(image_url[image_url_no]);
            main_image.setVisibility(View.INVISIBLE);
            image_progress.setVisibility(View.VISIBLE);
            Loading.setVisibility(View.VISIBLE);
        }
    }
    public void display_image(Bitmap bitmap){

        ImageView imageview = (ImageView)findViewById(R.id.imageView);
        e1.setVisibility(View.VISIBLE);
        go.setVisibility(View.VISIBLE);
        main_image.setVisibility(View.VISIBLE);
        prev_button.setVisibility(View.VISIBLE);
        next_button.setVisibility(View.VISIBLE);
        image_progress.setVisibility(View.INVISIBLE);
        Loading.setVisibility(View.INVISIBLE);
        imageview.setImageBitmap(bitmap);

    }
}

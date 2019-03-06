package com.worldtechq.frebse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ImageOpen extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_image_open);
        imageView=findViewById (R.id.iv1);
        textView=findViewById (R.id.t1);
        Intent intent=getIntent ();
        /*here we get the data from another activity*/
        textView.setText (intent.getStringExtra ("imagename"));
        url=intent.getStringExtra ("imageid");
         Glide.with (this).load (url).into (imageView);



    }
}

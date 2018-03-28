package com.doing.mavencontainermaster;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView textView = new TextView(this);
        textView.setText("这个app专门用来上传aar/jar到maven私服");
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        textView.setLayoutParams(layoutParams);

        addContentView(textView,layoutParams);




    }
}

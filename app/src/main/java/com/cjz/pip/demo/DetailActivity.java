package com.cjz.pip.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.cjz.pip.R;
import com.cjz.piphelper.PIPHelper;

/**
 * Created by cjz on 2018/6/27.
 */
public class DetailActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.imageView);

        PIPHelper.get()
//                .reMeetDismissFloatOne()
                .reMeetReplaceNewByFloatOne()
//                .reMeetDoNothing()
//                .floatViewDisableDrag()
                .onBackKeyUpAutoFloat()
                .initTarget(imageView);

    }

    public void grab(View view) {

        PIPHelper.get().floatView();

    }


    public void giveBack(View view) {
    }
}

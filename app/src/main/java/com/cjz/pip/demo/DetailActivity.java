package com.cjz.pip.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.cjz.pip.R;
import com.cjz.pip.demo.player.PlayerView;
import com.cjz.piphelper.PIPHelper;

/**
 * Created by cjz on 2018/6/27.
 */
public class DetailActivity extends AppCompatActivity {

    ImageView imageView;
    PlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.imageView);

        playerView = findViewById(R.id.player);

        PIPHelper.get()
//                .reMeetDismissFloatOne()
                .reMeetReplaceNewByFloatOne()
//                .reMeetDoNothing()
                .floatViewDisableDrag()
                .onBackKeyUpAutoFloat()
                .onEnterPIPListener(new PIPHelper.EnterPIPListener() {
                    @Override
                    public void onEnter(View floatRootView) {

                    }
                })
                .initTarget(playerView);

    }

    public void grab(View view) {

        PIPHelper.get().floatView();

    }


    public void play(View view) {
        playerView.play();
    }
}

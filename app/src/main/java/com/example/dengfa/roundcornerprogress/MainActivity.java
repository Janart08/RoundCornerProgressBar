package com.example.dengfa.roundcornerprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RoundCornerProgress mRoundCornerPb;
    private float               progress;
    private float               max;
    private float               secProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRoundCornerPb = (RoundCornerProgress) findViewById(R.id.rp);
        progress = mRoundCornerPb.getProgress();
        secProgress = mRoundCornerPb.getSecondaryProgress();
        max = mRoundCornerPb.getMax();
        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_decrement).setOnClickListener(this);
        findViewById(R.id.btn_add_sec).setOnClickListener(this);
        findViewById(R.id.btn_decrement_sec).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                mRoundCornerPb.setProgress(progress < max ? ++progress : max);
                break;
            case R.id.btn_decrement:
                mRoundCornerPb.setProgress(progress > 0 ? --progress : 0);
                break;
            case R.id.btn_add_sec:
                mRoundCornerPb.setSecondaryProgress(secProgress < max ? ++secProgress : max);
                break;
            case R.id.btn_decrement_sec:
                mRoundCornerPb.setSecondaryProgress(secProgress > 0 ? --secProgress : 0);
                break;
        }
    }
}

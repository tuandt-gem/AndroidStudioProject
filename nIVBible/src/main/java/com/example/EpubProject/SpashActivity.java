package com.example.EpubProject;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.EpubProject.parser.ParserAsync;
import com.nivbible.R;

public class SpashActivity extends Activity implements
        ParserAsync.IOnComplete {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                start();
                // Un-co
//                new ParserAsync(SpashActivity.this, SpashActivity.this).execute();
            }
        }, 2000);
    }

    private void start() {
        startActivity(new Intent(SpashActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onComplete() {
        start();
    }
}

package com.example.sanjay.catchtheball;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

public class SplashScreen extends AppCompatActivity {

    ProgressBar pgr;
    int progress = 0;
    Handler h = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        pgr = (ProgressBar) findViewById(R.id.progressBar1);
        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i= 0;i<4;i++){
                    progress = progress + 25;
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            pgr.setProgress(progress);
                            if(progress ==  pgr.getMax()){
                                Intent intent = new Intent(getApplicationContext(),Main.class);
                                startActivity(intent);
                            }
                        }
                    });
                    try {
                        Thread.sleep(2000);
                    }catch (InterruptedException e){


                    }
                }
            }
        }).start();
    }
}

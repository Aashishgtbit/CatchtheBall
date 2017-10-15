package com.example.sanjay.catchtheball;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Main extends AppCompatActivity {

    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView box;
    private ImageView orange;
    private ImageView pink;
    private ImageView black;
    private ImageView candy;
    //button
    private Button pauseBtn;

    //Size
    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;

    //position
    private int boxY;
    private int orangeX;
    private int orangeY;
    private int pinkX;
    private int pinkY;
    private int blackX;
    private int blackY;
    private int candyX;
    private int candyY;

    //Speed
    private  int boxSpeed;
    private int orangeSpeed;
    private int pinkSpeed;
    private int blackSpeed;
    private int candySpeed;


    //score
    private int score = 0;

    //Initialise class
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private SoundPlayer sound;

    //status check
    private boolean action_flg = false;
    private boolean start_flg = false;
    public boolean pause_flg = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sound = new SoundPlayer(this);
        pausePushed();
        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);
        box = (ImageView) findViewById(R.id.box);
        orange = (ImageView) findViewById(R.id.orange);
        pink = (ImageView) findViewById(R.id.pink);
        black = (ImageView) findViewById(R.id.black);
        candy = (ImageView) findViewById(R.id.candy);


        //Get screen size
        WindowManager wM= getWindowManager();
        Display disp = wM.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        /*
        Now Nexus 4 width = 764 and height = 1184
        speed box : 20 orange: 12 pink 20 black: 16
         */
        boxSpeed = Math.round(screenHeight/60f); // 1184/60 = 19.733....=>20
        orangeSpeed = Math.round(screenWidth/60f); //764/60 = 12.8.... => 13
        pinkSpeed = Math.round(screenWidth/36f);    //764/36 = 21.333 ....=>21
        candySpeed = Math.round(screenWidth/40f);
        blackSpeed = Math.round(screenWidth/45f);   //764/46 = 17.86 ....=>18

       /* Log.v("SPEED_BOX", boxSpeed+"");
        Log.v("SPEED_ORANGE", orangeSpeed+"");
        Log.v("SPEED_PINK", pinkSpeed+"");
        Log.v("SPEED_BLACK", blackSpeed+"");
        */

        //Move to out of Screen.
        orange.setX(80);
        orange.setY(80);
        pink.setX(80);
        pink.setY(80);
        candy.setX(80);
        candy.setY(80);
        black.setX(80);
        black.setY(80);

        scoreLabel.setText("Score: 0");
    }

    public void changePos() {
        hitcheck();
        //Orange
        orangeX -=orangeSpeed;
        if(orangeX<0) {
            orangeX = screenWidth +20;
            orangeY = (int) Math.floor(Math.random() * (frameHeight - orange.getHeight()));
        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        //candy
        candyX -=candySpeed;
        if(candyX<0) {
            candyX = screenWidth +2500;
            candyY = (int) Math.floor(Math.random() * (frameHeight - candy.getHeight()));
        }
        candy.setX(candyX);
        candy.setY(candyY);

        //black
        blackX-=blackSpeed;
        if(blackX<0){
            blackX = screenWidth + 10;
            blackY = (int) Math.floor(Math.random() * (frameHeight- black.getHeight()));
        }
        black.setX(blackX);
        black.setY(blackY);

        //pink
        pinkX -= pinkSpeed;
        if(pinkX<0){
            pinkX = screenWidth + 5000;
            pinkY = (int) Math.floor(Math.random()* (frameHeight - pink.getHeight()));
        }
        pink.setX(pinkX);
        pink.setY(pinkY);

        //move box
        if(action_flg == true){
            //touching
            boxY -=boxSpeed;
        }else{
            //Releasing
            boxY +=20;
        }
        //Check box position.
        if(boxY <0 ) boxY = 0;

        if(boxY > frameHeight-boxSize) boxY = frameHeight - boxSize;
        else if(boxY > frameHeight)boxY= frameHeight;

        box.setY(boxY);
        scoreLabel.setText("Score:"+ score);
    }
    public void hitcheck(){
        //if the center of the ball is in the box, it counts as hit.

        //orange
        int orangeCenterX = orangeX +orange.getWidth()/2;
        int orangeCenterY = orangeY + orange.getHeight()/2;

        //0 <= orangeCenterX <= boxWidth
        // boxY <= orangeCenterY <= boxY + boxHeight
        if(0<= orangeCenterX && orangeCenterX <= boxSize && boxY <= orangeCenterY && orangeCenterY <= boxY + boxSize){
            score += 10;
            orangeX = -10;
            sound.playHitSound();

        }

        //candy

        int candyCenterX = candyX+candy.getWidth()/2;
        int candyCenterY = candyY + candy.getHeight()/2;


        if(0<= candyCenterX && candyCenterX <= boxSize && boxY <= candyCenterY && candyCenterY <= boxY + boxSize){
            score += 20;
            candyX = -10;
            sound.playHitSound();

        }

        //pink
        int pinkCenterX = pinkX + pink.getWidth()/2;
        int pinkCenterY = pinkY + pink.getHeight()/2;
        if(0<= pinkCenterX && pinkCenterX <= boxSize && boxY <= pinkCenterY && pinkCenterY <= boxY + boxSize){
         score += 30;
            pinkX = -10;
            sound.playHitSound();
        }

        //black
        int blackCenterX = blackX + black.getWidth()/2;
        int blackCenterY = blackY + black.getHeight()/2;
        if(0<= blackCenterX && blackCenterX <= boxSize && boxY <= blackCenterY && blackCenterY <= boxY + boxSize){

            //stop timer!!
            timer.cancel();
            timer= null;

            sound.playOverSound();

            //show result
            Intent intent = new Intent(getApplicationContext(),result.class);
            intent.putExtra("SCORE",score);
            startActivity(intent);


        }
    }

    public boolean onTouchEvent(MotionEvent me){
        if(start_flg == false){
            start_flg = true;
            //why getframe height and box height here?
            //because the UI has not been set on the screen in Oncreate()!!

            FrameLayout frame = (FrameLayout) findViewById(R.id.frame) ;
            frameHeight = frame.getHeight();

            boxY = (int) box.getY();
            //the box is a square so both the width and height are same.

            boxSize = box.getHeight();

            startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });

                }
            } , 0,20);

        }else {
            if ((me.getAction() == MotionEvent.ACTION_DOWN )){
                action_flg = true;
            }else if (me.getAction()== MotionEvent.ACTION_UP){
                action_flg = false;
            }

        }


        return true;
    }

    //Disable return button
    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }

        }

        return super.dispatchKeyEvent(event);
    }

    public void pausePushed(){
        final ImageButton Btn = (ImageButton) findViewById(R.id.pauseBtn);

        Btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //ImageButton Btn = (ImageButton) findViewById(R.id.pauseBtn);

                if(Btn.isPressed() && pause_flg == false)
                {
                    //ImageButton Btn = (ImageButton) findViewById(R.id.pauseBtn);
                    pause_flg= true;
                    //change image on button click.
                    Btn.setImageResource(R.drawable.play);
                    //stop the timer
                    timer.cancel();
                    timer = null;


                }else{
                    //ImageButton Btn = (ImageButton) findViewById(R.id.pauseBtn);
                   pause_flg = false;
                    //change image on button click.


                    Btn.setImageResource(R.drawable.pause);

                    //create and start the timer.
                    timer = new Timer();

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    changePos();
                                }
                            });

                        }
                    } , 0,20);

                }

                          }
        });



    }


}

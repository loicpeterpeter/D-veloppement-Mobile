package loic.brickbreaker;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Debug;
import android.os.Handler;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.util.LogPrinter;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Jeu extends AppCompatActivity {

    ConstraintLayout constrain;
    //Initialisation des variables

    //Les View
    private TextView scoreLabel;
    private TextView health;
    private TextView startLabel;
    private ImageView paddle;
    private ImageView balle;
    private FrameLayout framelayout;
    private FrameLayout frame_pause;
    private Button pauseButton;
    private Button son_label;
    private Button vibrate_label;
    private Button quit;

    //Son
    private Sound sound;

    //bool
    private boolean flag_start = false;
    private boolean pause = true;
    private boolean son = true;
    private boolean vibrate = true;
    private boolean game_over = false;
    private boolean victoire = false;
    private boolean restart;

    //vibration
    Vibrator v;

    //position
    private float coord_x=0;
    private float startx;
    private float starty;

    private int vie;
    private int score;

    private int tilt;

    private String pseudo;

    private List<brique> liste_brique;

    //size
    private int frameHeight;
    private int frameWidth;
    private int balleSize;
    private int paddle_size;
    private int haut;

    //brique
    private int brique_hauteur=6;
    private int brique_largeur=3;
    private int brique_nb = brique_hauteur * brique_largeur;

    //class balls, direction et position
    private class balls{
        private float x;
        private float y;
        private float dir_x=0;
        private float dir_y=10;
    }
    balls balls = new balls();

    //collision
    private class rectangle{
        private float x1=0;
        private float y1=0;
        private float x2=0;
        private float y2=0;
    }

    // class
    rectangle balle_col= new rectangle();
    rectangle paddle_col= new rectangle();


    //Boucle temps
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeu);
        sound = new Sound(this);

        constrain = (ConstraintLayout) findViewById(R.id.constrainLayout) ;
        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        health = (TextView) findViewById(R.id.health);
        startLabel = (TextView) findViewById(R.id.startLabel);
        paddle = (ImageView) findViewById(R.id.paddle);
        balle = (ImageView) findViewById(R.id.balle);
        framelayout = (FrameLayout) findViewById(R.id.frame);
        pauseButton = (Button) findViewById(R.id.pauseButton);
        frame_pause = (FrameLayout) findViewById(R.id.table) ;
        son_label = (Button) findViewById(R.id.son);
        vibrate_label = (Button) findViewById(R.id.vibrate);
        quit = (Button) findViewById(R.id.quit);


        frame_pause.setVisibility(View.INVISIBLE);
        score = getIntent().getIntExtra("SCORE",0);
        vie = getIntent().getIntExtra("VIE",3);
        pseudo = getIntent().getStringExtra("PSEUDO");
        scoreLabel.setText("score : "+score);
        health.setText("health : " + vie);
        pauseButton.setEnabled(false);
        v=(Vibrator) getSystemService(VIBRATOR_SERVICE);

        liste_brique = new ArrayList<>();


    }


    public boolean collision(rectangle a ,rectangle b){
        return (((b.x1-a.x2)*(b.x2-a.x1)<=0) && ((b.y1-a.y2)*(b.y2-a.y1)<=0));
    }


    public void boucle(){
        //rebond contre les murs
        if (balls.x<0 || balls.x>frameWidth-balleSize){
            balls.dir_x=-balls.dir_x;
            if (son) sound.play_hit_wall();
        }
        if (balls.y<haut){
            balls.dir_y=-balls.dir_y;
            balls.y = haut;
            if (son) sound.play_hit_wall();
        }

        //Pour empecher que la balle bug dans le mur
        if (balls.x<0){
            balls.x = 1;
        }
        if (balls.x>frameWidth-balleSize){
            balls.x = frameWidth-(balleSize+1);
        }

        //Si la pause est activé
        if (pause){
            balls.x+=balls.dir_x;
            balls.y+=balls.dir_y;

            balle.setX(balls.x);
            balle.setY(balls.y);
        }


        //hitbox
        balle_col.x1 = balls.x;
        balle_col.x2 = balle_col.x1 + balleSize;
        balle_col.y1 = balls.y;
        balle_col.y2 = balle_col.y1 + balleSize;

        paddle_col.x1 = paddle.getX();
        paddle_col.x2 = paddle_col.x1 + paddle_size;
        paddle_col.y1 = paddle.getY();
        paddle_col.y2 = paddle_col.y1 + 15;

        //Si la balle touche la raquette
        if (collision(balle_col,paddle_col)){
            balls.x-=balls.dir_x;
            balls.y=paddle_col.y1-(paddle.getHeight()+1);
            balls.dir_y=-balls.dir_y;

            if (balls.dir_x==0)balls.dir_x=10;

            if(vibrate) v.vibrate(60);

            if (son) sound.play_hit_paddle();
            float X = (balle_col.x1+balleSize/2)-(paddle_col.x1+paddle_size/2);
            balls.dir_x = X/2;
            tilt=3;
        }

        //Si la balle touche une brique
        for (int i=0; i<liste_brique.size();i++){
            if (collision(balle_col,liste_brique.get(i).r)){
                score+=1;
                liste_brique.get(i).image.setVisibility(View.INVISIBLE);
                liste_brique.remove(i);
                brique_nb-=1;
                if (son)sound.play_hit_brique();
                balls.x-=balls.dir_x;
                balls.dir_y=-balls.dir_y;
                if (tilt!=0) tilt = 3;
                if(vibrate) v.vibrate(200);
            }
        }

        //vibration écran
        if (tilt!=0){
            if (vibrate){
                switch (tilt){
                    case 3:
                        tilt-=1;
                        framelayout.setY(framelayout.getY()+10);
                        break;
                    case 2:
                        tilt-=1;
                        framelayout.setY(framelayout.getY()-20);
                        break;
                    case 1:
                        tilt-=1;
                        framelayout.setY(framelayout.getY()+10);
                        break;
                }
            }
        }
        //si la balle == bas
        if (balls.y>=frameHeight){
            vie-=1;
            balls.x=startx;
            balls.y=starty;
            balls.dir_x=0;
            balls.dir_y=0;
            health.setText("health : "+vie);
            if(vie<=0){
                game_over=true;
            }
            restart = true;
            startLabel.setVisibility(View.VISIBLE);
            paddle.setX((frameWidth/2)-(paddle_size/2));
            pauseButton.setEnabled(false);
        }

        //Quand il y a plus de brique
        if (brique_nb == 0) victoire = true;

        if (victoire){
            timer.cancel();
            timer = null;
            Intent intent = new Intent(getApplicationContext(),Jeu.class);
            intent.putExtra("SCORE",score);
            intent.putExtra("VIE",vie);
            intent.putExtra("PSEUDO",pseudo);
            startActivity(intent);
            this.finish();
        }

        if (game_over){
            timer.cancel();
            timer = null;
            Intent intent = new Intent(getApplicationContext(), Game_over.class);
            intent.putExtra("SCORE",score);
            intent.putExtra("PSEUDO",pseudo);
            startActivity(intent);
            this.finish();
        }
        scoreLabel.setText("score : " + score);
    }



    public void pause(View view){
        if (pause){
            pause = false;
            pauseButton.setText(R.string.continu);
            frame_pause.setVisibility(View.VISIBLE);
        }
        else{
            pause = true;
            pauseButton.setText(R.string.pause);
            frame_pause.setVisibility(View.INVISIBLE);
        }
    }

    public void Son(View view){
        if (son){
            son = false;
            son_label.setText("Son : OFF");
        }
        else{
            son = true;
            son_label.setText("Son : ON");
        }
    }


    public void vibrate(View view){
        if (vibrate){
            vibrate = false;
            vibrate_label.setText(R.string.vibrate_off);
        }
        else{
            vibrate = true;
            vibrate_label.setText(R.string.vibrate_on);
        }
    }

    public void quit(View view){
        timer.cancel();
        timer = null;
        Intent intent = new Intent(getApplicationContext(), start.class);
        startActivity(intent);
        this.finish();
    }

    public ImageView brick(int w, int h){
        ImageView image = new ImageView(this);
        constrain.addView(image,w,h);
        image.setImageResource(R.drawable.brique);
        image.setVisibility(View.VISIBLE);
        return image;
    }

    private class brique{
        private rectangle r = new rectangle();
        private ImageView image;
        private brique(float x, float y, int w, int h){
            image = brick(w,h);
            image.setX(x);
            image.setY(y);
            r.x1 = x;
            r.x2 = x+w;
            r.y1 = y;
            r.y2 = y+h;
        }
    }

    public void generate(int x, int y){
        for(int j=0; j<y;j++){
            for (int i=0; i<x;i++){
                brique b = new brique((i*(frameWidth)/x),(j*60)+haut,(frameWidth/x),30);
                liste_brique.add(b);

            }
        }
    }


    public boolean onTouchEvent(MotionEvent me){

        if(!flag_start){
            flag_start=true;
            startLabel.setVisibility(View.INVISIBLE);

            pauseButton.setEnabled(true);
            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();
            frameWidth = frame.getWidth();
            balleSize = balle.getHeight();

            //balls
            balls.x = balle.getX();
            balls.y = balle.getY();

            //position depart
            startx=balls.x;
            starty=balls.y;

            //on centre le paddle
            paddle_size = paddle.getWidth();
            paddle.setX((frameWidth/2)-(paddle_size/2));

            //collision
            balle_col.x1 = balls.x;
            balle_col.x2 = balle_col.x1 + balleSize;
            balle_col.y1 = balls.y;
            balle_col.y2 = balle_col.y1 + balleSize;

            paddle_col.x1 = paddle.getX();
            paddle_col.x2 = paddle_col.x1 + paddle_size;
            paddle_col.y1 = paddle.getY();
            paddle_col.y2 = paddle_col.y1;

            haut=(health.getHeight()*2)+10;


            generate(brique_hauteur,brique_largeur);
            //la boucle du temps
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            boucle();
                        }
                    });
                }
            },0,20);
        }
        else{
            if (me.getAction()==MotionEvent.ACTION_DOWN){
                if (restart){
                    restart = false;
                    balls.dir_y=10;
                    startLabel.setVisibility(View.INVISIBLE);
                    pauseButton.setEnabled(true);
                }
            }
        }

        //bouger le paddle
        if (me.getAction()==MotionEvent.ACTION_MOVE){
            if(pause){
                coord_x = me.getX();
                paddle.setX(coord_x-(paddle_size/2));
            }
        }
        return true;
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if (event.getAction() == KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}

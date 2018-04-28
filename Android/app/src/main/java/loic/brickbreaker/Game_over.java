package loic.brickbreaker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import java.time.Instant;

public class Game_over extends AppCompatActivity {

    private String pseudo;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        TextView scoreLabel = (TextView) findViewById(R.id.scoreLabel_over);
        TextView highLabel = (TextView) findViewById(R.id.high_label);

        int score = getIntent().getIntExtra("SCORE",0);
        pseudo = getIntent().getStringExtra("PSEUDO");

        int list_score[] = new int[3];
        String list_nom[] = new String[3];

        scoreLabel.setText(pseudo + " : " +score);

        SharedPreferences setting = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        int highscore = setting.getInt("HIGH_SCORE0",0);

        for (int i=0; i<3;i++){
            list_nom[i]=setting.getString("NOM"+i,"default");
        }

        for (int i=0; i<3;i++){
            list_score[i]=setting.getInt("HIGH_SCORE"+i,0);
        }



        if (score>list_score[0]){
            highLabel.setText("High score : "+score);
            highLabel.setTextColor(Color.RED);
            list_score[2]=list_score[1];
            list_score[1]=list_score[0];
            list_score[0]=score;

            list_nom[2]=list_nom[1];
            list_nom[1]=list_nom[0];
            list_nom[0]=pseudo;
        }
        else if(score>list_score[1]){
            list_score[2]=list_score[1];
            list_score[1]=score;

            list_nom[2]=list_nom[1];
            list_nom[1]=pseudo;
        }
        else if(score>list_score[2]){
            list_score[2]=score;

            list_nom[2]=pseudo;
        }
        else{
            highLabel.setText("High score : "+highscore);
        }

        for (int i=0; i<3;i++){
            Log.d("liste"+i,list_nom[i]);
            SharedPreferences.Editor editor = setting.edit();
            editor.putInt("HIGH_SCORE"+i,list_score[i]);
            editor.putString("NOM"+i,list_nom[i]);
            editor.commit();
        }
    }

    public void tryAgain(View view){
        Intent intent = new Intent(getApplicationContext(),Jeu.class) ;
        intent.putExtra("PSEUDO",pseudo);
        startActivity(intent);
        this.finish();
    }

    public void quit(View view) {
        Intent intent = new Intent(getApplicationContext(), start.class);
        startActivity(intent);
        this.finish();
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

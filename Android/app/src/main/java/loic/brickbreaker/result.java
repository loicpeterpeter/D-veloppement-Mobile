package loic.brickbreaker;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class result extends AppCompatActivity {

    private ListView liste;
    public List<Slot> scores ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        liste = (ListView) findViewById(R.id.liste_test);
        String list_score[] = new String[3];
        String[] nom = new String[3];

        SharedPreferences setting = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);

        for (int i=0; i<3;i++){
            list_score[i]=String.valueOf(setting.getInt("HIGH_SCORE"+i,0));
            Log.d("liste"+i,String.valueOf(list_score[i]));
        }

        for (int i=0; i<3;i++){
            nom[i]=setting.getString("NOM"+i,"Default");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.text_layout,list_score);

        scores = new ArrayList<Slot>();

        for(int i =0 ; i < 3;i++){
            scores.add(new Slot(nom[i],list_score[i]));
        }
        ListAdapter slot = new ListAdapter(this,scores);
        liste.setAdapter(slot);
    }

    public void Quit(View view){
        Intent intent = new Intent(getApplicationContext(), start.class);
        startActivity(intent);
        this.finish();
    }
}

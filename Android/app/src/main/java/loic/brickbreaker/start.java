package loic.brickbreaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class start extends AppCompatActivity {

    private String pseudo = "test";
    private EditText text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        text = (EditText) findViewById(R.id.pseudo);
    }

    public void startGame(View view){
        pseudo = text.getText().toString();
        if (!pseudo.equals( "Enter pseudo")){
            Intent intent = new Intent(getApplicationContext(),Jeu.class) ;
            intent.putExtra("PSEUDO",pseudo);
            startActivity(intent);
            this.finish();
        }
    }

    public void result(View view){
        startActivity(new Intent(getApplicationContext(),result.class));
        this.finish();
    }
}

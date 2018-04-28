package loic.brickbreaker;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by loic on 22/04/2018.
 */

public class Sound {
    private static SoundPool soundPool;
    private static int hit_brick;
    private static int hit_paddle;
    private static int hit_wall;
    private static int fond;

    public Sound(Context context){
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);

        hit_brick = soundPool.load(context,R.raw.brick,1);
        hit_paddle = soundPool.load(context,R.raw.boom,1);
        hit_wall = soundPool.load(context,R.raw.wall,1);
        fond = soundPool.load(context,R.raw.fond,1);
    }

    public void play_hit_brique(){
        soundPool.play(hit_brick,1.0f,1.0f,2,0,1.0f);
    }
    public void play_hit_paddle(){
        soundPool.play(hit_paddle,1.0f,1.0f,2,0,1.0f);
    }
    public void play_hit_wall(){
        soundPool.play(hit_wall,1.0f,1.0f,2,0,1.0f);
    }
}

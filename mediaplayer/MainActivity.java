package project.ajiet.mediaplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    TextView playerPosition,playerDuration;
    SeekBar seekBar;
    ImageView btnRew,btnPlay,btnPause,btnFF;
    MediaPlayer mediaPlayer;
    Handler handler=new Handler();
    Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerPosition=findViewById(R.id.player_position);
        playerDuration=findViewById(R.id.player_duration);
        seekBar=findViewById(R.id.seekbar);
        btnRew=findViewById(R.id.btn_rew);
        btnPlay=findViewById(R.id.btn_play);
        btnPause=findViewById(R.id.btn_pause);
        btnFF=findViewById(R.id.btn_ff);


        mediaPlayer=MediaPlayer.create(this,R.raw.music2);

        runnable=new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this,500);
            }
        };
        //Get duration of media player
        int duration = mediaPlayer.getDuration();
        //convert milliseconds to minute and seconds
        String sDuration=convertFormat(duration);
        //set duration on textview
        playerDuration.setText(sDuration);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide play button
                btnPlay.setVisibility(View.GONE);
                //show pause button
                btnPause.setVisibility(View.VISIBLE);
                //start mediaPlayer
                mediaPlayer.start();
                // set max on seek bar
                seekBar.setMax(mediaPlayer.getDuration());
                //start Handler
                handler.postDelayed(runnable,0);
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hide pause button
                btnPause.setVisibility(View.GONE);
                //Show play button
                btnPlay.setVisibility(View.VISIBLE);
                //Pause mediaPlayer
                mediaPlayer.pause();
                //stop Handler
                handler.removeCallbacks(runnable);
            }
        });
        btnFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get current position of mediaPlayer
                int currentPos=mediaPlayer.getCurrentPosition();
                //Get duration of MediaPlayer
                int duration=mediaPlayer.getDuration();
                //check condition
                if(mediaPlayer.isPlaying() && duration!=currentPos){
                    //when media is playing and duration not equal to currentPosition
                    //Fast forward 5secs
                    currentPos=currentPos + 5000;
                    // Set currentPostion on Textview
                    playerPosition.setText(convertFormat(currentPos));
                    //set progress on seekBar
                    mediaPlayer.seekTo(currentPos);
                }
            }
        });

        btnRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get current position of mediaPlayer
                int currentPos=mediaPlayer.getCurrentPosition();
                //check condition
                if(mediaPlayer.isPlaying() && currentPos > 5000){
                    //when media is playing and currentPosition is lesser than 5secs
                    //Rewind 5secs
                    currentPos=currentPos - 5000;
                    //get currentPostion on Textview
                    playerPosition.setText(convertFormat(currentPos));
                    //set progress on seekBar
                    mediaPlayer.seekTo(currentPos);
                }

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //check condition
                if (fromUser){
                    //when user drags
                    //set progress on seekBar
                    mediaPlayer.seekTo(progress);
                }
                //set current position TextView
                playerPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //Hide pause button
                btnPause.setVisibility(View.GONE);
                //Show play button
                btnPlay.setVisibility(View.VISIBLE);
                //set mediaPlayer to initial position
                mediaPlayer.seekTo(0);
            }
        });
    }

    private String convertFormat(int duration) {
        return String.format("%02d:%02d"
        , TimeUnit.MILLISECONDS.toMinutes(duration)
        ,TimeUnit.MILLISECONDS.toSeconds(duration)-
         TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }
}
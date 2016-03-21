package com.horopter.ffmpeg_video_android;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Horopter on 3/8/2016.
 */
public class RecordingActivity extends AppCompatActivity {


    Button b1;
    VideoView vv;
    TextView tv;
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int REQUEST_GET_VID =2;
    File filepath = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_main);
        Intent i = getIntent();
        b1 = (Button) findViewById(R.id.record);
        vv = (VideoView) findViewById(R.id.videoView);
        tv = (TextView)findViewById(R.id.status);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                }
            }
        });
        Long time=1L;
        while(new File("/storage/sdcard1/Videos/android_"+time+".mp4").exists()) {
            time++;
        }
        vv.requestFocus();
        vv.start();
        filepath=new File("/storage/sdcard1/Videos/android_"+time+".mp4");
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                Intent intent = new Intent(getApplicationContext(), compression.class);
                intent.putExtra("filepath", filepath.getAbsolutePath());
                startActivity(intent);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
        {
            switch(requestCode)
            {
                case REQUEST_VIDEO_CAPTURE:
                Uri videoUri = data.getData();
                vv.setVideoURI(videoUri);
                try
                {
                    AssetFileDescriptor videoAsset = getContentResolver().openAssetFileDescriptor(videoUri, "r");
                    FileInputStream fis = videoAsset.createInputStream();
                    File root=new File("/storage/sdcard1/Videos/");
                    if (!root.exists()) {
                        Toast.makeText(this, "No directory", Toast.LENGTH_LONG).show();
                        root.mkdirs();
                    }
                    File file;
                    Long time = 1L;
                    while(new File(root,"android_"+time+".mp4").exists())
                        time++;
                    file=new File(root,"android_"+time+".mp4" );
                    FileOutputStream fos = new FileOutputStream(file);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = fis.read(buf)) > 0) {
                        fos.write(buf, 0, len);
                    }
                    fis.close();
                    fos.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                    break;
                case REQUEST_GET_VID:
                    String s = data.getStringExtra("retPath");
                    Uri vidUri = Uri.fromFile(new File(s));
                    tv.setText(data.getStringExtra("retPath"));
                    vv.setVideoURI(vidUri);
                    vv.requestFocus();
                    vv.start();
                    break;

            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

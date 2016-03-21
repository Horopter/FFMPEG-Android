package com.horopter.ffmpeg_video_android;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.netcompss.ffmpeg4android.GeneralUtils;
import com.netcompss.loader.LoadJNI;

import java.io.File;

/**
 * Created by Horopter on 3/8/2016.
 */
public class compression extends AppCompatActivity {

    private static String stringResult=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compression);
        Intent i = getIntent();
        String inpPath= i.getStringExtra("filepath");
        CompressVideo task = new CompressVideo();
        task.execute(inpPath);
    }
    private class CompressVideo extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... inpath) {
            LoadJNI vk = new LoadJNI();
            try {
                String inpPath = inpath[0];
                String workFolder=  getApplicationContext().getFilesDir().getAbsolutePath()+"/";
                String oup = "";
                String fileLoc = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"FFMPEGAndroid/Compressed/";
                if(new File(inpPath).exists()) {
                    oup = "_zResults_"+new File(inpPath).getName();
                    String commandStr = "ffmpeg -y -i " + inpPath + " -strict experimental -s 640x480 -r 25 -vcodec mpeg4 -b 150k -ab 48000 -ac 2 -ar 22050 "+ fileLoc + oup;
                    vk.run(GeneralUtils.utilConvertToComplex(commandStr), workFolder, getApplicationContext());
                    Toast.makeText(getApplicationContext(), "ffmpeg4android finished successfully", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplicationContext(),"File not found error",Toast.LENGTH_LONG).show();
            } catch (Throwable e) {
                Log.e("test", "vk run exception.", e);
            }
            return "Compression OK";
        }
        @Override
        protected void onPostExecute(String result) {
            setContentView(R.layout.complete);

        }
    }
}


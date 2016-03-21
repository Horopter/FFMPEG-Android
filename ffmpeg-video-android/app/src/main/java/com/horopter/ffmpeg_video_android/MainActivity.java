package com.horopter.ffmpeg_video_android;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_PICK_FILE = 1;
    private static final int REQUEST_GET_VID =2;
    private static final int REQUEST_RECORD_VID =3;


    private TextView filePath, compressedFilePath;
    private Button Browse,mRecord;
    private File selectedFile;
    private String s,t;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filePath = (TextView)findViewById(R.id.file_path);
        Browse = (Button)findViewById(R.id.browse);
        mRecord = (Button)findViewById(R.id.record);
        compressedFilePath = (TextView)findViewById(R.id.cmpathname);
        Browse.setOnClickListener(this);
        mRecord.setOnClickListener(this);
    }

    public void onClick(View v) {

        switch(v.getId()) {

            case R.id.browse:
                Intent intent = new Intent(this, FilePicker.class);
                startActivityForResult(intent, REQUEST_PICK_FILE);
                break;
            case R.id.record:
                Intent intent2 = new Intent(this, RecordingActivity.class);
                startActivity(intent2);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {

            switch(requestCode) {

                case REQUEST_PICK_FILE:
                    if(data.hasExtra(FilePicker.EXTRA_FILE_PATH)) {

                        selectedFile = new File
                                (data.getStringExtra(FilePicker.EXTRA_FILE_PATH));
                        filePath.setText(selectedFile.getPath());
                        t="VIDEO_" + new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date()) + ".mp4";
                        compressedFilePath.setText("Compressed file is :  _zResults_" + t);
                        s = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator
                                + "FFMPEGAndroid/"
                                + "Temp/";
                        copyFile(selectedFile.getAbsolutePath(),s,t);
                        Intent intent = new Intent(this, compression.class);
                        String u = s+t;
                        intent.putExtra("filepath",u);
                        startActivity(intent);
                    }
                    break;

            }
        }
    }

    private void copyFile(String inputPath, String outputPath, String outputFile) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }


            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath+outputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        }  catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }
}
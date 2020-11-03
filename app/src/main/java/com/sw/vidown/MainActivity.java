package com.sw.vidown;

import android.os.Bundle;
import android.app.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;

import android.app.DownloadManager;
import android.os.Environment;

import android.Manifest;
import android.content.pm.PackageManager;

import android.os.Process;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_VIEW.equals(action) && type != null)
        {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Process.myPid(), Process.myUid()) != PackageManager.PERMISSION_GRANTED)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Go to settings to grant storage permission if you want to write video files (this tool's purpose)").setTitle("Permission");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else
            {
                Uri data = intent.getData();

                DownloadManager.Request req = new DownloadManager.Request(data);
                req.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, data.getLastPathSegment());
                req.setAllowedOverMetered(false);

                DownloadManager dm = getSystemService(DownloadManager.class);
                dm.enqueue(req);

                finish();
            }
        }
    }
}

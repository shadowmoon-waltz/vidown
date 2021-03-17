// Copyright 2020 shadowmoon_waltz

// This file is part of vidown

// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

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
import android.content.DialogInterface;
import android.os.Build;
// import android.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_VIEW.equals(action) && type != null)
        {
            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Process.myPid(), Process.myUid()) != PackageManager.PERMISSION_GRANTED)
            {
                if (Build.VERSION.SDK_INT >= 14) {
                    setTheme(android.R.style.Theme_DeviceDefault_NoActionBar);
                }
                else if (Build.VERSION.SDK_INT >= 11) {
                    setTheme(android.R.style.Theme_Holo_NoActionBar);
                }
                super.onCreate(savedInstanceState);
                
                (new AlertDialog.Builder(this))
                    .setMessage("Go to settings to grant storage permission if you want to write video files (this tool's purpose)")
                    .setTitle("Permission Not Granted")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .create()
                    .show();
            }
            else
            {
                setTheme(android.R.style.Theme_NoDisplay);
                super.onCreate(savedInstanceState);
                
                Uri data = intent.getData();

                DownloadManager.Request req = new DownloadManager.Request(data);
                req.setDestinationInExternalPublicDir(Environment.DIRECTORY_MOVIES, data.getLastPathSegment());
                req.setAllowedOverMetered(false);

                DownloadManager dm = getSystemService(DownloadManager.class);
                dm.enqueue(req);
                
                finish();
            }
        }
        else {
            setTheme(android.R.style.Theme_NoDisplay);
            super.onCreate(savedInstanceState);
            
            finish();
        }
    }
}

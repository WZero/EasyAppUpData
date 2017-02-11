package com.easy.appupdata;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.anthonycr.grant.PermissionsResultAction;
import com.easy.appupdata.UpData.DialogOnClickListener;
import com.easy.appupdata.UpData.DownLoadApkTask;
import com.easy.appupdata.base.PermissionActivity;
import com.socks.library.KLog;

import java.io.File;

public class MainActivity extends PermissionActivity implements View.OnClickListener {
    //    String url = "http://v.meituan.net/mobile/app/Android/group-472_3-seo_qita_.apk";
    String url = "http://a5.pc6.com/xyb3/kuaidizaozhidao.pc6.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showAllPermission();
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_button_a:
                // TODO: 2017/2/10 通知栏更新
                break;
            case R.id.main_button_b:
                // TODO: 2017/2/10 弹出框更新
                getPermission(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, new PermissionsResultAction() {
                    @Override
                    public void onGranted() {
                        new DownLoadApkTask(MainActivity.this, new EasyDialogOnClickListener())
                                .startDownLoadApk(url,
                                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(),
                                        "123.apk");
                    }
                    @Override
                    public void onDenied(String permission) {
                        showPermissionDenyDialog();
                    }
                });
                break;
        }
    }

    private class EasyDialogOnClickListener implements DialogOnClickListener {
        private int DOWNLOAD_STATE = -1;
        private File apkFile;

        @Override
        public void onDownloadSuccess(File file) {
            this.apkFile = file;
            DOWNLOAD_STATE = 0;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            startActivity(intent);
        }

        @Override
        public void onDownloadFail() {
            Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (DOWNLOAD_STATE) {
                case -1:
                    Toast.makeText(getApplicationContext(), "取消下载", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    onDownloadSuccess(apkFile);
                    finish();
                    break;
                case 1:
                    break;
            }
        }
    }
}

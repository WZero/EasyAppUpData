package com.easy.appupdata.base;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.easy.appupdata.R;


/**
 * 作者： Zero
 * 时间： 2016/3/19
 */
public class PermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 获取指定权限
     *
     * @param permissions String[]
     * @param action      PermissionsResultAction
     */
    public void getPermission(@NonNull String[] permissions,
                              @Nullable PermissionsResultAction action) {
        PermissionsManager.getInstance().
                requestPermissionsIfNecessaryForResult(this, permissions, action);
    }

    /**
     * 获取指定权限
     *
     * @param permissions String
     */
    public void getPermission(@NonNull String permissions, @Nullable PermissionsResultAction action) {
        getPermission(new String[]{permissions}, action);
    }

    /**
     * 获取指定权限
     *
     * @param permissions String
     */
    public void getPermission(@NonNull String permissions) {
        getPermission(permissions, sPermissions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    private PermissionsResultAction sPermissions = new PermissionsResultAction() {
        @Override
        public void onGranted() {

        }

        @Override
        public void onDenied(String permission) {

        }
    };

    /**
     * 提示所有权限
     */
    public void showAllPermission(PermissionsResultAction permissions) {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, permissions);
    }

    /**
     * 提示所有权限
     */
    public void showAllPermission() {
        showAllPermission(sPermissions);
    }

    /**
     * 跳转到设置权限界面弹出框
     *
     * @param denyMessage String
     */
    public void showPermissionDenyDialog(String denyMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(denyMessage)
                .setCancelable(false)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    startActivity(intent);
                }

            }
        });
        builder.show();
    }

    /**
     * 跳转到设置权限界面弹出框
     */
    public void showPermissionDenyDialog() {
        showPermissionDenyDialog(String.format("对不起,由于您未授权使用相关权限该功能无法正常运行\n\n设置路径:->设置->应用->%s->权限",
                getResources().getString(R.string.app_name)));
    }
}

package com.easy.appupdata.UpData;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.MainThread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 作者： Wang
 * 时间： 2017/2/11
 */
public class DownLoadApkTask extends AsyncTask<String, Integer, File> {
    private ProgressDialog dialog;
    private Context context;
    private DialogOnClickListener onClickListener;
    private HttpURLConnection urlConnection;

    public DownLoadApkTask(Context context, DialogOnClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    protected void onPreExecute() {
        // TODO: 2017/2/11 主线程 执行之前回调
        dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle("下载中");
        dialog.setMax(0);
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", onClickListener);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                cancel(true);
            }
        });
        dialog.show();
    }

    @SuppressWarnings("All")
    @Override
    protected File doInBackground(String... params) {
        // TODO: 2017/2/11  AsyncTask 线程 做网络请求
        File file = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            URL url = new URL(params[0]);
            //打开连接请求
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept-Encoding", "identity");
            urlConnection.connect();//创建连接
            int fileLength = urlConnection.getContentLength();
            inputStream = urlConnection.getInputStream();
            file = new File(params[1]);
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(file, params[2]);
            outputStream = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int length;
            int downLength = 0;//下载进度
            while ((length = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, length);
                downLength += length;
                publishProgress(downLength, fileLength);
            }
        } catch (Exception e) {
            urlConnection.disconnect();
            e.printStackTrace();
            cancel(true);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (outputStream != null) {
                    outputStream.close();
                    outputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        // TODO: 2017/2/11  主线程 更新进度条
        dialog.setProgress(values[0] / 1024);
        dialog.setMax(values[1] / 1024);
    }

    @Override
    protected void onCancelled() {
        // TODO: 2017/2/11 主线程 任务取消时调用
        dialog.setTitle("下载失败");
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setText("确定");
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        onClickListener.onDownloadFail();
    }

    @Override
    protected void onPostExecute(File file) {
        // TODO: 2017/2/11 主线程 任务结束时调用
        dialog.setTitle("下载成功");
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setText("安装");
        onClickListener.onDownloadSuccess(file);
    }

    /**
     * @param apkUrl   Apk 下载地址
     * @param filePath 本地存储地址
     * @param fileName 本地存储文件名
     */
    @MainThread
    public void startDownLoadApk(String apkUrl, String filePath, String fileName) {
        execute(apkUrl, filePath, fileName);
    }

    /**
     * 取消网络请求
     */
    @MainThread
    public void cancelDownLoadApk() {
        dialog.dismiss();
    }
}
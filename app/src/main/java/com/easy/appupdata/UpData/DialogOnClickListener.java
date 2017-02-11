package com.easy.appupdata.UpData;

import android.content.DialogInterface;

import java.io.File;

/**
 * 作者： Wang
 * 时间： 2017/2/11
 */

public interface DialogOnClickListener extends DialogInterface.OnClickListener {
    /**
     * 下载成功回调
     *
     * @param file    下载文件
     */
    void onDownloadSuccess(File file);

    /**
     * 下载失败
     */
    void onDownloadFail();
}

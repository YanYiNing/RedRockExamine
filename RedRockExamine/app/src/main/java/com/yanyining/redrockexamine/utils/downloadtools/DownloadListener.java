package com.yanyining.redrockexamine.utils.downloadtools;

/**
 * Created by YanYiNing on 2017/4/17.
 */

public interface DownloadListener {
    void onProgress(int progress);
    void onSucess();
    void onFailed();
    void onPaused();
    void onCanceled();
}

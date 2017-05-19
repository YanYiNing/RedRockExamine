package com.yanyining.redrockexamine.utils.downloadtools;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by YanYiNing on 2017/4/17.
 */

public class DownloadTask extends AsyncTask<String, Integer, Integer> {
    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;
    private DownloadListener listener;
    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int lastProgress;

    public DownloadTask(DownloadListener listener)
    {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        BufferedInputStream bis = null;
        InputStream is = null;
        RandomAccessFile savedFile = null;
        URL url = null;
        File file = null;
        try{
            long downloadedLength = 0;
            String downloadUrl = params[0];
            url = new URL(params[0]);
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory + fileName);
            if (file.exists()) {
                downloadedLength = file.length();
            }
            long contentLength = getContentLength(downloadUrl);
            if (contentLength == 0) {
                return TYPE_FAILED;
            }else if (contentLength == downloadedLength) {
                return TYPE_SUCCESS;
            }

            URLConnection conn = url.openConnection();
            conn.setAllowUserInteraction(true);
            conn.setRequestProperty("RANGE", "bytes=" + downloadedLength + "-");
            byte[] buffer = new byte[1024];
            bis = new BufferedInputStream(conn.getInputStream());
            if (bis != null) {
                savedFile = new RandomAccessFile(file, "rw");
                savedFile.seek(downloadedLength);
                int len, total = 0;
                while ((len = bis.read(buffer, 0, 1024)) != -1) {
                    if (isCanceled) {
                        return TYPE_CANCELED;
                    } else if (isPaused) {
                        return TYPE_PAUSED;
                    } else {
                        total += len;
                        savedFile.write(buffer, 0, len);
                        int progress = (int) ((total + downloadedLength) * 100 / contentLength);
                        publishProgress(progress);
                    }
                }
                return TYPE_SUCCESS;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (savedFile != null) {
                    savedFile.close();
                }
                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress > lastProgress) {
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer status) {
        switch (status) {
            case TYPE_SUCCESS :
                listener.onSucess();
                break;
            case TYPE_FAILED :
                listener.onFailed();
                break;
            case TYPE_PAUSED :
                listener.onPaused();
                break;
            case TYPE_CANCELED :
                listener.onCanceled();
                break;
            default:
                break;
        }
    }

    public void pauseDownload () {
        isPaused = true;
    }

    public void cancelDownload() {
        isCanceled = true;
    }

    private long getContentLength(String downloadUrl) throws IOException{
        URL url = new URL(downloadUrl);
        URLConnection conn = url.openConnection();
        conn.setAllowUserInteraction(true);
        return conn.getContentLength();
    }
}

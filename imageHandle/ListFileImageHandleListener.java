package com.yt.kangaroo.libs.imageHandle;

import java.io.File;
import java.util.List;

public interface ListFileImageHandleListener {
    boolean onReady(List<File> inpFiles, File outPath);//准备
    void onProgress(String outFile, int position);//进展
    void onAllSuccess();//成功
    void onFailure(String text);//失败
    void onError(Exception e);//异常
}

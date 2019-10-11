package net.wt.gate.dev.util.imageHandle;

import java.io.File;

public interface FileImageHandleListener {
    boolean onReady(File inpFile, File outFile);//准备
    void onSuccess();//成功
    void onFailure(String text);//失败
    void onError(Exception e);//异常
}

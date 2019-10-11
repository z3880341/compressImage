package net.wt.gate.dev.util.imageHandle;

import android.graphics.Bitmap;

public interface BitmapImageHandleListener {
    boolean onReady(Bitmap inpBitmap);//准备
    void onSuccess(Bitmap outBitmap);//成功
    void onFailure(String text);//失败
    void onError(Exception e);//异常
}

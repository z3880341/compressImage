package net.wt.gate.dev.util.imageHandle;

import android.graphics.Bitmap;

public class BitmapImage {
    protected Bitmap mBitmap;
    protected int mTargetKB;
    protected float mTargetWidth;
    protected float mTargetHeight;
    protected BitmapImageHandleListener mBitmapImageHandleListener;
    protected BitmapImage(Bitmap bitmap){
        this.mBitmap = bitmap;
    }

    public BitmapImage setTargetKB(int targetKB){
        this.mTargetKB = targetKB;
        return this;
    }

    /**
     * 设置改变压缩图片的尺寸,注意如果不设置此属性,压缩的图片尺寸将会默认源图的尺寸
     * @param targetWidth 目标的宽
     * @param targetHeight 目标的高
     * @return
     */
    public BitmapImage setSize(float targetWidth,float targetHeight){
        this.mTargetWidth = targetWidth;
        this.mTargetHeight = targetHeight;
        return this;
    }

    public BitmapImage setHandleListener(BitmapImageHandleListener bitmapImageHandleListener){
        this.mBitmapImageHandleListener = bitmapImageHandleListener;
        return this;
    }

    public void build(){
        if (mBitmap == null){
            if (mBitmapImageHandleListener != null){
                mBitmapImageHandleListener.onFailure("bitmap is null");
            }
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                new ImageHandleUtil(BitmapImage.this);
            }
        }).start();

    }
}

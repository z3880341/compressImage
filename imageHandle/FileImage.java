package net.wt.gate.dev.util.imageHandle;

import java.io.File;

public class FileImage {
    protected File mInpFile;
    protected File mOutFile;
    protected int mTargetKB;
    protected float mTargetWidth;
    protected float mTargetHeight;
    protected FileImageHandleListener mFileImageHandleListener;
    protected FileImage(File inpFile,File outFile){
        this.mInpFile = inpFile;
        this.mOutFile = outFile;
    }

    public FileImage setTargetKB(int targetKB){
        this.mTargetKB = targetKB;
        return this;
    }

    /**
     * 设置改变压缩图片的尺寸,注意如果不设置此属性,压缩的图片尺寸将会默认源图的尺寸
     * @param targetWidth 目标的宽
     * @param targetHeight 目标的高
     * @return
     */
    public FileImage setSize(float targetWidth,float targetHeight){
        this.mTargetWidth = targetWidth;
        this.mTargetHeight = targetHeight;
        return this;
    }

    public FileImage setHandleListener(FileImageHandleListener fileImageHandleListener){
        this.mFileImageHandleListener = fileImageHandleListener;
        return this;
    }

    public void build(){
        if (mInpFile == null || mOutFile == null){
            if (mFileImageHandleListener != null){
                mFileImageHandleListener.onFailure("inpFile and outFile is null");
            }
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                new ImageHandleUtil(FileImage.this);
            }
        }).start();
    }

}

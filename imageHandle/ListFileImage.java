package net.wt.gate.dev.util.imageHandle;

import java.io.File;
import java.util.List;

public class ListFileImage {
    protected List<File> mInpFiles;
    protected File mOutPath;
    protected int mTargetKB;
    protected float mTargetWidth;
    protected float mTargetHeight;
    protected ListFileImageHandleListener mListFileImageHandleListener;

    protected ListFileImage(List<File> inpFiles,File outPath){
        this.mInpFiles = inpFiles;
        this.mOutPath = outPath;
    }

    public ListFileImage setTargetKB(int targetKB){
        this.mTargetKB = targetKB;
        return this;
    }

    /**
     * 设置改变压缩图片的尺寸,注意如果不设置此属性,压缩的图片尺寸将会默认源图的尺寸
     * @param targetWidth 目标的宽
     * @param targetHeight 目标的高
     * @return
     */
    public ListFileImage setSize(float targetWidth,float targetHeight){
        this.mTargetWidth = targetWidth;
        this.mTargetHeight = targetHeight;
        return this;
    }

    public ListFileImage setHandleListener(ListFileImageHandleListener listener){
        this.mListFileImageHandleListener = listener;
        return this;
    }

    public void build(){
        if (mInpFiles == null || mInpFiles.isEmpty() || mOutPath ==null){
            if (mListFileImageHandleListener!=null){
                mListFileImageHandleListener.onFailure("inpFiles and outFile is null");
            }
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                new ImageHandleUtil(ListFileImage.this);
            }
        }).start();
    }
}

package net.wt.gate.dev.util.imageHandle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class ImageHandleUtil {
    private static final String TAG = "ImageHandleUtil";
    private float mTargetWidth = 0;
    private float mTargetHeight = 0;
    private int  mTargetKB = 300;
    private int mDegrees = 0;//角度
    private BitmapImageHandleListener mBitmapImageHandleListener;
    private FileImageHandleListener mFileImageHandleListener;
    private ListFileImageHandleListener mListFileImageHandleListener;

    /**
     * 以文件图片形式传入压缩
     * @param fileImage
     */
    protected ImageHandleUtil(FileImage fileImage){
        this.mFileImageHandleListener = fileImage.mFileImageHandleListener;
        if (!fileImage.mInpFile.exists()){
            onFailure("inpFile not exists ");
            return;
        }
        if (!fileImage.mOutFile.getParentFile().isDirectory()){
            onFailure("outFile Directory not exists ");
            return;

        }
        if (fileImage.mTargetHeight != 0 && fileImage.mTargetWidth != 0){
            this.mTargetHeight = fileImage.mTargetHeight;
            this.mTargetWidth = fileImage.mTargetWidth;
        }
        if (fileImage.mTargetKB != 0){
            this.mTargetKB = fileImage.mTargetKB;
        }
        if (mFileImageHandleListener!=null){
            if (mFileImageHandleListener.onReady(fileImage.mInpFile,fileImage.mOutFile) == false){
                return;
            }
        }
        compressImage(fileImage.mInpFile.toString(),fileImage.mOutFile.toString(),mTargetKB);

    }

    /**
     * 以Bitmap位图形式传入压缩
     * @param bitmapImage
     */
    protected ImageHandleUtil(BitmapImage bitmapImage){
        this.mBitmapImageHandleListener = bitmapImage.mBitmapImageHandleListener;
        if (bitmapImage.mTargetHeight != 0 && bitmapImage.mTargetWidth != 0){
            this.mTargetHeight = bitmapImage.mTargetHeight;
            this.mTargetWidth = bitmapImage.mTargetWidth;
        }
        if (bitmapImage.mTargetKB != 0){
            this.mTargetKB = bitmapImage.mTargetKB;
        }
        if (mBitmapImageHandleListener!=null){
            if (mBitmapImageHandleListener.onReady(bitmapImage.mBitmap) == false){
                return;
            }
        }
        Bitmap bitmap = compressImage(sizeCompres(bitmapImage.mBitmap,mTargetWidth,mTargetHeight),mTargetKB);
        if (mBitmapImageHandleListener!=null){
            mBitmapImageHandleListener.onSuccess(bitmap);
        }

    }

    /**
     * 以批量文件图片形式传入压缩
     * @param listFileImage
     */
    protected ImageHandleUtil(ListFileImage listFileImage){
        this.mListFileImageHandleListener = listFileImage.mListFileImageHandleListener;
        if (!new File(listFileImage.mOutPath.getPath()).exists()){
            onFailure("outPath not exists");
            return;
        }
        if (listFileImage.mTargetHeight != 0 && listFileImage.mTargetWidth != 0){
            this.mTargetHeight = listFileImage.mTargetHeight;
            this.mTargetWidth = listFileImage.mTargetWidth;
        }
        if (listFileImage.mTargetKB != 0){
            this.mTargetKB = listFileImage.mTargetKB;
        }
        if (mListFileImageHandleListener!=null){
            if (mListFileImageHandleListener.onReady(listFileImage.mInpFiles,listFileImage.mOutPath) == false){
                return;
            }
        }
        compressImage(listFileImage.mInpFiles,listFileImage.mOutPath,mTargetKB);

    }


    /**
     * 文件图片 压缩
     * @param inpPath 输入图片路径与图片名称
     * @param outPath  输出图片路径与图片名称
     * @param targetKB 目标压缩KB
     */
    private void compressImage(String inpPath,String outPath, int targetKB){
        try {
            ExifInterface exifInterface = new ExifInterface(inpPath);//获取图片角度
            mDegrees = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            int angle = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            switch (angle){
                case ExifInterface.ORIENTATION_ROTATE_270:
                    mDegrees = 270;

                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    mDegrees = 180;

                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    mDegrees = 90;

                    break;
                default:
                    mDegrees = 0;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeFile(inpPath);
            if (mTargetWidth == 0 && mTargetHeight == 0){
                mTargetWidth = bitmap.getWidth();
                mTargetHeight = bitmap.getHeight();
            }
            Bitmap finishBitmap = compressImage(sizeCompres(bitmap,mTargetWidth,mTargetHeight), targetKB);
            FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(outPath);
            Matrix matrix = new Matrix();
            matrix.setRotate(mDegrees, finishBitmap.getWidth(), finishBitmap.getHeight());
            finishBitmap = Bitmap.createBitmap(finishBitmap, 0, 0, finishBitmap.getWidth(), finishBitmap.getHeight(), matrix,true);
            finishBitmap.compress(Bitmap.CompressFormat.JPEG,90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            bitmap.recycle();
            finishBitmap.recycle();
            if (mFileImageHandleListener!=null){
                mFileImageHandleListener.onSuccess();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            onError(e);
        } catch (IOException e) {
            e.printStackTrace();
            onError(e);
        }
    }

    /**
     * 文件图片 批量压缩
     * @param inpFiles
     */
    private void compressImage(List<File> inpFiles,File outPath,int targetKB){
        for (int i=0;i<inpFiles.size();i++){
            File inpFile = inpFiles.get(i);
            if (inpFile == null){
                onFailure("Failure: position "+i+" File is null");
                continue;
            }
            if (!inpFile.exists()){
                onFailure("Failure: position "+i+" File not exists. Failure path:"+inpFile.toString());
                continue;
            }
            compressImage(inpFile.toString(),outPath.getPath()+"/"+inpFile.getName(),targetKB);
            if (mListFileImageHandleListener != null){
                mListFileImageHandleListener.onProgress(outPath.getPath()+"/"+inpFile.getName(),i);
            }
        }
        if (mListFileImageHandleListener != null){
            mListFileImageHandleListener.onAllSuccess();
        }

    }

    /**
     *
     * @param bitmap bitmap图片 压缩
     * @param targetKB 目标压缩大小
     * @return
     */
    private Bitmap compressImage(Bitmap bitmap, int targetKB){
        Bitmap outBitmap = null;
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            int quality = 80;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            while ((baos.toByteArray().length/1024) > targetKB){
                quality = quality-10;
                baos.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG,quality,baos);
            }
            bais = new ByteArrayInputStream(baos.toByteArray());
            outBitmap = BitmapFactory.decodeStream(bais);
            bais.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
            onError(e);
        } finally {
            try {
                bais.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return outBitmap;

    }

    /**
     * 尺寸压缩
     * @param bitmap
     */
    private Bitmap sizeCompres(Bitmap bitmap,float targetWidth,float targetHeight){
        Bitmap handleBitmap = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
            // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            if (baos.toByteArray().length / 1024 > 1024) {
                baos.reset();// 重置baos即清空baos
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
            }
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(bais,null,options);
            options.inJustDecodeBounds = false;
            int imageWidth = options.outWidth;
            int imageHeight = options.outHeight;
            int be = 1;
            if (imageWidth>imageHeight && imageWidth>targetWidth){
                be = Math.round(imageWidth/targetWidth);

            }else if (imageHeight>imageWidth && imageHeight>targetHeight){
                be = Math.round(imageHeight/targetHeight);
            }
            if (be <= 1){
                be =1;//如果小于1等于1就不需要压缩直接返回
            }
            options.inSampleSize = be;
            bais = new ByteArrayInputStream(baos.toByteArray());//bais运行到这里可能已经清空了，所以需要再次添加
            handleBitmap = BitmapFactory.decodeStream(bais,null,options);
            bais.close();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
            onError(e);
        }
        return handleBitmap;

    }

    private void onFailure(String text){
        if (mFileImageHandleListener!=null){
            mFileImageHandleListener.onFailure(text);

        }
        if (mBitmapImageHandleListener!=null){
            mBitmapImageHandleListener.onFailure(text);

        }
        if (mListFileImageHandleListener!=null){
            mListFileImageHandleListener.onFailure(text);
        }
    }

    private void onError(Exception e){
        if (mFileImageHandleListener != null){
            mFileImageHandleListener.onError(e);
        }
        if (mBitmapImageHandleListener != null){
            mBitmapImageHandleListener.onError(e);
        }
        if (mListFileImageHandleListener!=null){
            mListFileImageHandleListener.onError(e);
        }

    }

}

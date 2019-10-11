package net.wt.gate.dev.util.imageHandle;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.List;

public class ImageHandle {
    /**
     * 文件图片形式压缩
     * @param inpFile 传入的图片
     * @param outFile 传出的图片（必需带路径和图片名称）
     * @return
     */
    public static FileImage fileImageConfig(@NonNull File inpFile, @NonNull File outFile){
        return new FileImage(inpFile,outFile);
    }

    /**
     * 位图图片形式压缩
     * @param bitmap
     * @return
     */
    public static BitmapImage bitmapImageConfig(@NonNull Bitmap bitmap){
        return new BitmapImage(bitmap);

    }

    /**
     * 批量文件图片形式压缩
     * @param inpFile 传入的图片
     * @param outPath 传出的路径（只需要路径）
     * @return
     */
    public static ListFileImage listFileImageConfig(@NonNull List<File> inpFile, @NonNull File outPath){
        return new ListFileImage(inpFile,outPath);

    }
}

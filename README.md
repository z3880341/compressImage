# compressImage

压缩图片的一个框架工具,支持Bitmap传入压缩,图片文件传入压缩,批量图片文件传入压缩.
(另外已经在底层处理好图片角度问题,所有以文件形式传入的图片都会保持原来的角度,以Bitmap传入压缩无法处理角度)
注意使用Bitmap传入压缩 和 图片文件传入压缩这两种压缩,传入和传出都需要完整文件路径 + 文件名称
批量压缩用法如下:
/*
注意批量压缩传入File需要完整的完整文件路径 + 文件名称
传出文件路径,只需要保存目录
*/
ImageHandle.listFileImageConfig(imageFileList, FilePathSession.getCompressImagePath())
                .setTargetKB(400)
                .setHandleListener(new ListFileImageHandleListener() {
                    @Override
                    public boolean onReady(List<File> inpFiles, File outPath) {
                        return true;
                    }

                    @Override
                    public void onProgress(String outFile, int position) {

                    }

                    @Override
                    public void onAllSuccess() {

                    }

                    @Override
                    public void onFailure(String text) {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                }).build();

package com.example.menglingshuai.addpicture.imagefileselector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.File;
import java.util.Arrays;

@SuppressWarnings("unused")
public class ImageFileSelector {

    private static final String TAG = ImageFileSelector.class.getSimpleName();

    private Callback mCallback;
    private ImagePickHelper mImagePickHelper;
    private ImageCaptureHelper mImageTaker;
    private ImageCompressHelper mImageCompressHelper;

	public ImageFileSelector(final Context context) {
		this(context, false);
	}

    public ImageFileSelector(final Context context, boolean isAllow) {
        mImagePickHelper = new ImagePickHelper(context, isAllow);
        mImagePickHelper.setCallback(new ImagePickHelper.Callback() {
            @Override
            public void onSuccess(String[] files) {
                AppLogger.d(TAG, "select image from sdcard: " + Arrays.toString(files));
                handleResult(files, false);
            }

            @Override
            public void onError() {
                handleError();
            }
        });

        mImageTaker = new ImageCaptureHelper();
        mImageTaker.setCallback(new ImageCaptureHelper.Callback() {
            @Override
            public void onSuccess(String file) {
                AppLogger.d(TAG, "select image from camera: " + file);
                handleResult(new String[]{file}, true);
            }

            @Override
            public void onError() {
                handleError();
            }
        });

        mImageCompressHelper = new ImageCompressHelper(context);
        mImageCompressHelper.setCallback(new ImageCompressHelper.CompressCallback() {
            @Override
            public void onCallBack(String outFile) {
                AppLogger.d(TAG, "compress image output: " + outFile);
                if (mCallback != null) {
                    mCallback.onSuccess(new String[]{outFile});
                }
            }
        });
    }

    public static void setDebug(boolean debug) {
        AppLogger.DEBUG = debug;
    }

    /**
     * 设置压缩后的文件大小
     *
     * @param maxWidth  压缩后文件宽度
     * @param maxHeight 压缩后文件高度
     */
    @SuppressWarnings("unused")
    public void setOutPutImageSize(int maxWidth, int maxHeight) {
        mImageCompressHelper.setOutPutImageSize(maxWidth, maxHeight);
    }

    /**
     * 设置压缩后保存图片的质量
     *
     * @param quality 图片质量 0 - 100
     */
    @SuppressWarnings("unused")
    public void setQuality(int quality) {
        mImageCompressHelper.setQuality(quality);
    }

    public void onActivityResult(Activity imageFileActivity, boolean stopped, int requestCode, int resultCode, Intent data) {
        mImagePickHelper.onActivityResult(requestCode, resultCode, data);
        mImageTaker.onActivityResult(imageFileActivity, stopped, requestCode, resultCode, data);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        mImagePickHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onSaveInstanceState(Bundle outState) {
        mImageTaker.onSaveInstanceState(outState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mImageTaker.onRestoreInstanceState(savedInstanceState);
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void selectImage(Activity activity) {
        mImagePickHelper.selectorImage(activity);
    }

    public void takePhoto(Activity activity) {
        mImageTaker.captureImage(activity);
    }

    private void handleResult(String[] fileNames, boolean deleteSrc) {
    	if(mCallback == null) {
    		return;
	    }
    	if(fileNames == null) {
    		mCallback.onSuccess(new String[]{});
	    } else if(fileNames.length == 1) {
		    File file = new File(fileNames[0]);
		    if (file.exists()) {
			    mImageCompressHelper.compress(fileNames[0], deleteSrc);
		    } else {
			    mCallback.onSuccess(new String[]{});
		    }
	    } else {
    		mCallback.onSuccess(fileNames);
	    }
    }

    private void handleError() {
        if (mCallback != null) {
            mCallback.onError();
        }
    }

    public interface Callback {
        void onSuccess(String[] files);

        void onError();
    }

}

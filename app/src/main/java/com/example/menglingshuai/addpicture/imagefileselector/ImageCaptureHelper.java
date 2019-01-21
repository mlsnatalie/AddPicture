package com.example.menglingshuai.addpicture.imagefileselector;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;
import com.example.menglingshuai.addpicture.R;

import java.io.File;


@SuppressWarnings("unused")
class ImageCaptureHelper {

    private static final String KEY_OUT_PUT_FILE = "key_out_put_file";
    private static final int CHOOSE_PHOTO_FROM_CAMERA = 0x702;

    private File mOutFile;
    private Callback mCallback;

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void onSaveInstanceState(Bundle outState) {
        if (mOutFile != null) {
            outState.putString(KEY_OUT_PUT_FILE, mOutFile.getPath());
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        String tempFilePath = savedInstanceState.getString(KEY_OUT_PUT_FILE);
        if (!TextUtils.isEmpty(tempFilePath)) {
            mOutFile = new File(tempFilePath);
        }
    }

    public void onActivityResult(Context context, boolean stoped, int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_PHOTO_FROM_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (mOutFile != null && mOutFile.exists()) {
                    if (mCallback != null) {
                        mCallback.onSuccess(mOutFile.getPath());
                    }
                } else {
                    if (mCallback != null) {
                        mCallback.onSuccess(null);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED && !stoped) {
                Toast.makeText(context, R.string.hint_no_camera_permission, Toast.LENGTH_SHORT).show();
            }


        }
    }

    public void captureImage(Activity activity) {
        mOutFile = CommonUtils.generateExternalImageCacheFile(activity, ".jpg");
        try {
            activity.startActivityForResult(createIntent(activity), CHOOSE_PHOTO_FROM_CAMERA);
        } catch (ActivityNotFoundException e) {
            AppLogger.printStackTrace(e);
            if (mCallback != null) {
                mCallback.onError();
            }
        }
    }

    private Intent createIntent(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        int currentVersuion = Build.VERSION.SDK_INT;
        if (currentVersuion < 24) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mOutFile));
        } else {
            Uri imageUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".fileprovider", mOutFile);//通过FileProvider创建一个content类型的Uri
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        }
        return intent;
    }

    public interface Callback {

        void onSuccess(String fileName);

        void onError();
    }
}

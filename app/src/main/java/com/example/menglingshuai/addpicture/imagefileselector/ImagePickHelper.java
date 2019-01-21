package com.example.menglingshuai.addpicture.imagefileselector;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


class ImagePickHelper {

    private static final int SELECT_PIC = 0x701;
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 0x11;

    private Callback mCallback;
    private Context mContext;
    private boolean isAllowMultiple = false;

    private WeakReference<Activity> mActivityWeakReference;

    public ImagePickHelper(Context context) {
        mContext = context;
    }

    public ImagePickHelper(Context context, boolean isAllowMultiple) {
    	this(context);
    	this.isAllowMultiple = isAllowMultiple;
    }

    public void enableAllowMultiple(boolean isAllowMultiple) {
    	this.isAllowMultiple = isAllowMultiple;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public void selectorImage(Activity activity) {
//        if (Build.VERSION.SDK_INT >= 16) {
//            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                //申请WRITE_EXTERNAL_STORAGE权限
//                mActivityWeakReference = new WeakReference<>(activity);
//                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                        READ_EXTERNAL_STORAGE_REQUEST_CODE);
//
//            } else {
//                doSelect(activity);
//            }
//        } else {
            doSelect(activity);
//        }
    }

    private void doSelect(Activity activity) {
        Intent intent = createIntent();
        activity.startActivityForResult(intent, SELECT_PIC);
    }

    private Intent createIntent() {
	    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
	        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, isAllowMultiple);
        }
        return intent;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode != SELECT_PIC || mCallback == null) {
        	return;
        }

        if(data == null) {
        	mCallback.onError();
        	return;
        }

	    if (data.getClipData() != null || data.hasExtra("uris")) {
	    	// multiple
		    String[] filePaths;
		    if (data.hasExtra("uris")) {
			    ArrayList<Uri> uris = data.getParcelableArrayListExtra("uris");
			    filePaths = new String[uris.size()];
			    for (int i = 0; i < uris.size(); i++) {
				    filePaths[i] = Compatibility.getPath(mContext, uris.get(i));
			    }
		    } else {
			    ClipData clipData = data.getClipData();
			    int count = clipData.getItemCount();
			    filePaths = new String[count];
			    for (int i = 0; i < count; i++) {
				    filePaths[i] = Compatibility.getPath(mContext, clipData.getItemAt(i).getUri());
			    }
		    }
		    mCallback.onSuccess(filePaths);
	    } else if(data.getData() != null) {
		    String[] files = new String[]{Compatibility.getPath(mContext, data.getData())};
		    mCallback.onSuccess(files);
	    } else {
	    	mCallback.onError();
	    }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Activity activity = mActivityWeakReference.get();
                if (activity != null) {
                    doSelect(activity);
                }
            } else {
                if (mCallback != null) {
                    mCallback.onError();
                }
            }
        }
    }

    public interface Callback {
		void onSuccess(String[] files);

		void onError();
	}


}

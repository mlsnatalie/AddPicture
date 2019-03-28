package com.example.menglingshuai.addpicture.imagefileselector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.menglingshuai.addpicture.R;

import java.io.File;
import java.util.Arrays;

public class ImageFileActivity extends FragmentActivity implements View.OnClickListener {

    public static final String INTENT_FILE_RESULT = "file_result";
    public static final String INTENT_TITLE = "title";
    public static final String INTENT_SELECT_TYPE = "type";
    public static final int TYPE_NONE = 0;
    public static final int TYPE_TAKE_PHOTO = 1;
    public static final int TYPE_SELECT_IMAGE = 2;

    private LinearLayout photoContainerView;
    private LinearLayout defaultContainerView;
    private ImageView imageView;
    private ImageFileSelector imageFileSelector;
    private ImageCropper imageCropper;
    private TextView cropBtn;
    private TextView chooserBtn;
    private TextView uploadBtn;
    private TextView cameraBtn;
    private TextView titleView;
    private ImageView backView;
    private ImageView cameraView;

    private String selectedFilePath;
    private boolean stopped;
    private int type = TYPE_NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_file);
        imageView = (ImageView) findViewById(R.id.iv_preview);
        cropBtn = (TextView) findViewById(R.id.btn_crop);
        chooserBtn = (TextView) findViewById(R.id.btn_chooser);
        uploadBtn = (TextView) findViewById(R.id.btn_confirm);
        cameraBtn = (TextView) findViewById(R.id.btn_camera);
        titleView = (TextView) findViewById(R.id.tv_title);
        backView = (ImageView) findViewById(R.id.iv_left_action);
        cameraView = (ImageView) findViewById(R.id.iv_camera);
        photoContainerView = (LinearLayout) findViewById(R.id.ll_photo_container);
        defaultContainerView = (LinearLayout) findViewById(R.id.ll_default_container);

        cropBtn.setOnClickListener(this);
        chooserBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);
        cameraBtn.setOnClickListener(this);
        backView.setOnClickListener(this);
        cameraView.setOnClickListener(this);

        imageFileSelector = new ImageFileSelector(this, true);
        imageCropper = new ImageCropper(this);

        imageFileSelector.setCallback(new ImageFileSelector.Callback() {
            @Override
            public void onSuccess(String[] files) {
                Log.d("select-image", Arrays.toString(files));
                if (files != null && files.length > 0 && !TextUtils.isEmpty(files[0])) {
                    selectedFilePath = files[0];
                    loadImage();
                }
            }

            @Override
            public void onError() {
            }
        });
        imageCropper.setCallback(new ImageCropper.ImageCropperCallback() {
            @Override
            public void onCropperCallback(ImageCropper.CropperResult result, File srcFile, File outFile) {
                if (result == ImageCropper.CropperResult.success) {
                    selectedFilePath = outFile.getPath();
                    loadImage();
                } else {
                    Toast.makeText(ImageFileActivity.this, "裁切图片失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        init(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        stopped = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopped = true;
    }

    private void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String title = intent.getStringExtra(INTENT_TITLE);
        if (!TextUtils.isEmpty(title)) {
            titleView.setText(title);
        }
        loadImage();
        if (savedInstanceState == null) {
            type = getIntent().getIntExtra(INTENT_SELECT_TYPE, TYPE_NONE);
            if (type == TYPE_TAKE_PHOTO) {
                imageFileSelector.takePhoto(this);
            } else if (type == TYPE_SELECT_IMAGE) {
                imageFileSelector.selectImage(this);
            }
        }
    }

    private void loadImage() {
        if (TextUtils.isEmpty(selectedFilePath)) {
            defaultContainerView.setVisibility(View.VISIBLE);
            photoContainerView.setVisibility(View.GONE);
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(selectedFilePath);
            if (bitmap == null) {
                Toast.makeText(ImageFileActivity.this, "图片格式错误！", Toast.LENGTH_SHORT).show();
            } else {
                imageView.setImageBitmap(bitmap);
                defaultContainerView.setVisibility(View.GONE);
                photoContainerView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageFileSelector.onActivityResult(this,stopped,requestCode, resultCode, data);
        imageCropper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("selectedFilePath", selectedFilePath);
        imageFileSelector.onSaveInstanceState(outState);
        imageCropper.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("selectedFilePath")) {
            selectedFilePath = savedInstanceState.getString("selectedFilePath");
        }
        imageFileSelector.onRestoreInstanceState(savedInstanceState);
        imageCropper.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_crop) {
            if (TextUtils.isEmpty(selectedFilePath)) {
                Toast.makeText(this, "请先选取一张照片", Toast.LENGTH_SHORT).show();
            } else {
                imageCropper.cropImage(new File(selectedFilePath));
            }
        } else if (id == R.id.btn_chooser) {
            imageFileSelector.selectImage(this);
        } else if (id == R.id.btn_confirm) {
            if (TextUtils.isEmpty(selectedFilePath)) {
                Toast.makeText(this, "请先选取一张照片", Toast.LENGTH_SHORT).show();
            } else {
                confirm();
            }
        } else if (id == R.id.iv_left_action) {
            finish();
        } else if (id == R.id.iv_camera) {
            imageFileSelector.takePhoto(this);
        } else if (id == R.id.btn_camera) {
            imageFileSelector.takePhoto(this);
        }
    }

    private void confirm() {
        Intent intent = new Intent();
        intent.putExtra(INTENT_FILE_RESULT, selectedFilePath);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}

package com.android.traveling.developer.jiaming.liu.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.UtilTools;
import com.android.traveling.widget.dialog.ChoosePhotoDialog;
import com.android.traveling.widget.dialog.PhotoViewDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.jiaming.liu.Activity
 * 文件名：AddNoteActivity
 * 创建者：LJM
 * 创建时间：2018/10/1 15:19
 * 描述：  新建游记
 */
public class AddNoteActivity extends AppCompatActivity {

    /**
     * 传递过来的参数
     * 判断是发表游记还是攻略
     */
    public static final String INTENT_EXTRA_TYPE = "type";


    /**
     * 选择照片请求码
     */
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;

    /**
     * 拍照请求码
     */
    private static final int REQUEST_CODE_TAKE_PHOTO = 2;

    /**
     * 申请读取存储卡权限请求码
     */
    private static final int REQUEST__CODE_READ_EXTERNAL_STORAGE = 3;

    /**
     * 申请相机权限请求码
     */
    private static final int REQUEST_CODE_CAMERA = 4;

    EditText et_title;  //标题输入框
    EditText et_content;  //内容输入框
    ImageView imageView;  //图片
    ConstraintLayout choose_picture;  //添加照片按钮
    Uri ImageUri;  //用于存储图片

    private ChoosePhotoDialog choosePhotoDialog;
    private PhotoViewDialog photoViewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initView();


    }

    //初始化View
    private void initView() {
        Intent intent = getIntent();
        String type = intent.getStringExtra(INTENT_EXTRA_TYPE);

        TextView title = findViewById(R.id.tv_title);
        title.setText(type);
        et_title = findViewById(R.id.et_title);
        et_content = findViewById(R.id.et_content);
        imageView = findViewById(R.id.note_photo);
        choose_picture = findViewById(R.id.add_picture);

        //添加事件监听
        addEvents();
    }

    //添加事件监听
    private void addEvents() {

        //取消
        TextView tv_cancel = findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(v -> finish());

        //选择照片或拍照
        choosePhotoDialog = new ChoosePhotoDialog(this, new ChoosePhotoDialog.OnBtnClickListener() {
            @Override
            public void onTakePhotoBtnClick(View v) {
                takePhoto();
                choosePhotoDialog.dismiss();
            }

            @Override
            public void onFromAlbumBtnClick(View v) {
                choosePhoto();
                choosePhotoDialog.dismiss();
            }
        });


        choose_picture.setOnClickListener(v -> {
            if (ImageUri != null) {
                UtilTools.toast(this, "暂时只能选择一张图片噢");
                return;
            }
            choosePhotoDialog.show();
        });

        imageView.setOnClickListener(v -> {
            if (photoViewDialog != null) {
                photoViewDialog.show();
            }
        });
    }

    /**
     * 选择图片
     */
    private void choosePhoto() {
        if (ContextCompat.checkSelfPermission(AddNoteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddNoteActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, REQUEST__CODE_READ_EXTERNAL_STORAGE);
        } else {
            //调用相册
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_CHOOSE_PHOTO);
        }
    }


    /**
     * 拍照
     */
    private void takePhoto() {
        if (ContextCompat.checkSelfPermission(AddNoteActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddNoteActivity.this, new String[]{
                    Manifest.permission.CAMERA
            }, REQUEST_CODE_CAMERA);
        } else {
            //创建File对象，用于存储拍照后的图片
            File outputImage = new File(getExternalCacheDir(), "outputImage.jpg");
            try {
                if (outputImage.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    outputImage.delete();
                }
                //noinspection ResultOfMethodCallIgnored
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImageUri = Uri.fromFile(outputImage);
            //启动相机程序
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageUri);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
        }
    }

    /**
     * 申请权限后的操作
     *
     * @param requestCode  requestCode
     * @param permissions  permissions
     * @param grantResults grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST__CODE_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhoto();
                } else {
                    UtilTools.toast(this, "您拒绝了此权限，因而无法通过相册选择图片！");
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    UtilTools.toast(this, "您拒绝了此权限，因而无法进行拍照！");
                }
                break;
        }
    }

    /**
     * 拍照或选择照片后的操作
     *
     * @param requestCode requestCode
     * @param resultCode  resultCode
     * @param data        data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    ImageUri = data.getData();
                    LogUtil.d("相册uri：" + ImageUri);
                    try {
                        showImageView(MediaStore.Images.Media.getBitmap(getContentResolver(), ImageUri));
                    } catch (IOException e) {
                        LogUtil.d("setImageBitmap 出错了");
                        e.printStackTrace();
                    }
                    //                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
                    //                    if (ImageUri != null) {
                    //                        Cursor c = getContentResolver().query(ImageUri, filePathColumns, null, null, null);
                    //                        if (c != null) {
                    //                            c.moveToFirst();
                    //                            int columnIndex = c.getColumnIndex(filePathColumns[0]);
                    //                            String imgPath = c.getString(columnIndex);
                    //                            LogUtil.d("相册path：" + imgPath);
                    //                            c.close();
                    //                        }
                    //                    }
                } else {
                    ImageUri = null;
                }
                break;
            case REQUEST_CODE_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    String imgPath = ImageUri.getPath();
                    LogUtil.d("相机path：" + imgPath);
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(ImageUri));
                        showImageView(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    et_content.setText(imgPath);
                } else {
                    ImageUri = null;
                }
                break;
        }
    }

    /**
     * 将选择的图片展示出来
     *
     * @param bitmap bitmap
     */
    private void showImageView(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        imageView.setVisibility(View.VISIBLE);
        generatePhotoViewDialog();
    }

    /**
     * 生成Dialog
     */
    private void generatePhotoViewDialog() {
        photoViewDialog = new PhotoViewDialog(this, ImageUri, v -> {
            ImageUri = null;
            imageView.setVisibility(View.GONE);
        });
    }
}

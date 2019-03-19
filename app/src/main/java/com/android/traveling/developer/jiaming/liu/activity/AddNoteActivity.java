package com.android.traveling.developer.jiaming.liu.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.entity.msg.NoteMsg;
import com.android.traveling.entity.note.BaseNote;
import com.android.traveling.entity.note.Note;
import com.android.traveling.entity.user.TravelingUser;
import com.android.traveling.entity.user.User;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.UtilTools;
import com.android.traveling.widget.dialog.ChoosePhotoDialog;
import com.android.traveling.widget.dialog.PhotoViewDialog;
import com.android.traveling.widget.dialog.PublishDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

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
     * 传回去的参数 note
     */
    public static final String INTENT_EXTRA_NOTE = "note";


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
    Button btn_publish;  //发表
    ConstraintLayout choose_picture;  //添加照片按钮
    Uri ImageUri;  //用于存储图片

    private boolean bTitle = false;  //标题输入框是否为空
    private boolean bContent = false;  //内容输入框是否为空

    private ChoosePhotoDialog choosePhotoDialog;
    private PhotoViewDialog photoViewDialog;
    private PublishDialog publishDialog;
    private int noteTag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initView();
    }

    //初始化View
    private void initView() {
        Intent intent = getIntent();
        noteTag = intent.getIntExtra(INTENT_EXTRA_TYPE, 1);

        publishDialog = new PublishDialog(this);

        TextView title = findViewById(R.id.tv_title);
        switch (noteTag) {
            case BaseNote.TAG_1:
                title.setText("发表游记");
                break;
            case BaseNote.TAG_2:
                title.setText("发表攻略");
                break;
        }
        btn_publish = findViewById(R.id.btn_publish);
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

        //标题输入监听
        et_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    bTitle = false;
                    btn_publish.setTextColor(getResources().getColor(R.color.gray_comment));
                    btn_publish.setBackgroundColor(getResources().getColor(R.color.bind_gray));
                } else {
                    bTitle = true;
                    if (bContent) {
                        btn_publish.setTextColor(getResources().getColor(R.color.black));
                        btn_publish.setBackgroundColor(getResources().getColor(R.color.bind_yellow));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //内容输入监听
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    bContent = false;
                    btn_publish.setTextColor(getResources().getColor(R.color.gray_comment));
                    btn_publish.setBackgroundColor(getResources().getColor(R.color.bind_gray));
                } else {
                    bContent = true;
                    if (bTitle) {
                        btn_publish.setTextColor(getResources().getColor(R.color.black));
                        btn_publish.setBackgroundColor(getResources().getColor(R.color.bind_yellow));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //发表
        btn_publish.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(et_title.getText()) && !TextUtils.isEmpty(et_content.getText())) {
                //发布
                User currentUser = TravelingUser.getCurrentUser();
                if (currentUser != null) {
                    publishDialog.show();
                    BaseNote baseNote = new BaseNote(currentUser.getUserId(), et_title.getText().toString(),
                            et_content.getText().toString(), noteTag);
                    Call<NoteMsg> call = baseNote.publish(getPart(), new BaseNote.CallBack() {
                        @Override
                        public void onSuccess(Note note) {
                            Intent intent = new Intent();
                            intent.putExtra(INTENT_EXTRA_NOTE, note);
                            setResult(RESULT_OK, intent);
                            UtilTools.toast(AddNoteActivity.this, "发表成功");
                            finish();
                            publishDialog.dismiss();
                        }

                        @Override
                        public void onFailure(String reason) {
                            UtilTools.toast(AddNoteActivity.this, "发表失败：" + reason);
                            finish();
                            publishDialog.dismiss();
                        }
                    });
                    publishDialog.setOnCancelListener(dialog -> {
                        UtilTools.toast(AddNoteActivity.this, "已取消");
                        call.cancel();
                        dialog.dismiss();
                    });
                } else {
                    UtilTools.toast(this, "未登录状态！");
                }
            }
        });

        //选择照片
        choose_picture.setOnClickListener(v -> {
            if (ImageUri != null) {
                UtilTools.toast(this, "暂时只能选择一张图片噢");
                return;
            }
            choosePhotoDialog.show();
        });

        //点击预览
        imageView.setOnClickListener(v -> {
            if (photoViewDialog != null) {
                photoViewDialog.show();
            }
        });
    }

    /**
     * uri 转 MultipartBody.Part
     *
     * @return MultipartBody.Part
     */
    private MultipartBody.Part getPart() {
        String[] filePathColumns = {MediaStore.Images.Media.DATA};
        if (ImageUri != null) {
            Cursor c = getContentResolver().query(ImageUri, filePathColumns, null, null, null);
            if (c != null) {
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String imgPath = c.getString(columnIndex);
                File file = new File(imgPath);
                RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("imgfile", file.getName(), imageBody);
                c.close();
                return imageBodyPart;
            }
        }
        return null;
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

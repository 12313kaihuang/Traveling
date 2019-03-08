package com.android.traveling.developer.zhiming.li.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.entity.msg.Msg;
import com.android.traveling.entity.user.TravelingUser;
import com.android.traveling.entity.user.User;
import com.android.traveling.entity.user.UserCallback;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.UtilTools;
import com.android.traveling.widget.dialog.ChoosePhotoDialog;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.ui
 * 文件名：UserEditActivity
 * 创建者：HY
 * 创建时间：2018/10/12 19:50
 * 描述：  编辑个人资料
 */

public class UserEditActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;  //从相册选择的请求码
    private static final int REQUEST_CODE_TAKE_PHOTO = 2;  //拍照的请求码
    private static final int REQUEST__CODE_READ_EXTERNAL_STORAGE = 3;  //申请读取存储卡权限请求码
    private static final int REQUEST_CODE_CAMERA = 4;  //申请相机权限请求码

    public static final int TYPE_USER_BG = 0;  //当前为上传用户背景
    public static final int TYPE_USER_IMG = 1;  //当前为上传用户头像

    @IntDef({TYPE_USER_BG, TYPE_USER_IMG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UploadType {
    }

    private User user;
    private EditText username;
    private EditText gender;
    private EditText live_area;
    private EditText signature;

    /**
     * 用户背景
     */
    private ImageView user_bg;
    /**
     * 用户头像
     */
    private CircleImageView user_img;
    /**
     * 拍照后保存图片路径
     */
    private Uri ImageUri;
    /**
     * 当前是上传头像还是上传背景
     */
    @UploadType
    private int uploadType;


    private ChoosePhotoDialog choosePhotoDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
        initView();
        initData();
    }

    //初始化View
    private void initView() {
        user = TravelingUser.getCurrentUser();
        username = findViewById(R.id.username);
        gender = findViewById(R.id.gender);
        live_area = findViewById(R.id.live_area);
        signature = findViewById(R.id.signature);
        user_bg = findViewById(R.id.user_bg);
        user_img = findViewById(R.id.user_img);
        TextView set_user_bg = findViewById(R.id.set_user_bg);
        ImageView img_back = findViewById(R.id.img_back);
        TextView save = findViewById(R.id.save);
        save.setOnClickListener(this);
        img_back.setOnClickListener(this);


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

        //设置背景
        set_user_bg.setOnClickListener(v -> {
            uploadType = TYPE_USER_BG;
            choosePhotoDialog.show();
        });
        //设置头像
        user_bg.setOnClickListener(v -> {
            uploadType = TYPE_USER_IMG;
            choosePhotoDialog.show();
        });
    }

    /**
     * 选择图片
     */
    private void choosePhoto() {
        if (ContextCompat.checkSelfPermission(UserEditActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UserEditActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, REQUEST__CODE_READ_EXTERNAL_STORAGE);
        } else {
            //调用相册
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_CHOOSE_PHOTO);
        }
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        if (ContextCompat.checkSelfPermission(UserEditActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UserEditActivity.this, new String[]{
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


    //初始化数据
    private void initData() {
        username.setText(user.getNickName());
        signature.setText(user.getSignature());
        live_area.setText(user.getArea());
        gender.setText(user.getGender());
        Picasso.get().load(user.getImg()).error(R.drawable.err_img_bg).fit().into(user_img);
        Picasso.get().load(user.getBackgroundImg()).error(R.drawable.err_img_bg).fit().into(user_bg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                User newUser = TravelingUser.getCurrentUser();
                if (newUser != null) {
                    newUser.setNickName(username.getText().toString());
                    newUser.setSignature(signature.getText().toString());
                    newUser.setGender(gender.getText().toString());
                    newUser.setArea(live_area.getText().toString());
                    newUser.update(new Callback<Msg>() {
                        @Override
                        public void onResponse(@NonNull Call<Msg> call, @NonNull Response<Msg> response) {
                            Msg msg = response.body();
                            if (msg != null) {
                                UtilTools.toast(UserEditActivity.this, msg.getInfo());
                                if (msg.getStatus() == Msg.CORRECT_STATUS) {
                                    onBackPressed();
                                }
                            } else {
                                UtilTools.toast(UserEditActivity.this, "出错了 msg为空");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Msg> call, @NonNull Throwable t) {

                            UtilTools.toast(UserEditActivity.this, "出错了 onFailure");
                        }
                    });
                } else {
                    UtilTools.toast(this, "currentUser = null");
                }
                break;
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CHOOSE_PHOTO:
                if (resultCode == RESULT_OK && data != null) {
                    afterChoosePhoto(data);
                }
                break;
            case REQUEST_CODE_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    String imgPath = ImageUri.getPath();
                    LogUtil.d("相机path：" + imgPath);
                    uploadImg(imgPath);
                    //                    try {
                    //                        //将拍摄的照片显示出来
                    ////                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(ImageUri));
                    //                        String imgPath = ImageUri.getPath();
                    //                        LogUtil.d("相机path：" + imgPath);
                    //                        uploadImg(imgPath);
                    //                    } catch (FileNotFoundException e) {
                    //                        e.printStackTrace();
                    //                        LogUtil.d("photo:result" + e.getMessage());
                    //                    }
                }
                break;
        }
    }

    /**
     * 选择照片后的操作
     *
     * @param data data
     */
    private void afterChoosePhoto(Intent data) {
        Uri selectedImage = data.getData();
        LogUtil.d("相册uri：" + selectedImage);
        String[] filePathColumns = {MediaStore.Images.Media.DATA};
        if (selectedImage != null) {
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            if (c != null) {
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String imgPath = c.getString(columnIndex);
                LogUtil.d("相册path：" + imgPath);
                uploadImg(imgPath);
                c.close();
            }
        }
    }

    /**
     * 上传头像或背景
     *
     * @param imgPath imgPath
     */
    private void uploadImg(String imgPath) {
        User currentUser = TravelingUser.getCurrentUser();
        if (currentUser != null) {
            currentUser.uploadImg(imgPath, uploadType, new UserCallback() {
                @Override
                public void onSuccess(User user) {
                    UtilTools.toast(UserEditActivity.this, "上传成功");
                    if (uploadType == TYPE_USER_IMG) {
                        Picasso.get().load(user.getImg()).error(R.drawable.err_img_bg).fit().into(user_img);
                    } else if (uploadType == TYPE_USER_BG) {
                        Picasso.get().load(user.getBackgroundImg()).error(R.drawable.err_img_bg).fit().into(user_bg);
                    }
                }

                @Override
                public void onFiled(String info) {
                    UtilTools.toast(UserEditActivity.this, "上传失败：" + info);
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST__CODE_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //调用相册
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_CODE_CHOOSE_PHOTO);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d("UserEditActivity Destroy");
    }
}

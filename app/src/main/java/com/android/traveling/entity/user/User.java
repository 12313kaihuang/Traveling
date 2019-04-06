package com.android.traveling.entity.user;


import android.support.annotation.NonNull;

import com.android.traveling.entity.service.UploadService;
import com.android.traveling.developer.zhiming.li.ui.UserEditActivity;
import com.android.traveling.entity.msg.LoginMsg;
import com.android.traveling.entity.msg.Msg;
import com.android.traveling.entity.service.FocusService;
import com.android.traveling.entity.service.UserService;
import com.android.traveling.util.LogUtil;
import com.android.traveling.util.StaticClass;
import com.android.traveling.util.UtilTools;

import org.litepal.crud.LitePalSupport;

import java.io.File;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by HY
 * 2018/12/2 15:00
 * <p>
 * user表  存储用户信息
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class User extends LitePalSupport {

    private Integer id;

    private int userId;

    private String nickName = "Traveling用户";    //昵称

    private String password;

    private String email;

    private boolean emailVerified = false;

    private String phoneNumber;

    private boolean phoneNumberVerified = false;

    private String img = "default.png"; //头像地址

    private String backgroundImg = "defaultBg.png"; //背景地址

    private String signature;

    private String gender;  //性别

    private Date birthday;

    private String area;

    private int level = 1;

    public User() {
    }

    public User(User user) {
        this.id = user.getId();
        this.nickName = user.getNickName();
        this.password = user.password;
        this.email = user.getEmail();
        this.emailVerified = user.isEmailVerified();
        this.phoneNumber = user.getPhoneNumber();
        this.phoneNumberVerified = user.isPhoneNumberVerified();
        this.img = user.getImg();
        this.gender = user.gender;
        this.signature = user.signature;
        this.birthday = user.birthday;
        this.area = user.area;
        this.level = user.level;
        this.userId = user.userId;
    }

    public User(String phoneNumber) {
        this.phoneNumberVerified = true;
        this.phoneNumber = phoneNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(String backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * 是否有密码
     *
     * @return 是否有密码
     */
    public boolean hasPassword() {
        return password != null;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isPhoneNumberVerified() {
        return phoneNumberVerified;
    }

    public void setPhoneNumberVerified(boolean phoneNumberVerified) {
        this.phoneNumberVerified = phoneNumberVerified;
    }

    public String getImg() {
        return img;
    }

    //获取图片名 不带前面的url
    public String getDirectImg() {
        return img.replace(StaticClass.IMG_URL, "");
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId=" + userId +
                ", nickName='" + nickName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", emailVerified=" + emailVerified +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", phoneNumberVerified=" + phoneNumberVerified +
                ", img='" + img + '\'' +
                ", backgroundImg='" + backgroundImg + '\'' +
                ", signature='" + signature + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", area='" + area + '\'' +
                ", level=" + level +
                '}';
    }

    /**
     * 更新User
     *
     * @param user user
     */
    public void refresh(User user) {
        this.nickName = user.nickName;
        this.phoneNumber = user.phoneNumber;
        this.password = user.password;
        this.phoneNumberVerified = user.phoneNumberVerified;
        this.email = user.email;
        this.emailVerified = user.emailVerified;
        this.img = user.img;
        this.backgroundImg = user.getBackgroundImg();
        this.signature = user.signature;
        this.gender = user.gender;
        this.birthday = user.birthday;
        this.area = user.area;
        this.level = user.level;
        save();
    }

    /**
     * currentUser 编辑个人资料页面修改个人信息
     *
     * @param callback 回调接口callback
     */
    public void update(Callback<Msg> callback) {
        if (userId == 0) {
            LogUtil.d("User.refresh()  userId没对");
        } else {
            //创建Retrofit对象  注意url后面有一个'/'。
            Retrofit retrofit = UtilTools.getRetrofit();
            // 获取UserService对象
            UserService userService = retrofit.create(UserService.class);
            Call<Msg> update = userService.update(userId, nickName, signature, area, gender);
            update.enqueue(new Callback<Msg>() {
                @Override
                public void onResponse(@NonNull Call<Msg> call, @NonNull Response<Msg> response) {
                    Msg msg = response.body();
                    if (msg != null && msg.getStatus() == Msg.CORRECT_STATUS) {
                        save();
                    }
                    callback.onResponse(call, response);
                }

                @Override
                public void onFailure(@NonNull Call<Msg> call, @NonNull Throwable t) {
                    callback.onFailure(call, t);
                }
            });
        }
    }

    /**
     * currentUser 绑定邮箱
     *
     * @param userCallback 回调接口
     */
    public void bindEmail(String password, UserCallback userCallback) {

        //创建Retrofit对象  注意url后面有一个'/'。
        Retrofit retrofit = UtilTools.getRetrofit();
        // 获取UserService对象
        UserService userService = retrofit.create(UserService.class);
        Call<LoginMsg> call;
        if (password != null) {
            call = userService.bindEmailAndPass(userId, email, password);
        } else {
            call = userService.bindEmail(userId, email);
        }
        enqueue(userCallback, call);
    }

    /**
     * 设置新密码
     *
     * @param newPass newPass
     */
    public void changePassword(String newPass, UserCallback userCallback) {
        Call<LoginMsg> call = UtilTools.getRetrofit().create(UserService.class).setPassword(userId, newPass);
        enqueue(userCallback, call);
    }

    /**
     * 改变密码
     *
     * @param oldPass oldPass
     * @param newPass newPass
     */
    public void changePassword(String oldPass, String newPass, UserCallback userCallback) {
        Call<LoginMsg> call = UtilTools.getRetrofit().create(UserService.class).changePassword(userId, oldPass, newPass);
        enqueue(userCallback, call);
    }


    protected void enqueue(UserCallback userCallback, Call<LoginMsg> call) {
        call.enqueue(new Callback<LoginMsg>() {
            @Override
            public void onResponse(@NonNull Call<LoginMsg> call, @NonNull Response<LoginMsg> response) {
                LoginMsg body = response.body();
                if (body == null) {
                    userCallback.onFiled("body == null");
                } else {
                    if (body.getStatus() == Msg.CORRECT_STATUS) {
                        refresh(body.getUser()); //更新currentUser
                        userCallback.onSuccess(body.getUser());
                    } else {
                        userCallback.onFiled(body.getInfo());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginMsg> call, @NonNull Throwable t) {
                userCallback.onFiled("onFailure e=" + t.getMessage());
            }
        });
    }

    /**
     * 上传并保存头像或背景
     *
     * @param imgPath  图片路径
     * @param callback 回调接口
     */
    public void uploadImg(String imgPath, @UserEditActivity.UploadType int uploadType, UserCallback callback) {

        File file = new File(imgPath);
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imageBodyPart = MultipartBody.Part.createFormData("imgfile", file.getName(), imageBody);
        UtilTools.getRetrofit().create(UploadService.class).
                uploadUserImg(userId, uploadType, imageBodyPart).enqueue(new Callback<LoginMsg>() {
            @Override
            public void onResponse(@NonNull Call<LoginMsg> call, @NonNull Response<LoginMsg> response) {
                LoginMsg msg = response.body();
                if (msg == null) {
                    callback.onFiled("msg == null");
                } else {
                    if (msg.getStatus() == Msg.CORRECT_STATUS) {
                        refresh(msg.getUser());
                        callback.onSuccess(msg.getUser());
                    } else {
                        callback.onFiled(msg.getInfo());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginMsg> call, @NonNull Throwable t) {
                callback.onFiled(t.getMessage());
            }
        });

    }

    /**
     * 获取所有文章等详细信息
     *
     * @param callback 回调接口
     */
    public void getDetailInfo(DetailUserInfoCallback callback) {
        TravelingUser.getDetailUserInfo(userId, callback);
    }

    /**
     * 添加或取消关注
     *
     * @param isFocus 是否已关注  true则取消关注，false则添加关注
     * @param toId    toId
     */
    public void addOrCancelFocus(boolean isFocus, int toId) {
        FocusService focusService = UtilTools.getRetrofit().create(FocusService.class);
        Call<Msg> call;
        if (isFocus) {
            call = focusService.cancelFocus(userId, toId);
        } else {
            call = focusService.addFocus(userId, toId);
        }
        call.enqueue(new Callback<Msg>() {
            @Override
            public void onResponse(@NonNull Call<Msg> call, @NonNull Response<Msg> response) {
                Msg msg = response.body();
                if (msg == null) {
                    LogUtil.d("================addOrCancelFocus  msg == null");
                } else {
                    if (msg.isStatusCorrect()) {
                        LogUtil.d("================addOrCancelFocus 添加/取消关注成功");
                    } else {
                        LogUtil.d("================addOrCancelFocus  添加/取消关注失败：" + msg.getInfo());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Msg> call, @NonNull Throwable t) {
                LogUtil.d("================addOrCancelFocus  failure:" + t.getMessage());
            }
        });
    }


}

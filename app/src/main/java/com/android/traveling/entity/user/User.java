package com.android.traveling.entity.user;



import android.content.pm.PackageManager;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * Created by HY
 * 2018/12/2 15:00
 * <p>
 * user表  存储用户信息
 */

@SuppressWarnings("unused")
public class User extends LitePalSupport{

    private Integer id;

    private int userId;

    private String nickName = "Traveling用户";    //昵称

    private String password;

    private String email;

    private boolean emailVerified = false;

    private String phoneNumber;

    private boolean phoneNumberVerified = false;

    private String img = "default.png"; //头像地址

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
        this.password = user.getPassword();
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

    public String getPassword() {
        return password;
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
                ", signature='" + signature + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday=" + birthday +
                ", area='" + area + '\'' +
                ", level=" + level +
                '}';
    }
}

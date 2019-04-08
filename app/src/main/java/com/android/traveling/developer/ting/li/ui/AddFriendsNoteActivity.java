package com.android.traveling.developer.ting.li.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.traveling.R;
import com.android.traveling.entity.companion.BaseCompanion;
import com.android.traveling.entity.companion.Companion;
import com.android.traveling.entity.user.TravelingUser;
import com.android.traveling.entity.user.User;
import com.android.traveling.util.UtilTools;
import com.android.traveling.widget.dialog.PublishDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddFriendsNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText headlineEdit;
    private EditText contentEdit;
    private EditText destinationEdit;
    private Button timeStart;
    private Button timeEnd;
    private Button btnPublish;
    private TextView tv_cancel;

    private boolean bTitle = false;  //标题输入框是否为空
    private boolean bContent = false;  //内容输入框是否为空
    //显示时间
    private int mYear;
    private int mMonth;
    private int mDay;
    private Date dateStart;
    private Date dateEnd;

    private PublishDialog publishDialog;


    //private Button timeset;
    //id_addfriendsnote_title
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_friends);
        initView();
        setAction();
        //        Intent intent = getIntent();
        //        String name = intent.getStringExtra("type");
        //        titleName.setText(name);
        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
    }

    private void initView() {
        headlineEdit = findViewById(R.id.id_addfriendsnote_headline);
        destinationEdit = findViewById(R.id.id_addfriendsnote_destinationeditor);
        contentEdit = findViewById(R.id.id_addfriendsnote_content);
        timeStart = findViewById(R.id.id_addfriendsnote_timestart);
        timeEnd = findViewById(R.id.id_addfriendsnote_timeend);
        btnPublish = findViewById(R.id.btn_friends_publish);
        tv_cancel = findViewById(R.id.tv_cancel_friends);
        publishDialog = new PublishDialog(this);
        //        BaseCompanion baseCompanion = new BaseCompanion(2, "测试", "test", new Date(), new Date(), "ss");
        //        Companion.add(baseCompanion, new Companion.Callback() {
        //            @Override
        //            public void onSuccess(List<Companion> companions) {
        //                UtilTools.toast(AddFriendsNoteActivity.this, "success");
        //            }
        //
        //            @Override
        //            public void onFailure(int errCode, String reason) {
        //                UtilTools.toast(AddFriendsNoteActivity.this, "failure");
        //
        //            }
        //        });

        //修改hint的文字大小
        /*String headline = "标题";
        String content = "正文";
        SpannableString sHeadline =  new SpannableString(headline);
        SpannableString sContent =  new SpannableString(content);
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(25, true);
        sHeadline.setSpan(ass, 0, sHeadline.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sContent.setSpan(ass, 0, sHeadline.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        headlineEdit.setHint(new SpannedString(sHeadline));
        contentEdit.setHint(new SpannedString(sContent));*/
    }

    private void setAction() {
        timeStart.setOnClickListener(this);
        timeEnd.setOnClickListener(this);
        btnPublish.setOnClickListener(this);
        //取消
        tv_cancel.setOnClickListener(v -> finish());
        //标题输入监听
        headlineEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    bTitle = false;
                    btnPublish.setTextColor(getResources().getColor(R.color.gray_comment));
                    btnPublish.setBackgroundColor(getResources().getColor(R.color.bind_gray));
                } else {
                    bTitle = true;
                    if (bContent) {
                        btnPublish.setTextColor(getResources().getColor(R.color.white));
                        btnPublish.setBackgroundColor(getResources().getColor(R.color.blue));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //内容输入监听
        contentEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    bContent = false;
                    btnPublish.setTextColor(getResources().getColor(R.color.gray_comment));
                    btnPublish.setBackgroundColor(getResources().getColor(R.color.bind_gray));
                } else {
                    bContent = true;
                    if (bTitle) {
                        btnPublish.setTextColor(getResources().getColor(R.color.white));
                        btnPublish.setBackgroundColor(getResources().getColor(R.color.blue));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private DatePickerDialog.OnDateSetListener onDateSetListenerstart = new DatePickerDialog.OnDateSetListener() {
        @SuppressLint("SimpleDateFormat")
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            String days;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = String.valueOf(mYear) + "年" + "0" +
                            (mMonth + 1) + "月" + "0" + mDay + "日";
                } else {
                    days = String.valueOf(mYear) + "年" + "0" +
                            (mMonth + 1) + "月" + mDay + "日";
                }
            } else {
                if (mDay < 10) {
                    days = String.valueOf(mYear) + "年" +
                            (mMonth + 1) + "月" + "0" + mDay + "日";
                } else {
                    days = String.valueOf(mYear) + "年" +
                            (mMonth + 1) + "月" + mDay + "日";
                }
            }
            timeStart.setText(days);
            //SimpleDateFormat dateStart = new SimpleDateFormat("yyyy年MM月dd日");
            String format = "yyyy年MM月dd日";//日期格式
            //String curDate= "2018-05-22";//当前日期
            try {
                dateStart = new SimpleDateFormat(format).parse(days);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };
    private DatePickerDialog.OnDateSetListener onDateSetListenerend = new DatePickerDialog.OnDateSetListener() {
        @SuppressLint("SimpleDateFormat")
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            String days;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = String.valueOf(mYear) + "年" + "0" +
                            (mMonth + 1) + "月" + "0" + mDay + "日";
                } else {
                    days = String.valueOf(mYear) + "年" + "0" +
                            (mMonth + 1) + "月" + mDay + "日";
                }
            } else {
                if (mDay < 10) {
                    days = String.valueOf(mYear) + "年" +
                            (mMonth + 1) + "月" + "0" + mDay + "日";
                } else {
                    days = String.valueOf(mYear) + "年" +
                            (mMonth + 1) + "月" + mDay + "日";
                }
            }
            timeEnd.setText(days);
            String format = "yyyy年MM月dd日";//日期格式
            //String curDate= "2018-05-22";//当前日期
            try {
                dateEnd = new SimpleDateFormat(format).parse(days);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_addfriendsnote_timestart: {
                new DatePickerDialog(this, onDateSetListenerstart, mYear, mMonth, mDay).show();
                break;
            }
            case R.id.id_addfriendsnote_timeend: {
                new DatePickerDialog(this, onDateSetListenerend, mYear, mMonth, mDay).show();
                break;
            }
            case R.id.btn_friends_publish: {
                if (!TextUtils.isEmpty(headlineEdit.getText()) && !TextUtils.isEmpty(contentEdit.getText()) && !TextUtils.isEmpty(destinationEdit.getText()) && !(dateStart == null) && !(dateEnd == null)) {
                    User currentUser = TravelingUser.getCurrentUser();
                    if (currentUser == null) {
                        Toast.makeText(AddFriendsNoteActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
                    } else {
                        publishDialog.show();
                        BaseCompanion baseCompanion = new BaseCompanion(currentUser.getUserId(), headlineEdit.getText().toString(), contentEdit.getText().toString(), dateStart, dateEnd, destinationEdit.getText().toString());
                        Companion.add(baseCompanion, new Companion.Callback() {
                            @Override
                            public void onSuccess(List<Companion> companions) {
                                UtilTools.toast(AddFriendsNoteActivity.this, "success");
                                publishDialog.dismiss();
                            }

                            @Override
                            public void onFailure(int errCode, String reason) {
                                UtilTools.toast(AddFriendsNoteActivity.this, reason);
                                publishDialog.dismiss();
                            }

                        });
                        publishDialog.setOnCancelListener(dialog -> {
                            UtilTools.toast(AddFriendsNoteActivity.this, "已取消");
                            //call.cancel();
                            dialog.dismiss();
                        });
                    }
                } else {
                    UtilTools.toast(AddFriendsNoteActivity.this, "请补全信息。");
                }
            }
            default:
                break;
        }
    }
}

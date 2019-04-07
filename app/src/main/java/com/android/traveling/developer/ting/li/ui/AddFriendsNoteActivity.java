package com.android.traveling.developer.ting.li.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.entity.companion.BaseCompanion;
import com.android.traveling.entity.companion.Companion;
import com.android.traveling.util.ReflectionUtil;
import com.android.traveling.util.UtilTools;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AddFriendsNoteActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView titleSure;
    private TextView titleName;
    private LinearLayout titleBack;
    private EditText headlineEdit;
    private EditText contentEdit;
    private EditText destinationEdit;
    private Button timestart;
    private Button timeend;
    //显示时间
    private int mYear;
    private int mMonth;
    private int mDay;

    //private Button timeset;
    //id_addfriendsnote_title
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_friends);
        initView();
        setAction();
        Intent intent = getIntent();
        String name = intent.getStringExtra("type").toString();
        //        titleName.setText(name);

        Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
    }

    private void initView() {

        headlineEdit = findViewById(R.id.id_addfriendsnote_headline);
        destinationEdit = findViewById(R.id.id_addfriendsnote_destinationeditor);
        timestart = findViewById(R.id.id_addfriendsnote_timestart);
        timeend = findViewById(R.id.id_addfriendsnote_timeend);

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
        timestart.setOnClickListener(this);
        timeend.setOnClickListener(this);
    }

    private DatePickerDialog.OnDateSetListener onDateSetListenerstart = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            String days;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }
            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }
            }
            timestart.setText(days);
        }
    };
    private DatePickerDialog.OnDateSetListener onDateSetListenerend = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            String days;
            if (mMonth + 1 < 10) {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").append("0").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }
            } else {
                if (mDay < 10) {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append("0").append(mDay).append("日").toString();
                } else {
                    days = new StringBuffer().append(mYear).append("年").
                            append(mMonth + 1).append("月").append(mDay).append("日").toString();
                }
            }
            timeend.setText(days);
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
            default:
                break;
        }
    }
}

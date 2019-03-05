package com.android.traveling.developer.jiaming.liu.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.traveling.R;
/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.jiaming.liu.Activity
 * 文件名：AddNoteActivity
 * 创建者：LJM
 * 创建时间：2018/10/1 15:19
 * 描述：  编辑文本界面
 */
public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView titleSure;
    private LinearLayout titleBack;
    private TextView titleName;

    private EditText headlineEdit;
    private  EditText contentEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_note);
        initView();
        setAction();
        Intent intent = getIntent();
        String name = intent.getStringExtra("type").toString();
        titleName.setText(name);
    }

    private void initView(){
        titleSure = findViewById(R.id.id_addnote_title).findViewById(R.id.id_titlebar_addnote_sure);
        titleBack = findViewById(R.id.id_addnote_title).findViewById(R.id.id_titlebar_addnote_back);
        titleName = findViewById(R.id.id_addnote_title).findViewById(R.id.id_titlebar_addnote_name);

        headlineEdit = findViewById(R.id.id_addnote_headline);
        contentEdit  =findViewById(R.id.id_addnote_editor);

        //修改hint的文字大小
        String headline = "标题";
        String content = "正文";
        SpannableString sHeadline =  new SpannableString(headline);
        SpannableString sContent =  new SpannableString(content);
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(25, true);
        sHeadline.setSpan(ass, 0, sHeadline.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sContent.setSpan(ass, 0, sHeadline.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        headlineEdit.setHint(new SpannedString(sHeadline));
        contentEdit.setHint(new SpannedString(sContent));

    }
    private void setAction(){
        titleSure.setOnClickListener(this);
        titleBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_titlebar_addnote_back:{
                onBackPressed();
                break;
            }
            case R.id.id_titlebar_addnote_sure:{
                break;
            }
            default:break;
        }
    }
}

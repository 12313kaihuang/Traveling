package com.android.traveling.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.traveling.R;
import com.android.traveling.util.SoftKeyboardUtils;
import com.android.traveling.util.UtilTools;

/**
 * 项目名：SearchView
 * 包名：  com.searchview
 * 文件名：SearchView
 * 创建者：HY
 * 创建时间：2019/1/31 14:26
 * 描述：  自定义控件SearchView
 */

public class SearchView extends RelativeLayout implements ISearcher {

    private TextWatcher textWatcher;
    private ISearcher.onImageButtonVoiceClickListener onImageButtonVoiceClickListener;
    private ISearcher.onImageButtonCancelClickListener onImageButtonCancelClickListener;
    private ISearcher.onImageButtonSearchClickListener onImageButtonSearchClickListener;
    private ISearcher.onTextViewSearchClickListener onTextViewSearchClickListener;


    Context context;
    EditText et_input;
    ImageButton ib_voice;  //语音图标
    ImageButton ib_cancel; //清除图标
    ImageView search_ico;  //搜索图标
    TextView tv_search;
    ConstraintLayout rl_mapsearcher;
    View view;

    public SearchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        view = LayoutInflater.from(context).inflate(R.layout.layout_searchview, this);
        initTextWatcher();
        initView();
        this.context = context;
        initParams(attrs);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.layout_searchview, this);
        initTextWatcher();
        initView();
        this.context = context;
        initParams(attrs);
    }

    /**
     * 初始化自定义属性
     *
     * @param attrs attrs
     */
    private void initParams(AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchView);
        if (typedArray != null) {
            String hint = typedArray.getString(R.styleable.SearchView_hint);
            if (hint != null) {
                et_input.setHint(hint);
            }

            Drawable clear_ico = typedArray.getDrawable(R.styleable.SearchView_clear_ico);
            if (clear_ico != null) {
                ib_cancel.setBackground(clear_ico);
            }

            Drawable voice_ico = typedArray.getDrawable(R.styleable.SearchView_voice_ico);
            if (clear_ico != null) {
                ib_voice.setBackground(voice_ico);
            }

            Drawable search_ico = typedArray.getDrawable(R.styleable.SearchView_search_ico);
            if (clear_ico != null) {
                this.search_ico.setImageDrawable(search_ico);
            }
            typedArray.recycle();
        }
    }


    private void initView() {
        et_input = view.findViewById(R.id.et_input);
        ib_voice = view.findViewById(R.id.ib_voice);
        ib_cancel = view.findViewById(R.id.ib_cancle);
        search_ico = view.findViewById(R.id.iv_search);
        tv_search = view.findViewById(R.id.tv_search);
        rl_mapsearcher = view.findViewById(R.id.rl_mapsearcher);


        et_input.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                rl_mapsearcher.setSelected(true);
                if (et_input.getText().length() > 0) {
                    ib_cancel.setVisibility(View.VISIBLE);
                    tv_search.setVisibility(View.VISIBLE);
                    ib_voice.setVisibility(View.GONE);
                } else {
                    et_input.setSelected(false);
                    ib_cancel.setVisibility(View.GONE);
                    tv_search.setVisibility(View.GONE);
                    ib_voice.setVisibility(View.VISIBLE);
                }
            } else {
                et_input.setHint("请输入关键字");
            }
        });

        if (textWatcher != null) {
            et_input.addTextChangedListener(textWatcher);
        }

        ib_voice.setOnClickListener(v -> {
            if (onImageButtonVoiceClickListener != null) {
                onImageButtonVoiceClickListener.onClick(et_input, ib_voice, SearchView.this);
            }
        });

        //清空内容
        ib_cancel.setOnClickListener(v -> {
            et_input.setText("");
            if (onImageButtonCancelClickListener != null) {
                onImageButtonCancelClickListener.onClick(et_input, ib_cancel, SearchView.this);
            }
        });

        //搜索按钮点击时间
        tv_search.setOnClickListener(v -> {
            et_input.clearFocus();
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                SoftKeyboardUtils.hideSoftKeyboard(activity);
            }
            if (onTextViewSearchClickListener != null) {
                onTextViewSearchClickListener.onClick(et_input, tv_search, SearchView.this);
            }
        });

        //搜索图标点击事件
        search_ico.setOnClickListener(v -> {
            if (onImageButtonSearchClickListener != null) {
                onImageButtonSearchClickListener.onClick(et_input, search_ico, SearchView.this);
            }
        });
    }

    /**
     * 语音按钮点击事件
     *
     * @param listener listener
     */
    @Override
    public void setImageButtonVoiceClickListener(ISearcher.onImageButtonVoiceClickListener listener) {
        this.onImageButtonVoiceClickListener = listener;
    }

    /**
     * 搜索图标按钮点击事件
     *
     * @param listener 回调接口listener
     */
    @Override
    public void setImageButtonSearchClickListener(ISearcher.onImageButtonSearchClickListener listener) {
        this.onImageButtonSearchClickListener = listener;
    }


    /**
     * 清除图标点击事件
     *
     * @param listener listener
     */
    @Override
    public void setImageButtonCancelClickListener(ISearcher.onImageButtonCancelClickListener listener) {
        this.onImageButtonCancelClickListener = listener;
    }

    /**
     * “搜索”点击事件
     *
     * @param listener listener
     */
    @Override
    public void setonTextViewSearchClickListener(ISearcher.onTextViewSearchClickListener listener) {
        this.onTextViewSearchClickListener = listener;
    }


    /**
     * 获取EditText的内容
     *
     * @return EditText的内容
     */
    @Override
    public String getSearchContent() {
        return et_input.getText().toString().trim();
    }

    /**
     * 设置EditText的内容
     *
     * @param content content
     */
    @Override
    public void setSearchContent(String content) {
        et_input.setText(content);
    }

    /**
     * 获取EditText
     *
     * @return EditText
     */
    @Override
    public EditText getEt_input() {
        return et_input;
    }

    /**
     * 设置搜索图标
     *
     * @param searchIcon searchIcon
     */
    @Override
    public void setSearchIcon(Drawable searchIcon) {
        this.search_ico.setImageDrawable(searchIcon);
    }

    /**
     * 设置语音图标
     *
     * @param voiceIcon voiceIcon
     */
    @Override
    public void setVoiceIcon(Drawable voiceIcon) {
        ib_voice.setBackground(voiceIcon);
    }

    /**
     * 设置清除图标
     *
     * @param clearIcon clearIcon
     */
    @Override
    public void setClearIcon(Drawable clearIcon) {
        ib_cancel.setBackground(clearIcon);
    }

    /**
     * 初始化textWatcher
     */
    private void initTextWatcher() {
        textWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if (length > 0 && et_input.isFocused()) {
                    ib_voice.setVisibility(View.GONE);
                    ib_cancel.setVisibility(View.VISIBLE);
                    tv_search.setVisibility(View.VISIBLE);
                } else {
                    ib_cancel.setVisibility(View.GONE);
                    tv_search.setVisibility(View.GONE);
                    ib_voice.setVisibility(View.VISIBLE);
                }
            }
        };
    }

}

package com.yu.hu.traveling.ui.publish;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.yu.hu.ninegridlayout.utils.PixUtils;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.DialogTypeSelectBinding;

import java.util.Collections;
import java.util.List;

/**
 * @author Hy
 * created on 2020/04/19 17:44
 * 文章类型选择Dialog，继承自{@link BottomSheetDialogFragment},
 * 可以实现一个放大缩小效果
 **/
public class TypeSelectDialog extends BottomSheetDialogFragment {

    private Builder mBuilder;

    private TypeSelectDialog(Builder builder) {
        this.mBuilder = builder;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        DialogTypeSelectBinding binding = DialogTypeSelectBinding.inflate(LayoutInflater.from(requireContext()), null, false);
        TypeItemAdapter mAdapter = new TypeItemAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(mAdapter);
        mAdapter.submitList(mBuilder.typeList == null ? Collections.emptyList() : mBuilder.typeList);
        dialog.setContentView(binding.getRoot());

        //BottomSheetBehavior 配合使用使dialog可以在屏幕高度1/3 到2/3之间动态滑动
        ViewGroup parent = (ViewGroup) binding.getRoot().getParent();
        Log.d("TypeSelectDialog", "onCreateDialog: parent = " + parent);
        BottomSheetBehavior<ViewGroup> behavior = BottomSheetBehavior.from(parent);
        behavior.setPeekHeight(PixUtils.getScreenHeight() / 3);
        behavior.setHideable(true); //是否可以通过滑动隐藏dialog
        //设置最大高度
        ViewGroup.LayoutParams layoutParams = parent.getLayoutParams();
        layoutParams.height = PixUtils.getScreenHeight() / 3 * 2;
        parent.setLayoutParams(layoutParams);

        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    class TypeItemAdapter extends ListAdapter<String, RecyclerView.ViewHolder> {


        TypeItemAdapter() {
            super(new DiffUtil.ItemCallback<String>() {
                @Override
                public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
                    return oldItem.equals(newItem);
                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            textView.setTextSize(13);
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setClickable(true);
            textView.setFocusable(true);
            textView.setPadding(PixUtils.dp2px(16), 0, PixUtils.dp2px(16), 0);
            textView.setBackgroundResource(R.drawable.item_selector);
            textView.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.color_000));
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setLayoutParams(new RecyclerView.LayoutParams(-1, PixUtils.dp2px(45)));

            return new RecyclerView.ViewHolder(textView) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            TextView textView = (TextView) holder.itemView;
            String item = getItem(position);
            textView.setText(item);
            holder.itemView.setOnClickListener(v -> {
                if (mBuilder.listener != null) {
                    mBuilder.listener.onTypeItemSelected(item);
                    dismiss();
                }
            });
        }
    }

    public interface OnTypeItemSelectedListener {
        void onTypeItemSelected(String type);
    }

    @SuppressWarnings("WeakerAccess")
    public static class Builder {

        private List<String> typeList;

        private OnTypeItemSelectedListener listener;

        @SuppressWarnings("WeakerAccess")
        public Builder setOnTypeItemSelectedListener(OnTypeItemSelectedListener listener) {
            this.listener = listener;
            return this;
        }

        @SuppressWarnings("WeakerAccess")
        public Builder setTypeList(List<String> typeList) {
            this.typeList = typeList;
            return this;
        }

        public TypeSelectDialog build() {
            return new TypeSelectDialog(this);
        }

        public void show(FragmentManager fragmentManager) {
            build().show(fragmentManager, null);
        }
    }
}

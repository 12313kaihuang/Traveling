package com.yu.hu.traveling.ui.share;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.hu.common.dialog.BaseDialog;
import com.yu.hu.common.dialog.DialogBuilder;
import com.yu.hu.common.view.CircleImageView;
import com.yu.hu.common.view.ViewHelper;
import com.yu.hu.ninegridlayout.utils.PixUtils;
import com.yu.hu.traveling.R;
import com.yu.hu.traveling.databinding.LayoutShareDialogBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hy
 * created on 2020/04/09 10:28
 * <p>
 * 分享Dialog，可分享至QQ，微信
 **/
@SuppressWarnings("unused")
public class ShareDialog extends BaseDialog<ShareDialog, LayoutShareDialogBinding> {

    private Builder mBuilder;

    private ShareAdapter shareAdapter;

    private List<ResolveInfo> shareItems = new ArrayList<>();

    private ShareDialog(Builder builder) {
        setGravity(Gravity.BOTTOM);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.mBuilder = builder;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDataBinding.rootView.setViewOutLine(PixUtils.dp2px(20), ViewHelper.RADIUS_TOP);

        shareAdapter = new ShareAdapter();
        RecyclerView recyclerView = mDataBinding.recyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recyclerView.setAdapter(shareAdapter);

        queryShareItems();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_share_dialog;
    }

    //查询分享入口--是否安装有QQ，微信等
    private void queryShareItems() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");

        List<ResolveInfo> resolveInfos = requireContext().getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {

            String packageName = resolveInfo.activityInfo.packageName;
            //过滤出qq 或 微信
            if (TextUtils.equals(packageName, "com.tencent.mm") || TextUtils.equals(packageName, "com.tencent.mobileqq")) {
                shareItems.add(resolveInfo);
            }
        }
        shareAdapter.notifyDataSetChanged();
    }

    private class ShareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final PackageManager packageManager;

        ShareAdapter() {
            packageManager = requireContext().getPackageManager();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.item_share, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ResolveInfo resolveInfo = shareItems.get(position);
            CircleImageView imageView = holder.itemView.findViewById(R.id.share_icon);
            Drawable drawable = resolveInfo.loadIcon(packageManager);
            imageView.setImageDrawable(drawable);

            TextView shareText = holder.itemView.findViewById(R.id.share_text);
            shareText.setText(resolveInfo.loadLabel(packageManager));

            //item点击事件
            holder.itemView.setOnClickListener(v -> {
                String pkg = resolveInfo.activityInfo.packageName;
                String cls = resolveInfo.activityInfo.name;
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setComponent(new ComponentName(pkg, cls));
                intent.putExtra(Intent.EXTRA_TEXT, mBuilder.shareContent);

                requireContext().startActivity(intent);

                if (mBuilder.listener != null) {
                    mBuilder.listener.onClick(v);
                }

                dismiss();
            });
        }

        @Override
        public int getItemCount() {
            return shareItems == null ? 0 : shareItems.size();
        }
    }

    public static class Builder extends DialogBuilder<ShareDialog> {

        private String shareContent;
        private View.OnClickListener listener;

        public Builder setShareContent(String shareContent) {
            this.shareContent = shareContent;
            return this;
        }

        public Builder setShareItemClickListener(View.OnClickListener listener) {
            this.listener = listener;
            return this;
        }

        @Override
        public ShareDialog build() {
            return new ShareDialog(this);
        }
    }
}

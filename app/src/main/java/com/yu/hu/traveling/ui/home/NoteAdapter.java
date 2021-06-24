package com.yu.hu.traveling.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.yu.hu.common.binding.BindingViewHolder;
import com.yu.hu.common.paging.AbsPagedListAdapter;
import com.yu.hu.ninegridlayout.OnGridItemClickListener;
import com.yu.hu.traveling.databinding.ItemNoteBinding;
import com.yu.hu.traveling.model.Note;



/**
 * @author Hy
 * created on 2020/04/17 16:54
 **/
public class NoteAdapter extends AbsPagedListAdapter<Note, NoteAdapter.ViewHolder> {

    private OnNoteItemClickListener itemClickListener;

    private OnGridItemClickListener gridItemClickListener;

    @SuppressWarnings("WeakerAccess")
    public NoteAdapter(OnNoteItemClickListener listener) {
        super(new DiffUtil.ItemCallback<Note>() {
            @Override
            public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                return oldItem.noteId == newItem.noteId;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.itemClickListener = listener;
    }

    @SuppressWarnings("WeakerAccess")
    public void setGridItemClickListener(OnGridItemClickListener gridItemClickListener) {
        this.gridItemClickListener = gridItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder2(@NonNull ViewGroup parent, int viewType) {
        ItemNoteBinding binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder2(@NonNull ViewHolder holder, int position) {
        Note note = getItem(position);
        holder.bind(note);
        holder.itemView.setOnClickListener(v -> itemClickListener.onItemClicked(note));
    }

    class ViewHolder extends BindingViewHolder<Note, ItemNoteBinding> {

        ViewHolder(@NonNull ItemNoteBinding dataBinding) {
            super(dataBinding);
        }

        @Override
        public void bind(Note note) {
            mDataBinding.setNote(note);
            mDataBinding.imgGridView.setItems(note.getImgItems());
            mDataBinding.imgGridView.setOnItemClickListener(gridItemClickListener);
            mDataBinding.interactionLayout.comment.setOnClickListener(v -> itemView.callOnClick());
        }
    }
}

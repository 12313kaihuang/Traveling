package com.yu.hu.traveling.ui.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DiffUtil;

import com.yu.hu.common.binding.BindingViewHolder;
import com.yu.hu.common.paging.AbsPagedListAdapter;
import com.yu.hu.common.utils.LogUtil;
import com.yu.hu.traveling.databinding.ItemSearchResultBinding;
import com.yu.hu.traveling.model.Note;
import com.yu.hu.traveling.ui.home.OnNoteItemClickListener;
import com.yu.hu.traveling.utils.StringConvert;



/**
 * @author Hy
 * created on 2020/04/20 21:32
 **/
public class SearchResultAdapter extends AbsPagedListAdapter<Note, SearchResultAdapter.ViewHolder> {


    private String searchContent;
    private String savedContent;
    private OnNoteItemClickListener noteItemClickListener;

    @SuppressWarnings("WeakerAccess")
    public void setonNoteItemClickListener(OnNoteItemClickListener listener) {
        this.noteItemClickListener = listener;
    }

    @SuppressWarnings("WeakerAccess")
    public SearchResultAdapter() {
        super(new DiffUtil.ItemCallback<Note>() {
            @Override
            public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                return oldItem.noteId == newItem.noteId;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                /*
                 * eg：第一次搜索x收到id为1的item，再次搜索y结果中同样有id为1的item，
                 * 如果此时还通过equals去判断是否相同，则item不会刷新高亮的关键还是会高亮第一次的搜索关键词
                 */
                return false;
            }
        });
    }

    @Override
    public void submitList(PagedList<Note> pagedList) {
        LogUtil.d("submitList size = " + pagedList.size());
        super.submitList(pagedList);
    }

    @Override
    public ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        ItemSearchResultBinding binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder2(ViewHolder holder, int position) {
        Note item = getItem(position);
        holder.bind(item);
        holder.itemView.setOnClickListener(v -> {
            if (noteItemClickListener != null) {
                noteItemClickListener.onItemClicked(item);
            }
        });
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }

    class ViewHolder extends BindingViewHolder<Note, ItemSearchResultBinding> {

        ViewHolder(@NonNull ItemSearchResultBinding dataBinding) {
            super(dataBinding);
        }

        @Override
        public void bind(Note note) {
            mDataBinding.setNote(note);
            LogUtil.d("titile = " + note.title + ", content = " + note.content);
            mDataBinding.setCover(StringConvert.convertNoteFirstCover(note));
            mDataBinding.setUrl(StringConvert.convertNoteFirstUrl(note));
            mDataBinding.setSearchContent(searchContent);
        }
    }
}

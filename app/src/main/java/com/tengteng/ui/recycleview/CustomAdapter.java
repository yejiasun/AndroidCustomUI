package com.tengteng.ui.recycleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tengteng.ui.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author yejiasun
 * @date Create on 12/14/22
 */

class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.TTViewHolder> {
    private Context context;
    private List<CustomItemBean> list;

    public CustomAdapter(Context mContext, List<CustomItemBean> mList) {
        this.context = mContext;
        this.list = mList;
    }

    @NonNull
    @Override
    public TTViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycleview, null);
        return new TTViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TTViewHolder holder, int position) {
        CustomItemBean bean = list.get(position);
        holder.tv.setText(bean.getContent());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    /**
     * 当前position是不是groupHead
     *
     * @param position
     * @return
     */
    public boolean isGroupHead(int position) {
        if (position == 0) {
            return true;
        } else {
            return !getGroupName(position).equals(getGroupName(position - 1));
        }
    }

    public String getGroupName(int position) {
        return list == null ? "" : list.get(position).getGroupName();
    }

    /**
     * item的样式适配器
     */
    public class TTViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public TTViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_item);
        }
    }
}

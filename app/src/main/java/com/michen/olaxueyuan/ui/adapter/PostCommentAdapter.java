package com.michen.olaxueyuan.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.michen.olaxueyuan.R;
import com.michen.olaxueyuan.app.SEConfig;
import com.michen.olaxueyuan.common.RoundRectImageView;
import com.michen.olaxueyuan.common.manager.Utils;
import com.michen.olaxueyuan.protocol.result.CommentModule;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mingge on 2016/7/14.
 */
public class PostCommentAdapter extends BaseAdapter {
    private Context mContext;
    private List<CommentModule.ResultBean> list = new ArrayList<>();

    public PostCommentAdapter(Context context) {
        mContext = context;
    }

    public void upDateData(List<CommentModule.ResultBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_comment, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.itemCommentName.setText(list.get(position).getUserName());
        holder.itemCommentLocation.setText("@" + list.get(position).getLocation());
        holder.itemCommentOriginalContent.setText(list.get(position).getContent());
        holder.itemCommentTime.setText(list.get(position).getTime());
        Picasso.with(mContext).load(SEConfig.getInstance().getAPIBaseURL() + "/upload/" + list.get(position).getUserAvatar()).placeholder(R.drawable.ic_default_avatar)
                .error(R.drawable.ic_default_avatar).resize(Utils.dip2px(mContext, 50), Utils.dip2px(mContext, 50)).into(holder.itemCommentAvatar);
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.item_comment_avatar)
        RoundRectImageView itemCommentAvatar;
        @Bind(R.id.item_comment_name)
        TextView itemCommentName;
        @Bind(R.id.item_comment_originalContent)
        TextView itemCommentOriginalContent;
        @Bind(R.id.item_comment_time)
        TextView itemCommentTime;
        @Bind(R.id.item_comment_location)
        TextView itemCommentLocation;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

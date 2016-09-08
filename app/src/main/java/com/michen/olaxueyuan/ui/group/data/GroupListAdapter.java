package com.michen.olaxueyuan.ui.group.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.michen.olaxueyuan.R;
import com.michen.olaxueyuan.common.RoundRectImageView;
import com.michen.olaxueyuan.protocol.result.UserGroupListResult;
import com.michen.olaxueyuan.ui.circle.PostDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mingge on 16/9/7.
 */
public class GroupListAdapter extends BaseAdapter {
    private Context mContext;
    List<UserGroupListResult.ResultEntity> list = new ArrayList<>();

    public GroupListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void updateData(List<UserGroupListResult.ResultEntity> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.grouplist_fragment_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            holder.avatar.setRectAdius(100);
            Picasso.with(mContext).load(list.get(position).getAvatar()).config(Bitmap.Config.RGB_565)
                    .placeholder(R.drawable.default_index).error(R.drawable.ic_default_avatar).into(holder.avatar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.title.setText(list.get(position).getName());
        holder.memberCount.setText(list.get(position).getNumber() + "人");
        holder.introduceText.setText(list.get(position).getProfile());
        if (list.get(position).getIsMember() == 0) {
            holder.joinGroup.setText("关注");
        } else {
            holder.joinGroup.setText("取消关注");
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, PostDetailActivity.class);//
                intent.putExtra("circleId", list.get(position).getId());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.avatar)
        RoundRectImageView avatar;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.member_count)
        TextView memberCount;
        @Bind(R.id.introduce_text)
        TextView introduceText;
        @Bind(R.id.join_group)
        Button joinGroup;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

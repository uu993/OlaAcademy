package com.michen.olaxueyuan.ui.circle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.michen.olaxueyuan.R;
import com.michen.olaxueyuan.app.SEAPP;
import com.michen.olaxueyuan.common.NoScrollGridAdapter;
import com.michen.olaxueyuan.common.RoundRectImageView;
import com.michen.olaxueyuan.common.SubListView;
import com.michen.olaxueyuan.common.manager.Logger;
import com.michen.olaxueyuan.common.manager.ToastUtil;
import com.michen.olaxueyuan.protocol.manager.QuestionCourseManager;
import com.michen.olaxueyuan.protocol.result.CommentModule;
import com.michen.olaxueyuan.protocol.result.OLaCircleModule;
import com.michen.olaxueyuan.ui.activity.SEBaseActivity;
import com.michen.olaxueyuan.ui.adapter.PostCommentAdapter;
import com.michen.olaxueyuan.ui.story.activity.ImagePagerActivity;
import com.snail.photo.util.NoScrollGridView;
import com.snail.svprogresshud.SVProgressHUD;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PostDetailActivity extends SEBaseActivity {
    @Bind(R.id.avatar)
    RoundRectImageView avatar;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.address)
    TextView address;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.study_name)
    TextView studyName;
    @Bind(R.id.gridview)
    NoScrollGridView gridview;
    @Bind(R.id.comment_comment)
    TextView commentComment;
    @Bind(R.id.comment_empty)
    TextView commentEmpty;
    @Bind(R.id.comment_list)
    SubListView listView;

    OLaCircleModule.ResultBean resultBean;
    PostCommentAdapter commentAdapter;
    @Bind(R.id.comment_praise)
    TextView commentPraise;
    @Bind(R.id.comment)
    ImageView comment;
    @Bind(R.id.share)
    ImageView share;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.bt_send)
    Button btSend;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);
        mContext = this;
        setTitleText("欧拉分享详情");
        initView();
        getCommentListData();
    }

    private void initView() {
        resultBean = (OLaCircleModule.ResultBean) getIntent().getSerializableExtra("OLaCircleModule.ResultBean");
        final int type = resultBean.getType();
        avatar.setRectAdius(100);
        title.setText(resultBean.getUserName());
        //缺一个头像
//        if (!TextUtils.isEmpty(resultBean.getUserAvatar())) {
//            Picasso.with(mContext).load(SEConfig.getInstance().getAPIBaseURL() + "/upload/" + resultBean.getUserAvatar()).placeholder(R.drawable.ic_default_avatar)
//                    .error(R.drawable.ic_default_avatar).resize(Utils.dip2px(mContext, 50), Utils.dip2px(mContext, 50)).into(avatar);
//        } else {
        avatar.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_default_avatar));
//        }
        time.setText(resultBean.getTime());
        studyName.setText(resultBean.getContent());
        commentPraise.setText(resultBean.getPraiseNumber());
        if (!TextUtils.isEmpty(resultBean.getLocation())) {
            address.setText("@" + resultBean.getLocation());
        } else {
            address.setText("");
        }
        if (!TextUtils.isEmpty(resultBean.getImageGids())) {
            ArrayList<String> imageUrls = getListFromString(resultBean.getImageGids());
            final ArrayList<String> imageList = imageUrls;
            if (imageUrls.size() == 1) {
                gridview.setNumColumns(1);
            } else {
                gridview.setNumColumns(3);
            }
            gridview.setAdapter(new NoScrollGridAdapter(mContext, imageUrls));
            // 点击回帖九宫格，查看大图
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    imageBrower(position, imageList);
                }
            });
        } else {
            gridview.setVisibility(View.GONE);
        }

        commentAdapter = new PostCommentAdapter(mContext);
        listView.setAdapter(commentAdapter);
    }

    private void getCommentListData() {
        SVProgressHUD.showInView(mContext, getString(R.string.request_running), true);
        QuestionCourseManager.getInstance().getCommentList(String.valueOf(resultBean.getCircleId()), "2", new Callback<CommentModule>() {
            @Override
            public void success(CommentModule commentModule, Response response) {
                Logger.json(commentModule);
                SVProgressHUD.dismiss(mContext);
                if (commentModule.getApicode() != 10000) {
                    SVProgressHUD.showInViewWithoutIndicator(mContext, commentModule.getMessage(), 2.0f);
                } else {
                    commentAdapter.upDateData(commentModule.getResult());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                SVProgressHUD.dismiss(mContext);
            }
        });
    }

    private ArrayList<String> getListFromString(String images) {
        ArrayList imageUrlList = new ArrayList();
        String[] imageUrlArray = images.split(",");
        for (String imgUrl : imageUrlArray) {
            imageUrlList.add(SEAPP.PIC_BASE_URL + imgUrl);
        }
        return imageUrlList;
    }

    /**
     * 打开图片查看器
     *
     * @param position
     * @param urls2
     */
    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
    }

    @OnClick({R.id.comment_praise, R.id.comment, R.id.share, R.id.bt_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.comment_praise:
                ToastUtil.showToastShort(mContext, "点赞");
                break;
            case R.id.comment:
                ToastUtil.showToastShort(mContext, "评论");
                break;
            case R.id.share:
                ToastUtil.showToastShort(mContext, "分享");
                break;
            case R.id.bt_send:
                ToastUtil.showToastShort(mContext, "发送");
                break;
            default:
                break;
        }
    }
}

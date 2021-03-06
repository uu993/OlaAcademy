package com.michen.olaxueyuan.ui.course.turtor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.michen.olaxueyuan.R;
import com.michen.olaxueyuan.protocol.manager.MCOrgManager;
import com.michen.olaxueyuan.protocol.manager.SEAuthManager;
import com.michen.olaxueyuan.protocol.model.MCOrgInfo;
import com.michen.olaxueyuan.protocol.result.MCOrgListResult;
import com.michen.olaxueyuan.protocol.result.UserLoginNoticeModule;
import com.michen.olaxueyuan.ui.activity.SEBaseActivity;
import com.snail.pulltorefresh.PullToRefreshBase;
import com.snail.pulltorefresh.PullToRefreshListView;
import com.snail.svprogresshud.SVProgressHUD;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 名师辅导页面
 */
public class TurtorActivity extends SEBaseActivity {

    private PullToRefreshListView orgListView;
    private OrgInfoAdapter adapter;
    private ArrayList<MCOrgInfo> orgArrayList;

    public final static String ORG_KEY = "cn.swiftacademy.organization";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turtor);

        setTitleText("名师辅导");

        orgListView = (PullToRefreshListView) findViewById(R.id.orgListView);
        initData();
        orgListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });

        orgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TurtorActivity.this, OrganizationInfoActivity.class);
                MCOrgInfo orgInfo = orgArrayList.get((int) id);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ORG_KEY, orgInfo);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        EventBus.getDefault().register(this);
    }

    // EventBus 回调
    public void onEventMainThread(UserLoginNoticeModule module) {
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        MCOrgManager orgManager = MCOrgManager.getInstance();
        String userId = "";
        SEAuthManager am = SEAuthManager.getInstance();
        if (am.isAuthenticated()) {
            userId = am.getAccessUser().getId();
        }
        orgManager.fetchOrganizationList(userId, new Callback<MCOrgListResult>() {
            @Override
            public void success(MCOrgListResult result, Response response) {
                if (!TurtorActivity.this.isFinishing()) {
                    if (!result.apicode.equals("10000")) {
                        SVProgressHUD.showInViewWithoutIndicator(TurtorActivity.this, result.message, 2.0f);
                    } else {
                        orgArrayList = result.orgList;
                        adapter = new OrgInfoAdapter(TurtorActivity.this, orgArrayList);
                        orgListView.setAdapter(adapter);
                    }
                    orgListView.onRefreshComplete();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (!TurtorActivity.this.isFinishing()) {
                    orgListView.onRefreshComplete();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            initData();
        }
    }
}

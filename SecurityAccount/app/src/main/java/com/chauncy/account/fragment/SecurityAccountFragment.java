package com.chauncy.account.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.chauncy.account.R;
import com.chauncy.account.common.recyclerview.MultiItemViewAdapter;
import com.chauncy.account.delegate.AccountDetailWidgetDelegate;
import com.chauncy.account.delegate.AssetWidgetDelegate;
import com.chauncy.account.delegate.FunctionWidgetDelegate;
import com.chauncy.account.delegate.PositionOrderWidgetDelegate;
import com.chauncy.account.model.AccountDataCenter;
import com.chauncy.account.model.bean.SecurityAccount;
import com.chauncy.account.utils.AccountConstant;
import com.chauncy.account.model.AccountBaseModel;

import java.util.ArrayList;
import java.util.List;

public class SecurityAccountFragment extends Fragment {
    private String mAccountType;
    private SecurityAccount mAccount;
    private SecurityAccountAdapter mAccountAdapter;
    private List<AccountBaseModel> mBaseModels;
    private boolean isInited = false;
    private View root;
    private SwipeRefreshLayout mSwipeRefreshlayout;

    private SecurityAccountFragment(String accountType) {
        mAccountType = accountType;
    }

    public static Fragment newInstance(String accountType) {
        return new SecurityAccountFragment(accountType);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccount = AccountDataCenter.get().getDefaultAccount(mAccountType);
        mBaseModels = initWidgetDelegate();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (!isInited) {
            isInited = true;
            root = inflater.inflate(R.layout.security_account_layout, container, false);
            Context context = inflater.getContext();
            mSwipeRefreshlayout = root.findViewById(R.id.swipeRefreshLayout);
            mSwipeRefreshlayout.setDistanceToTriggerSync(100);// 设置手指在屏幕下拉多少距离会触发下拉刷新
            mSwipeRefreshlayout.setSize(SwipeRefreshLayout.LARGE);
            mSwipeRefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if(mAccount.getAccountID()==AccountConstant.ACCOUNT_ID_DEFAULT_VALUE){
                        SecurityAccount info = AccountDataCenter.get().getDefaultAccount(mAccountType);
                        mAccount.setAccountID(info.getAccountID());
                    }
                    refreshData();
                    mSwipeRefreshlayout.setRefreshing(false);
                }
            });
            RecyclerView mRecyclerView = root.findViewById(R.id.security_account_recyclerView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mAccountAdapter = new SecurityAccountAdapter(context, mBaseModels);
            mRecyclerView.setAdapter(mAccountAdapter);
        } else {
            refreshData();
        }
        return root;
    }


    private List<AccountBaseModel> initWidgetDelegate() {
        List<AccountBaseModel> data = new ArrayList<>();
        data.add(new AccountBaseModel(mAccount, AccountConstant.ASSET_WIDGET));
        data.add(new AccountBaseModel(mAccount, AccountConstant.ASSET_DETAIL_WIDGET));
        data.add(new AccountBaseModel(mAccount, AccountConstant.FUNCTION_WIDGET));
        data.add(new AccountBaseModel(mAccount, AccountConstant.POSITION_ORDER_WIDGET));
        return data;
    }

    public void refreshData() {
        mAccountAdapter.notifyDataSetChanged();
    }


    private class SecurityAccountAdapter extends MultiItemViewAdapter<AccountBaseModel> {
        public SecurityAccountAdapter(Context context, List<AccountBaseModel> data) {
            super(context, data);
            addItemViewDelegate(new AssetWidgetDelegate());
            addItemViewDelegate(new AccountDetailWidgetDelegate());
            addItemViewDelegate(new FunctionWidgetDelegate());
            addItemViewDelegate(new PositionOrderWidgetDelegate());
        }
    }
}

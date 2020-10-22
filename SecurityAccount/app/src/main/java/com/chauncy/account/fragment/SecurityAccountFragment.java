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
    private SecurityAccountAdapter mAccountAdapter;
    private List<AccountBaseModel> mBaseModels;
    private boolean isInited=false;
    private View root;

    private SecurityAccountFragment(String accountType) {
        mAccountType = accountType;
        mBaseModels=initWidgetDelegate();
    }

    public static Fragment newInstance(String accountType) {
        return new SecurityAccountFragment(accountType);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
           if(!isInited)
           {
               isInited=true;
               root = inflater.inflate(R.layout.security_account_layout, container, false);
               Context context = inflater.getContext();
               RecyclerView mRecyclerView = root.findViewById(R.id.security_account_recyclerView);
               LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
               mRecyclerView.setLayoutManager(linearLayoutManager);
               mAccountAdapter = new SecurityAccountAdapter(context, mBaseModels);
               mRecyclerView.setAdapter(mAccountAdapter);
           }else {
              refreshData();
           }
           return root;
    }



    private List<AccountBaseModel> initWidgetDelegate() {
        List<AccountBaseModel> data = new ArrayList<>();
        SecurityAccount account = new SecurityAccount(mAccountType);
        data.add(new AccountBaseModel(account, AccountConstant.ASSET_WIDGET));
        data.add(new AccountBaseModel(account, AccountConstant.ASSET_DETAIL_WIDGET));
        data.add(new AccountBaseModel(account, AccountConstant.FUNCTION_WIDGET));
        data.add(new AccountBaseModel(account, AccountConstant.POSITION_ORDER_WIDGET));
        return data;
    }

    public void refreshData() {
        mAccountAdapter.notifyDataSetChanged();
    }


    private  class SecurityAccountAdapter extends MultiItemViewAdapter<AccountBaseModel> {
        public SecurityAccountAdapter(Context context, List<AccountBaseModel> data) {
            super(context, data);
            addItemViewDelegate(new AssetWidgetDelegate());
            addItemViewDelegate(new AccountDetailWidgetDelegate());
            addItemViewDelegate(new FunctionWidgetDelegate());
            addItemViewDelegate(new PositionOrderWidgetDelegate());
        }
    }
}

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
import com.chauncy.account.common.recyclerview.ViewHolder;
import com.chauncy.account.delegate.AccountDetailWidgetDelegate;
import com.chauncy.account.delegate.AssetWidgetDelegate;
import com.chauncy.account.delegate.FunctionWidgetDelegate;
import com.chauncy.account.delegate.PositionOrderWidgetDelegate;
import com.chauncy.account.model.SecurityAccount;
import com.chauncy.account.utils.AccountConstant;
import com.chauncy.account.model.AccountBaseModel;

import java.util.ArrayList;
import java.util.List;

public class SecuritiesAccountFragment extends Fragment {
    private String mAccountType;
    private SecuritiesAccountAdapter mAccountAdapter;
    private List<AccountBaseModel> mBaseModels;

    private SecuritiesAccountFragment(String accountType) {
        mAccountType = accountType;
        mBaseModels=initWidgetDelegate();
    }

    public static Fragment newInstance(String accountType) {
        return new SecuritiesAccountFragment(accountType);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.security_account_layout, container, false);
           Context context = inflater.getContext();
           RecyclerView mRecyclerView = view.findViewById(R.id.security_account_recyclerView);
           LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
           mRecyclerView.setLayoutManager(linearLayoutManager);
           mAccountAdapter = new SecuritiesAccountAdapter(context, mBaseModels);
           mRecyclerView.setAdapter(mAccountAdapter);
           return view;
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


    private  class SecuritiesAccountAdapter extends MultiItemViewAdapter<AccountBaseModel> {
        private ViewHolder mViewHolder;
        public SecuritiesAccountAdapter(Context context, List<AccountBaseModel> data) {
            super(context, data);
            addItemViewDelegate(new AssetWidgetDelegate());
            addItemViewDelegate(new AccountDetailWidgetDelegate());
            addItemViewDelegate(new FunctionWidgetDelegate());
            addItemViewDelegate(new PositionOrderWidgetDelegate());
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position)
        {
            super.onBindViewHolder(holder, position);
            mViewHolder=holder;
        }
    }
}

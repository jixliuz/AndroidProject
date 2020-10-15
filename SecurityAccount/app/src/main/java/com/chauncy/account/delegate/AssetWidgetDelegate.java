package com.chauncy.account.delegate;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.chauncy.account.R;
import com.chauncy.account.common.event.AccountEventHandler;
import com.chauncy.account.common.event.Subject;
import com.chauncy.account.model.AccountDataCenter;
import com.chauncy.account.model.AccountInfo;
import com.chauncy.account.model.SecurityAccount;
import com.chauncy.account.share.ShareConstant;
import com.chauncy.account.share.ShareType;
import com.chauncy.account.utils.AccountConstant;
import com.chauncy.account.model.AccountBaseModel;
import com.chauncy.account.common.recyclerview.ItemViewDelegate;
import com.chauncy.account.common.recyclerview.ViewHolder;
import com.chauncy.account.utils.AccountHelper;
import com.chauncy.account.utils.SettingInfoManager;
import com.chauncy.account.utils.Global;
import com.chauncy.account.utils.NumberFormat;

import java.util.List;


public class AssetWidgetDelegate implements ItemViewDelegate<AccountBaseModel> {


    private boolean isShow;
    private ViewHolder mViewHolder;
    private AccountBaseModel mBaseModel;
    private SecurityAccount mAccount;
    private AccountInfo mAccountInfo;
    private Subject eventHandler;

    private SpannerAdapter adapter = null;
    private Spinner mSpinner = null;

    private boolean isInited = false;

    @Override
    public int getItemViewLayoutId() {
        return R.layout.asset_widget_layout;
    }

    @Override
    public boolean isForViewType(AccountBaseModel item, int position) {
        return item.getWidgetType() == AccountConstant.ASSET_WIDGET;
    }

    @Override
    public void convert(ViewHolder holder, AccountBaseModel accountBaseModel, int position) {
        mViewHolder = holder;
        mBaseModel = accountBaseModel;
        mAccount = mBaseModel.getAccount();
        SecurityAccount defaultAccount = AccountDataCenter.get().getDefaultAccount(mAccount.getAccountType());
        mAccount.setAccountID(defaultAccount.getAccountID());
        if (!isInited) {
            isInited = true;
            eventHandler = AccountEventHandler.get().createSubject();
            initUI();
        } else {
            refreshUI();
        }
    }


    private void initUI() {
        //初始化图标
        initIcon();
        //初始化下拉框数据
        initSpinnerList();
        //初始化资产信息
        initAssetInfo();
    }

    private void refreshUI() {
        refreshIcon();
        refreshAssetInfo();
        refreshSpinnerList();
    }

    private void refreshSpinnerList() {
        List<SecurityAccount> data = AccountDataCenter.get().getAllAccount(mAccount.getAccountType());
        adapter.refreshData(data);
    }

    private void initAssetInfo() {
        mViewHolder.setText(R.id.asset_title, AccountHelper.getAssetTitle(mAccount.getAccountType()));
        refreshAssetInfo();
    }

    private void initIcon() {
        TextView hiddenView = mViewHolder.getView(R.id.asset_title);
        isShow = SettingInfoManager.getInfoShowStatus();
        Drawable drawable;
        if (isShow) {
            drawable = Global.getDrawable(R.drawable.icon_show_flat);
        } else {
            drawable = Global.getDrawable(R.drawable.icon_hide_flat);
        }
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        hiddenView.setCompoundDrawables(drawable, null, null, null);

        hiddenView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShow = !isShow;
                SettingInfoManager.saveInfoShowStatus(isShow);
                refreshUI();

                //通知其他模块
                mAccount.setInfoShowStatus(isShow);
                mAccount.setEventType(SecurityAccount.IAction.INFO_STATE_CHANGE);
                eventHandler.notifyDataChange(mAccount);
            }
        });

        mViewHolder.setOnListener(R.id.today_profit_title, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString(ShareConstant.KEY_SHARE_ACCOUNT_ID,mAccountInfo.getAccountId());
                bundle.putString(ShareConstant.KEY_SHARE_ACCOUNT_TYPE,mAccountInfo.getAccountType());
                bundle.putInt(ShareConstant.KEY_SHARE_TYPE, ShareType.SHARE_ASSET.ordinal());
                bundle.putDouble(ShareConstant.KEY_SHARE_PROFIT,mAccountInfo.getTodayProfit());
                bundle.putDouble(ShareConstant.KEY_SHARE_PROFIT_RATIO,mAccountInfo.getTodayProfitRatio());


                Intent intent=new Intent();
                intent.setAction("com.chauncy.nntrade.share_activity");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtras(bundle);
                Global.getContext().startActivity(intent);
            }
        });
    }

    private void refreshAssetInfo() {
        TextView asset=mViewHolder.getView(R.id.asset);
        TextView todayProfit=mViewHolder.getView(R.id.today_profit);
        TextView profitRatio=mViewHolder.getView(R.id.today_profit_ratio);
        isShow=SettingInfoManager.getInfoShowStatus();
        if (isShow) {
             mAccountInfo = AccountDataCenter.get().getAccountInfo(mAccount.getAccountID());
            if (mAccountInfo != null) {
                asset.setText(NumberFormat.parseToString(mAccountInfo.getAsset()));
                Global.setTextAndColor(todayProfit, mAccountInfo.getTodayProfit(),false);
                Global.setTextAndColor(profitRatio, mAccountInfo.getTodayProfitRatio(),true);
            }
        } else {
            asset.setText("*****");
            todayProfit.setText("*****");
            Global.resetColor(todayProfit);
            Global.resetColor(profitRatio);
            profitRatio.setText("*****");
        }
    }

    private void initSpinnerList() {
        List<SecurityAccount> accountBaseModels = AccountDataCenter.get().getAllAccount(mBaseModel.getAccountType());
        mSpinner = mViewHolder.getView(R.id.security_account_spinner);
        adapter = new SpannerAdapter(mViewHolder.getContext(), accountBaseModels);
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(0, false);
        mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSpinner.setSelection(position);

                /**
                 * 通知其他模块账户更改
                 */
                mAccount.setAccountID(adapter.getAccountID(position));
                mAccount.setEventType(SecurityAccount.IAction.ACCOUNT_CHANGE);
                eventHandler.notifyDataChange(mAccount);
                refreshAssetInfo();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void refreshIcon() {
        Context context = mViewHolder.getContext();
        TextView textView = mViewHolder.getView(R.id.asset_title);
        isShow=SettingInfoManager.getInfoShowStatus();
        if (isShow) {
            Drawable drawable = context.getResources().getDrawable(R.drawable.icon_show_flat);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            textView.setCompoundDrawables(drawable, null, null, null);
        } else {
            Drawable drawable = context.getResources().getDrawable(R.drawable.icon_hide_flat);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            textView.setCompoundDrawables(drawable, null, null, null);
        }
    }


    private static class SpannerAdapter extends BaseAdapter {
        private List<SecurityAccount> mData;
        private Context mContext;

        public SpannerAdapter(Context context, List<SecurityAccount> data) {
            mData = data;
            mContext = context;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.icon_text_item_layout, parent, false);
            }

            SecurityAccount account = mData.get(position);
            TextView spinnerItem = convertView.findViewById(R.id.item_img_text);

            Drawable icon = getIcon(account.getAccountType());
            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
            spinnerItem.setText(AccountHelper.getAccountFullName(account.getAccountID()));
            spinnerItem.setCompoundDrawables(icon, null, null, null);

            return convertView;
        }

        public void refreshData(List<SecurityAccount> data) {
            mData.clear();
            if (data != null) {
                mData.addAll(data);
            }
            notifyDataSetChanged();
        }


        private Drawable getIcon(String accountType) {
            int id;
            switch (accountType) {
                default:
                case AccountConstant.ACCOUNT_TYPE_HK:
                    id = R.drawable.icon_account_type_hk;
                    break;
                case AccountConstant.ACCOUNT_TYPE_USA:
                    id = R.drawable.icon_account_type_us;
                    break;
                case AccountConstant.ACCOUNT_TYPE_CN:
                    id = R.drawable.icon_account_type_cn;
                    break;
            }
            return Global.getDrawable(id);
        }


        public String getAccountID(int position) {
            return mData.get(position).getAccountID();
        }
    }
}

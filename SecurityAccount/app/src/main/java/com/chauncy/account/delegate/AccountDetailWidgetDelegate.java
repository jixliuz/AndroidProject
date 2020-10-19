package com.chauncy.account.delegate;

import android.view.View;
import android.widget.LinearLayout;

import com.chauncy.account.R;
import com.chauncy.account.common.event.AccountEventHandler;
import com.chauncy.account.common.event.Observer;
import com.chauncy.account.model.AccountDataCenter;
import com.chauncy.account.model.bean.AccountInfo;
import com.chauncy.account.model.bean.SecurityAccount;
import com.chauncy.account.utils.AccountConstant;
import com.chauncy.account.model.AccountBaseModel;
import com.chauncy.account.common.recyclerview.ItemViewDelegate;
import com.chauncy.account.common.recyclerview.ViewHolder;
import com.chauncy.account.utils.SettingInfoManager;
import com.chauncy.account.utils.NumberFormat;

import org.jetbrains.annotations.NotNull;


public class AccountDetailWidgetDelegate implements ItemViewDelegate<AccountBaseModel> {
    private ViewHolder mViewHolder;
    private SecurityAccount mAccount;

    private LinearLayout cashLayout;
    private LinearLayout marginLayout;

    private boolean isInited = false;

    private Observer observer = new Observer() {
        @Override
        public void update(@NotNull SecurityAccount msg) {
            if (mAccount == msg) {
                switch (msg.getEventType()) {
                    case SecurityAccount.IAction.ACCOUNT_CHANGE:
                    case SecurityAccount.IAction.INFO_STATE_CHANGE:
                        refreshUI();
                        break;
                    default:
                }
            }
        }
    };


    @Override
    public int getItemViewLayoutId() {
        return R.layout.asset_detail_layout;
    }

    @Override
    public boolean isForViewType(AccountBaseModel item, int position) {
        return item.getWidgetType() == AccountConstant.ASSET_DETAIL_WIDGET;
    }

    @Override
    public void convert(ViewHolder holder, AccountBaseModel baseModel, int position) {
        mViewHolder = holder;
        mAccount = baseModel.getAccount();
        if (!isInited) {
            isInited = true;
            AccountEventHandler.get().subject(observer);
            cashLayout = holder.getView(R.id.detail_cash_layout);
            marginLayout = holder.getView(R.id.detail_margin_layout);
        }
        refreshUI();
    }


    private void refreshUI() {
        AccountInfo info = AccountDataCenter.get().getAccountInfo(mAccount.getAccountID());
        if (info == null) return;
        if (AccountConstant.ACCOUNT_FUNCTION_TYPE_CASH.equals(info.getType())) {
            cashLayout.setVisibility(View.VISIBLE);
            marginLayout.setVisibility(View.GONE);
        } else {
            cashLayout.setVisibility(View.GONE);
            marginLayout.setVisibility(View.VISIBLE);
        }
        boolean isShow = SettingInfoManager.getInfoShowStatus();
        if (isShow) {
            showText(info);
        } else {
            hideText(info);
        }
    }

    private void hideText(AccountInfo info) {
        //设置现金账户的值
        if (AccountConstant.ACCOUNT_FUNCTION_TYPE_CASH.equals(info.getType())) {
            mViewHolder.setText(R.id.cash_market_value, "***");
            mViewHolder.setText(R.id.cash_value, "***");
            mViewHolder.setText(R.id.cash_max_purchase, "***");
            mViewHolder.setText(R.id.cash_amount_owned, "***");
            mViewHolder.setText(R.id.frozen_cash, "***");
            mViewHolder.setText(R.id.cash_available, "***");
        } else {
            mViewHolder.setText(R.id.margin_market_value, "***");
            mViewHolder.setText(R.id.margin_max_purchase, "***");
            mViewHolder.setText(R.id.risk_level, "***");
            mViewHolder.setText(R.id.margin_cash, "***");
            mViewHolder.setText(R.id.margin_frozen_cash, "***");
            mViewHolder.setText(R.id.margin_cash_available, "***");
        }
    }

    private void showText(AccountInfo info) {
        if (AccountConstant.ACCOUNT_FUNCTION_TYPE_CASH.equals(info.getType())) {
            mViewHolder.setText(R.id.cash_market_value, NumberFormat.parseToString(info.getMarketValue()));
            mViewHolder.setText(R.id.cash_value, NumberFormat.parseToString(info.getCash()));
            mViewHolder.setText(R.id.cash_max_purchase, NumberFormat.parseToString(info.getMaxPurchase()));
            mViewHolder.setText(R.id.cash_amount_owned, NumberFormat.parseToString(info.getAmountOwed()));
            mViewHolder.setText(R.id.frozen_cash, NumberFormat.parseToString(info.getFrozenCash()));
            mViewHolder.setText(R.id.cash_available, NumberFormat.parseToString(info.getAvailableCash()));
        } else {
            mViewHolder.setText(R.id.margin_market_value, NumberFormat.parseToString(info.getMarketValue()));
            mViewHolder.setText(R.id.margin_max_purchase, NumberFormat.parseToString(info.getMaxPurchase()));
            mViewHolder.setText(R.id.risk_level, info.getRiskLevel());
            mViewHolder.setText(R.id.margin_cash, NumberFormat.parseToString(info.getCash()));
            mViewHolder.setText(R.id.margin_frozen_cash, NumberFormat.parseToString(info.getFrozenCash()));
            mViewHolder.setText(R.id.margin_cash_available, NumberFormat.parseToString(info.getAvailableCash()));
        }
    }

}

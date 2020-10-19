package com.chauncy.account.delegate;

import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.chauncy.account.R;
import com.chauncy.account.common.event.AccountEventHandler;
import com.chauncy.account.common.event.Observer;
import com.chauncy.account.delegate.wrapper.AccountPositionWrapper;
import com.chauncy.account.model.DbDataCenter;
import com.chauncy.account.model.bean.SecurityAccount;
import com.chauncy.account.model.bean.AccountOrder;
import com.chauncy.account.model.bean.Position;
import com.chauncy.account.utils.AccountConstant;
import com.chauncy.account.model.AccountBaseModel;
import com.chauncy.account.common.recyclerview.ItemViewDelegate;
import com.chauncy.account.common.recyclerview.ViewHolder;
import com.chauncy.account.delegate.wrapper.TodayOrderWrapper;

import org.jetbrains.annotations.NotNull;

import java.util.List;


public class PositionOrderWidgetDelegate implements ItemViewDelegate<AccountBaseModel> {

    private ViewHolder viewHolder;

    private int selectedColor = 0XFFFF8800;
    private int noSelectedColor = Color.GRAY;
    private SecurityAccount mAccount;

    private boolean isInited = false;
    private AccountPositionWrapper positionWrapper;
    private TodayOrderWrapper todayOrderWrapper;


    private TextView tabPosition;
    private TextView tabOrder;

    private Observer observer = new Observer() {
        @Override
        public void update(@NotNull SecurityAccount msg) {
            if(mAccount==msg) {
                switch (msg.getEventType()) {
                    case SecurityAccount.IAction.ACCOUNT_CHANGE:
                        refreshPositionList(DbDataCenter.get().getAccountPosition(mAccount.getAccountID()));
                        refreshOrderList(DbDataCenter.get().getAccountOrder(mAccount.getAccountID()));
                    case SecurityAccount.IAction.INFO_STATE_CHANGE:
                        refreshShowInfo();
                        break;
                    default:
                }
            }
        }
    };

    private void refreshShowInfo() {
        if (positionWrapper != null)
            positionWrapper.refreshPositionShow();
        if(todayOrderWrapper!=null)
            todayOrderWrapper.refreshOrderShowInfo();
    }

    private void refreshOrderList(List<AccountOrder> orderList){
        if (todayOrderWrapper != null) {
            refreshTabOrderTitle(orderList.size());
            todayOrderWrapper.refreshOrder(orderList);
        }
    }

    private void refreshPositionList(List<Position> positionList) {
        if (positionWrapper != null) {
            refreshTabPositionTitle(positionList.size());
            positionWrapper.refreshPosition(positionList);
        }

    }

    private void refreshTabOrderTitle(int amount) {
        tabOrder.setText("今日订单(" + amount + ")");

    }

    private void refreshTabPositionTitle(int amount) {
        tabPosition.setText("持仓(" + amount + ")");
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.position_order_tab_layout;
    }

    @Override
    public boolean isForViewType(AccountBaseModel item, int position) {
        return item.getWidgetType() == AccountConstant.POSITION_ORDER_WIDGET;
    }

    @Override
    public void convert(ViewHolder holder, AccountBaseModel baseModel, int position) {
        mAccount = baseModel.getAccount();
        viewHolder = holder;
        List<Position> positionList = DbDataCenter.get().getAccountPosition(mAccount.getAccountID());
        List<AccountOrder> orderList = DbDataCenter.get().getAccountOrder(mAccount.getAccountID());
        if (!isInited) {
            isInited=true;
            positionWrapper = new AccountPositionWrapper(holder, positionList);
            todayOrderWrapper = new TodayOrderWrapper(holder, orderList);
            tabPosition = holder.getView(R.id.tab_position);
            tabOrder = holder.getView(R.id.tab_today_order);
            tabPosition.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    showPositionLayout();
                }
            });

            tabOrder.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    hidePositionLayout();
                }
            });
            //订阅事件
            AccountEventHandler.get().subject(observer);
        }else{
            refreshPositionList(positionList);
            refreshOrderList(orderList);
        }
        //刷新持仓数量
        refreshTabOrderTitle(orderList.size());
        refreshTabPositionTitle(positionList.size());
    }

    private void showPositionLayout() {
        View tabPositionLine = viewHolder.getView(R.id.tab_position_line);
        View tabOrderLine = viewHolder.getView(R.id.tab_order_line);
        tabPosition.setTextColor(selectedColor);
        tabOrder.setTextColor(noSelectedColor);
        tabPositionLine.setVisibility(View.VISIBLE);
        tabOrderLine.setVisibility(View.INVISIBLE);
        LinearLayout orderLayout = viewHolder.getView(R.id.order_layout);
        orderLayout.setVisibility(View.GONE);
        LinearLayout positionLayout = viewHolder.getView(R.id.position_head_layout);
        positionLayout.setVisibility(View.VISIBLE);
    }

    private void hidePositionLayout() {
        View tabPositionLine = viewHolder.getView(R.id.tab_position_line);
        View tabOrderLine = viewHolder.getView(R.id.tab_order_line);
        tabPosition.setTextColor(noSelectedColor);
        tabOrder.setTextColor(selectedColor);
        tabPositionLine.setVisibility(View.INVISIBLE);
        tabOrderLine.setVisibility(View.VISIBLE);
        LinearLayout orderLayout = viewHolder.getView(R.id.order_layout);
        orderLayout.setVisibility(View.VISIBLE);
        LinearLayout positionLayout = viewHolder.getView(R.id.position_head_layout);
        positionLayout.setVisibility(View.GONE);
    }
}

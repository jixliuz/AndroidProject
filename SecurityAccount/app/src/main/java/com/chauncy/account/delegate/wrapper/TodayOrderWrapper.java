package com.chauncy.account.delegate.wrapper;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chauncy.account.R;
import com.chauncy.account.common.recyclerview.RecyclerViewDivider;
import com.chauncy.account.common.recyclerview.ViewHolder;
import com.chauncy.account.model.bean.AccountOrder;
import com.chauncy.account.utils.AccountConstant;
import com.chauncy.account.utils.AccountHelper;
import com.chauncy.account.utils.Global;
import com.chauncy.account.utils.SettingInfoManager;


import java.util.ArrayList;
import java.util.List;

public class TodayOrderWrapper {

    private RecyclerView orderRecyclerView;
    private OrderAdapter adapter;

    private TextView orderTotalView;
    private View popInfoView;
    private TextView totalView;
    private TextView pendingView;
    private TextView dealView;
    private TextView cancelView;

    private List<AccountOrder> mOrderList;

    public TodayOrderWrapper(final ViewHolder viewHolder, List<AccountOrder> orderList) {
        orderRecyclerView = viewHolder.getView(R.id.order_recyclerView);
        mOrderList = orderList;
        if (mOrderList == null)
            mOrderList = new ArrayList<>();
        adapter = new OrderAdapter(mOrderList);
        orderRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(viewHolder.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        orderRecyclerView.setLayoutManager(layoutManager);
        orderRecyclerView.addItemDecoration(new RecyclerViewDivider(viewHolder.getContext(), LinearLayoutManager.HORIZONTAL));

        popInfoView = LayoutInflater.from(viewHolder.getContext()).inflate(R.layout.order_popwindow_layout, null);
        totalView = popInfoView.findViewById(R.id.pop_window_total);
        pendingView = popInfoView.findViewById(R.id.pop_window_pendingDeal);
        dealView = popInfoView.findViewById(R.id.pop_window_deal);
        cancelView = popInfoView.findViewById(R.id.pop_window_order_cancel);
        orderTotalView = viewHolder.getView(R.id.order_total);
        orderTotalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow(viewHolder.getContext(), orderTotalView);
            }
        });


        refreshPopTitle(orderList);

    }

    public void refreshOrder(List<AccountOrder> orderList) {
        if (orderList != null) {
            mOrderList = orderList;
            refreshPopTitle(orderList);
            adapter.refreshData(orderList);
        }
        orderTotalView.setText(Global.getString(R.string.string_text_total));
    }

    public void refreshOrderShowInfo() {
        adapter.refreshInfoShow();
        orderTotalView.setText(Global.getString(R.string.string_text_total));
    }

    private void refreshPopTitle(List<AccountOrder> orderList) {
        int pending = 0, deal = 0, cancel = 0;
        for (AccountOrder order : orderList) {
            switch (order.getSubmitStatus()) {
                case AccountConstant.Order.ORDER_PENDING_SUBMIT:
                    pending++;
                    break;
                case AccountConstant.Order.ORDER_SUBMITTED:
                    deal++;
                    break;
                case AccountConstant.Order.ORDER_CANCELLED:
                    cancel++;
                    break;
            }
        }
        int total = pending + deal + cancel;
        totalView.setText(Global.getString(R.string.string_text_total)+"(" + total + ")");
        pendingView.setText(Global.getString(R.string.string_text_pendingSubmit)+"(" + pending + ")");
        dealView.setText(Global.getString(R.string.string_text_submitted)+"(" + deal + ")");
        cancelView.setText(Global.getString(R.string.string_text_cancelled)+"(" + cancel + ")");
    }

    private void showPopWindow(final Context context, View parent) {
        final PopupWindow popWindow = new PopupWindow(popInfoView, 350, 450, true);
        popWindow.setTouchable(true);

        totalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.refreshData(mOrderList);
                orderTotalView.setText(Global.getString(R.string.string_text_total));
                popWindow.dismiss();
            }
        });

        pendingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AccountOrder> orderList = AccountHelper.filter(mOrderList, "等待提交");
                orderTotalView.setText(Global.getString(R.string.string_text_pendingSubmit));
                adapter.refreshData(orderList);
                popWindow.dismiss();
            }
        });

        dealView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AccountOrder> orderList = AccountHelper.filter(mOrderList, "已提交");
                orderTotalView.setText(Global.getString(R.string.string_text_submitted));
                adapter.refreshData(orderList);
                popWindow.dismiss();
            }
        });

        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<AccountOrder> orderList = AccountHelper.filter(mOrderList, "已撤单");
                orderTotalView.setText(Global.getString(R.string.string_text_cancelled));
                adapter.refreshData(orderList);
                popWindow.dismiss();
            }
        });
        popWindow.showAsDropDown(parent);
        CountDownTimer timer = new CountDownTimer(3000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                popWindow.dismiss();
            }
        };
        timer.start();
    }

    private static class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {
        private List<AccountOrder> mOrderList;

        public OrderAdapter(@NonNull List<AccountOrder> data) {
            mOrderList = new ArrayList<>(data);
        }

        @NonNull
        @Override
        public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout, parent, false);
            OrderViewHolder holder = new OrderViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
            holder.fill(mOrderList.get(position));
        }

        @Override
        public int getItemCount() {
            return mOrderList.size();
        }

        public void refreshData(List<AccountOrder> orderList) {
            if (orderList != null) {
                mOrderList.clear();
                mOrderList.addAll(orderList);
                notifyDataSetChanged();
            }
        }

        public void refreshInfoShow() {
            notifyDataSetChanged();
        }
    }


    private static class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView securityName;
        private TextView securityId;
        private TextView orderPrice;
        private TextView orderAmount;
        private TextView succAmount;
        private TextView tradeStatus;
        private TextView submitStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            securityName = itemView.findViewById(R.id.security_name);
            securityId = itemView.findViewById(R.id.security_id);
            orderPrice = itemView.findViewById(R.id.order_price);
            orderAmount = itemView.findViewById(R.id.order_amount);
            succAmount = itemView.findViewById(R.id.succ_amount);
            tradeStatus = itemView.findViewById(R.id.trade_status);
            submitStatus = itemView.findViewById(R.id.submit_status);
        }


        public void fill(AccountOrder order) {
            boolean isShow = SettingInfoManager.getInfoShowStatus();
            if (isShow) {
                showInfo(order);
            } else {
                hideInfo();
            }
        }

        private void showInfo(AccountOrder todayOrder) {
            securityName.setText(todayOrder.getStockName());
            securityId.setText(todayOrder.getStockId());
            orderPrice.setText(String.valueOf(todayOrder.getOrderPrice()));
            orderAmount.setText(String.valueOf(todayOrder.getOrderAmount()));
            succAmount.setText(String.valueOf(todayOrder.getSuccAmount()));
            tradeStatus.setText(todayOrder.getOrderStatus());
            submitStatus.setText(todayOrder.getSubmitStatus());
        }

        private void hideInfo() {
            securityName.setText("***");
            securityId.setText("***");
            orderPrice.setText("***");
            orderAmount.setText("***");
            succAmount.setText("***");
            tradeStatus.setText("***");
            submitStatus.setText("***");
        }
    }


}

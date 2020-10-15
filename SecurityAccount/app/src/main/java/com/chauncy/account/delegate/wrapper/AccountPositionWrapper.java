package com.chauncy.account.delegate.wrapper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chauncy.account.R;
import com.chauncy.account.common.recyclerview.RecyclerViewDivider;
import com.chauncy.account.common.recyclerview.ViewHolder;
import com.chauncy.account.model.bean.Position;
import com.chauncy.account.share.ShareConstant;
import com.chauncy.account.share.ShareType;
import com.chauncy.account.utils.AccountHelper;
import com.chauncy.account.utils.Global;
import com.chauncy.account.utils.NumberFormat;
import com.chauncy.account.utils.SettingInfoManager;

import java.util.ArrayList;
import java.util.List;

public class AccountPositionWrapper {
    static final int ONLY_NAME_AND_CODE = 0;
    static final int OTHER_POSITION_INFO = 1;

    private RecyclerView nameRecyclerView;
    private PositionAdapter nameAdapter;

    private RecyclerView positionRecyclerView;
    private PositionAdapter positionAdapter;

    public AccountPositionWrapper(ViewHolder viewHolder, List<Position> positionList) {
        nameRecyclerView = viewHolder.getView(R.id.name_code_recyclerView);
        if (positionList == null)
            positionList = new ArrayList<>();
        nameAdapter = new PositionAdapter(positionList, ONLY_NAME_AND_CODE);
        LinearLayoutManager nameManager = new LinearLayoutManager(viewHolder.getContext());
        nameManager.setOrientation(LinearLayoutManager.VERTICAL);
        nameRecyclerView.setLayoutManager(nameManager);
        nameRecyclerView.addItemDecoration(new RecyclerViewDivider(viewHolder.getContext(), LinearLayoutManager.HORIZONTAL));
        nameRecyclerView.setAdapter(nameAdapter);

        positionRecyclerView = viewHolder.getView(R.id.position_info_recyclerView);
        positionAdapter = new PositionAdapter(positionList, OTHER_POSITION_INFO);
        LinearLayoutManager otherManager = new LinearLayoutManager(viewHolder.getContext());
        otherManager.setOrientation(LinearLayoutManager.VERTICAL);
        positionRecyclerView.setLayoutManager(otherManager);
        positionRecyclerView.setAdapter(positionAdapter);
        positionRecyclerView.addItemDecoration(new RecyclerViewDivider(viewHolder.getContext(), LinearLayoutManager.HORIZONTAL));
    }


    public void refreshPosition(List<Position> positionList) {
        if (positionList != null) {
            positionAdapter.refreshData(positionList);
            nameAdapter.refreshData(positionList);
        }
    }

    public void refreshPositionShow() {
        positionAdapter.refreshShowInfo();
        nameAdapter.refreshShowInfo();
    }

    private static class PositionAdapter extends RecyclerView.Adapter<PositionViewHolder> {
        private List<Position> mPositionList;
        private int mType;

        public PositionAdapter(List<Position> positionList, int type) {
            mPositionList = new ArrayList<>(positionList);
            mType = type;
        }

        @NonNull
        @Override
        public PositionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(getItemViewLayoutId(), parent, false);
            PositionViewHolder holder = new PositionViewHolder(view, mType);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull PositionViewHolder holder, int position) {
            holder.fill(mPositionList.get(position));
        }

        @Override
        public int getItemCount() {
            return mPositionList.size();
        }

        public int getItemViewLayoutId() {
            if (mType == ONLY_NAME_AND_CODE)
                return R.layout.position_name_layout;
            else
                return R.layout.position_item_layout;
        }

        public void refreshData(List<Position> positions) {
            if (positions != null) {
                mPositionList.clear();
                mPositionList.addAll(positions);
                notifyDataSetChanged();
            }
        }

        public void refreshShowInfo() {
            notifyDataSetChanged();
        }
    }

    private static class PositionViewHolder extends RecyclerView.ViewHolder {

        private int mType;
        private Position mPosition;

        private TextView securityName;
        private TextView securityId;
        private TextView marketValue;
        private TextView securityAmount;
        private TextView securityPrice;
        private TextView securityCost;
        private TextView todayProfit;
        private TextView positionProfit;
        private TextView positionProfitRatio;
        private TextView positionRatio;
        private ImageView shareIcon;

        public PositionViewHolder(@NonNull View itemView, int type) {
            super(itemView);
            mType = type;
            if ((mType = type) == ONLY_NAME_AND_CODE) {
                securityName = itemView.findViewById(R.id.security_name);
                securityId = itemView.findViewById(R.id.security_id);
            } else {
                marketValue = itemView.findViewById(R.id.security_market_value);
                securityAmount = itemView.findViewById(R.id.security_amount);
                securityPrice = itemView.findViewById(R.id.security_price);
                securityCost = itemView.findViewById(R.id.security_cost);
                todayProfit = itemView.findViewById(R.id.today_profit);
                positionProfit = itemView.findViewById(R.id.position_profit);
                positionProfitRatio=itemView.findViewById(R.id.position_profit_ratio);
                positionRatio = itemView.findViewById(R.id.position_ratio);
                shareIcon=itemView.findViewById(R.id.position_stock_share);

                shareIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mPosition==null) return;
                        Bundle bundle=new Bundle();
                        String accountId=mPosition.getAccountId();
                        bundle.putString(ShareConstant.KEY_SHARE_ACCOUNT_ID, mPosition.getAccountId());
                        bundle.putString(ShareConstant.KEY_SHARE_ACCOUNT_TYPE, AccountHelper.getAccountType(accountId));
                        bundle.putInt(ShareConstant.KEY_SHARE_TYPE, ShareType.SHARE_STOCK.ordinal());
                        bundle.putDouble(ShareConstant.KEY_SHARE_PROFIT,mPosition.getTodayProfit());
                        bundle.putDouble(ShareConstant.KEY_SHARE_PROFIT_RATIO,mPosition.getPositionProfitRatio());

                        Intent intent=new Intent();
                        intent.setAction("com.chauncy.nntrade.share_activity");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtras(bundle);
                        Global.getContext().startActivity(intent);
                    }
                });
            }
        }

        public void fill(Position position) {
            mPosition=position;
            boolean isShow = SettingInfoManager.getInfoShowStatus();
            if (isShow) {
                showInfo(position);
            } else {
                hideInfo();
            }
        }

        private void showInfo(Position position) {
            if (mType == ONLY_NAME_AND_CODE) {
                securityName.setText(position.getStockName());
                securityId.setText(position.getStockId());
            } else {
                marketValue.setText(Double.toString(position.getMarketValue()));
                securityAmount.setText(Integer.toString(position.getAmount()));
                securityPrice.setText(Double.toString(position.getPrice()));
                securityCost.setText(Double.toString(position.getCost()));
                Global.setTextAndColor(todayProfit,position.getTodayProfit(),false);
                Global.setTextAndColor(positionProfit,position.getPositionProfit(),false);
                positionProfitRatio.setText(NumberFormat.parseToString(position.getPositionProfitRatio())+"%");
                positionRatio.setText(position.getPositionRatio() + "%");
            }
        }

        private void hideInfo() {
            if (mType == ONLY_NAME_AND_CODE) {
                securityName.setText("***");
                securityId.setText("***");
            } else {
                marketValue.setText("***");
                securityAmount.setText("***");
                securityPrice.setText("***");
                securityCost.setText("***");
                todayProfit.setText("***");
                Global.resetColor(todayProfit);
                positionProfit.setText("***");
                Global.resetColor(positionProfit);
                positionProfitRatio.setText("***");
                positionRatio.setText("***");
            }
        }
    }

}

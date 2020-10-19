package com.chauncy.account.delegate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chauncy.account.R;
import com.chauncy.account.model.bean.SecurityAccount;
import com.chauncy.account.utils.AccountConstant;
import com.chauncy.account.model.AccountBaseModel;
import com.chauncy.account.model.IconText;
import com.chauncy.account.common.recyclerview.ItemViewDelegate;
import com.chauncy.account.common.recyclerview.MultiItemViewAdapter;
import com.chauncy.account.common.recyclerview.ViewHolder;
import com.chauncy.account.utils.AccountHelper;
import com.chauncy.account.utils.Global;

import java.util.List;


public class FunctionWidgetDelegate implements ItemViewDelegate<AccountBaseModel> {

    private int recyclerViewId = R.id.function_recyclerView;

    private SecurityAccount mAccount;

    private RecyclerView mRecyclerView;
    FunctionWidgetAdapter adapter;
    private boolean isInited=false;

    public FunctionWidgetDelegate() {
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.function_widget_layout;
    }

    @Override
    public boolean isForViewType(AccountBaseModel item, int position) {
        return item.getWidgetType() == AccountConstant.FUNCTION_WIDGET;
    }

    @Override
    public void convert(ViewHolder holder, AccountBaseModel baseModel, int position) {
        Context context = holder.getContext();
        mAccount = baseModel.getAccount();
        List<IconText> iconTexts = AccountHelper.getFunctionItemsIconText(mAccount.getAccountType());
        if(!isInited) {
            isInited=true;
            mRecyclerView = holder.getView(recyclerViewId);
            adapter = new FunctionWidgetAdapter(context, iconTexts);
            mRecyclerView.setLayoutManager(new GridLayoutManager(context, 5, RecyclerView.VERTICAL, false));
            mRecyclerView.setAdapter(adapter);
        }else{
            adapter.refreshData(iconTexts);
        }
    }


    private static class FunctionWidgetAdapter extends MultiItemViewAdapter<IconText> {

        public FunctionWidgetAdapter(@NonNull Context context, @NonNull List<IconText> data) {
            super(context, data);
            addItemViewDelegate(new FunctionItemDelegate());
        }

    }

    private static class FunctionItemDelegate implements ItemViewDelegate<IconText> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.icon_text_item_layout;
        }

        @Override
        public boolean isForViewType(IconText item, int position) {
            return true;
        }

        @Override
        public void convert(final ViewHolder holder, IconText item, final int position) {
            View convertView = holder.getConvertView();
            Context context = holder.getContext();
            ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(Global.dp2px(75), Global.dp2px(75));
            convertView.setLayoutParams(params);

            TextView textView = holder.getView(R.id.item_img_text);
            textView.setText(item.getText());
            Drawable drawable = context.getDrawable(item.getImageResource());
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            textView.setCompoundDrawables(null, drawable, null, null);


            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(holder.getContext(), "你点击了Item" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}


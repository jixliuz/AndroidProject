package com.chauncy.account.share;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


import com.chauncy.account.R;

import com.chauncy.account.common.recyclerview.RecyclerViewClickListener;
import com.chauncy.account.view.PageIndicatorView;
import com.chauncy.account.view.PageRecyclerView;

import java.util.Date;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

public class ShareActivity extends AppCompatActivity {

	private PageRecyclerView mPageRecyclerView = null;

	private ShareStrategy mShareStrategy=null;

	private ShareScrollView mShareScrollView=null;

	private List<ShareItem> mShareItemList=null;

	private int curSelectedTab=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_share);


        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("分享");
        }


        ShareInfo info=parseParams();
		mShareStrategy=ShareStrategy.Factory.create(info.getShareType());
		mShareStrategy.setInfo(info);
		mShareItemList=mShareStrategy.createShareItem();

		mPageRecyclerView = findViewById(R.id.share_recyclerView);
		// 设置指示器
		mPageRecyclerView.setIndicator((PageIndicatorView) findViewById(R.id.share_indicator));
        LinearLayoutManager manager=new LinearLayoutManager(ShareActivity.this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mPageRecyclerView.setLayoutManager(manager);
		// 设置数据
		mPageRecyclerView.setAdapter(new SharePageAdapter(mShareStrategy));

		mShareScrollView=findViewById(R.id.share_scrollView);
		mShareScrollView.setShareItems(mShareItemList);

		mShareScrollView.setItemClickListener(new RecyclerViewClickListener.OnItemClickListener(){

			@Override
			public void onItemClick(View view, int position) {
				mPageRecyclerView.scrollToPosition(position);
			}
		});
	}

    private ShareInfo parseParams(){
      Bundle bundle= getIntent().getExtras();

      String accountId=bundle.getString(ShareConstant.KEY_SHARE_ACCOUNT_ID);
      int ordinal=bundle.getInt(ShareConstant.KEY_SHARE_TYPE);
      ShareType shareType=ShareType.Companion.create(ordinal);
      double profit=bundle.getDouble(ShareConstant.KEY_SHARE_PROFIT);
      double profitRatio=bundle.getDouble(ShareConstant.KEY_SHARE_PROFIT_RATIO);
      String accountType=bundle.getString(ShareConstant.KEY_SHARE_ACCOUNT_TYPE);

     return new ShareInfo(shareType, accountId, profit, profitRatio, accountType,new Date());

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}

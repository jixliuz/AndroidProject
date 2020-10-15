package com.chauncy.account.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.chauncy.account.utils.Global;

import java.util.ArrayList;
import java.util.List;

public class PageIndicatorView extends LinearLayout {
	private Context mContext = null;
	private int width = 15; // 指示器的宽（dp）
	private int height=4; //指示器高
	private int margins = 2; // 指示器间距（dp）
	private List<View> indicatorViews = null; // 存放指示器

	public PageIndicatorView(Context context) {
		this(context, null);
	}

	public PageIndicatorView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PageIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		this.mContext = context;

		setGravity(Gravity.CENTER);
		setOrientation(HORIZONTAL);

		width = Global.dp2px(context, width);
		height=Global.dp2px(context,height);
		margins = Global.dp2px(context, margins);
	}

	/**
	 * 初始化指示器，默认选中第一页
	 *
	 * @param count 指示器数量，即页数
	 */
	public void initIndicator(int count) {

		if (indicatorViews == null) {
			indicatorViews = new ArrayList<>();
		} else {
			indicatorViews.clear();
			removeAllViews();
		}
		View view;
		LayoutParams params = new LayoutParams(width, height);
		params.setMargins(margins, margins, margins, margins);
		for (int i = 0; i < count; i++) {
			view = new View(mContext);
			view.setBackgroundResource(android.R.drawable.presence_invisible);
			addView(view, params);
			indicatorViews.add(view);
		}
		if (indicatorViews.size() > 0) {
			indicatorViews.get(0).setBackgroundResource(android.R.drawable.presence_online);
		}
	}

	/**
	 * 设置选中页
	 *
	 * @param selected 页下标，从0开始
	 */
	public void setSelectedPage(int selected) {
		for (int i = 0; i < indicatorViews.size(); i++) {
			if (i == selected) {
				indicatorViews.get(i).setBackgroundResource(android.R.drawable.presence_online);
			} else {
				indicatorViews.get(i).setBackgroundResource(android.R.drawable.presence_invisible);
			}
		}
	}

}

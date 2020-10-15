package com.chauncy.account.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.chauncy.account.R;

public class DrawableTextView extends AppCompatTextView {
    //设置图片方向
    public static final int LEFT = 1, TOP = 2, RIGHT = 3, BOTTOM = 4;
    private int location;
    private int imageWidth;
    private int imageHeight;

    public DrawableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupAttributes(attrs);
    }

    public DrawableTextView(Context context) {
        super(context);
    }

    public DrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupAttributes(attrs);
    }


    private void setupAttributes(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray types = getContext().obtainStyledAttributes(attrs, R.styleable.DrawableTextView);
            Drawable mDrawable = types.getDrawable(R.styleable.DrawableTextView_drawable_src);

            imageWidth = types.getDimensionPixelSize(R.styleable.DrawableTextView_drawable_width,0);
            imageHeight = types.getDimensionPixelSize(R.styleable.DrawableTextView_drawable_height, 0);
            location = types.getInt(R.styleable.DrawableTextView_drawable_location, LEFT);

            types.recycle();
            setDrawable(mDrawable, imageWidth, imageHeight, location);
        }
    }


    public int getLocation() {
        return location;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setDrawable(Drawable drawable, int width, int height, int location) {
        if (drawable != null) {
            drawable.setBounds(0, 0, width, height);
            switch (location) {
                case LEFT:
                    setCompoundDrawables(drawable, null, null, null);
                    break;
                case TOP:
                    setCompoundDrawables(null, drawable, null, null);
                    break;
                case RIGHT:
                    setCompoundDrawables(null, null, drawable, null);
                    break;
                case BOTTOM:
                    setCompoundDrawables(null, null, null, drawable);
                    break;
            }
        }
    }

}

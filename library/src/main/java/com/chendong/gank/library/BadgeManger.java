package com.chendong.gank.library;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TabWidget;
import android.widget.TextView;

import java.io.Serializable;

/**
 * 作者：chendongde310
 * Github:www.github.com/chendongde310
 * 日期：2016/12/5 - 15:39
 * 注释：https://github.com/stefanjauker/BadgeView
 * 更新内容：
 */
public class BadgeManger extends TextView implements Serializable {

    public static final int STYLE_DEFAULT = 1;
    public static final int STYLE_GONE = 0;
    public static final int STYLE_SMALL = 2;
    private boolean mHideOnNull = true;
    private int style;

    public BadgeManger(Context context) {
        this(context, null);
    }

    public BadgeManger(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public BadgeManger(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private void init() {
        if (!(getLayoutParams() instanceof LayoutParams)) {
            LayoutParams layoutParams =
                    new LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            Gravity.RIGHT | Gravity.TOP);
            setLayoutParams(layoutParams);
        }

        // set default font
        setTextColor(Color.WHITE);
        setTypeface(Typeface.DEFAULT_BOLD);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        setPadding(dip2Px(5), dip2Px(1), dip2Px(5), dip2Px(1));

        // set default background
        setBackground(9, Color.parseColor("#d3321b"));

        setGravity(Gravity.CENTER);

        // default values
        setHideOnNull(true);
        setBadgeCount(0);
    }

    public void setBackground(int dipRadius, int badgeColor) {
        int radius = dip2Px(dipRadius);
        float[] radiusArray = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};

        RoundRectShape roundRect = new RoundRectShape(radiusArray, null, null);
        ShapeDrawable bgDrawable = new ShapeDrawable(roundRect);
        bgDrawable.getPaint().setColor(badgeColor);
        setBackgroundDrawable(bgDrawable);
    }

    public void setBadgeStyle(int style) {
        this.style = style;
        switch (style) {
            case STYLE_DEFAULT:
                setVisibility(View.VISIBLE);
                break;
            case STYLE_SMALL:
                setVisibility(View.VISIBLE);
                setSmallBadge();
                break;
            case STYLE_GONE:
                setVisibility(View.GONE);
                break;
        }

    }

    private void setSmallBadge() {
        setWidth(dip2Px(7.5f));
        setHeight(dip2Px(7.5f));
    }


    public boolean isHideOnNull() {
        return mHideOnNull;
    }


    public void setHideOnNull(boolean hideOnNull) {
        mHideOnNull = hideOnNull;
        setText(getText());
    }


    @Override
    public void setText(CharSequence text, TextView.BufferType type) {
        if (isHideOnNull() && (text == null || text.toString().equalsIgnoreCase("0"))) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
        }
        if (style == STYLE_GONE) {
            setVisibility(View.GONE);
        }
        super.setText(text, type);
    }


    public void setBadgeCount(int count) {

        if (style == STYLE_SMALL && count != 0) {
            setText(String.valueOf(""));
        } else {
            setText(String.valueOf(count));
        }
    }

    public int getBadgeGravity() {
        LayoutParams params = (LayoutParams) getLayoutParams();
        return params.gravity;
    }

    public void setBadgeGravity(int gravity) {
        LayoutParams params = (LayoutParams) getLayoutParams();
        params.gravity = gravity;
        setLayoutParams(params);
    }

    public void setBadgeMargin(int leftDipMargin, int topDipMargin, int rightDipMargin, int bottomDipMargin) {
        LayoutParams params = (LayoutParams) getLayoutParams();
        params.leftMargin = dip2Px(leftDipMargin);
        params.topMargin = dip2Px(topDipMargin);
        params.rightMargin = dip2Px(rightDipMargin);
        params.bottomMargin = dip2Px(bottomDipMargin);
        setLayoutParams(params);
    }

    public int[] getBadgeMargin() {
        LayoutParams params = (LayoutParams) getLayoutParams();
        return new int[]{params.leftMargin, params.topMargin, params.rightMargin, params.bottomMargin};
    }

    public void setBadgeMargin(int dipMargin) {
        setBadgeMargin(dipMargin, dipMargin, dipMargin, dipMargin);
    }


    public void setTargetView(TabWidget target, int tabIndex) {
        View tabView = target.getChildTabViewAt(tabIndex);
        setTargetView(tabView);
    }


    public void setTargetView(View target) {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }

        if (target == null) {
            return;
        }


        if (target.getParent() instanceof FrameLayout) {
            ((FrameLayout) target.getParent()).addView(this);

        } else if (target.getParent() instanceof ViewGroup) {

            ViewGroup parentContainer = (ViewGroup) target.getParent();
            int groupIndex = parentContainer.indexOfChild(target);
            parentContainer.removeView(target);

            FrameLayout badgeContainer = new FrameLayout(getContext());
            ViewGroup.LayoutParams parentLayoutParams = target.getLayoutParams();

            badgeContainer.setLayoutParams(parentLayoutParams);
            target.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            parentContainer.addView(badgeContainer, groupIndex, parentLayoutParams);
            badgeContainer.addView(target);

            badgeContainer.addView(this);
        } else if (target.getParent() == null) {
            Log.e(getClass().getSimpleName(), "ParentView is needed");
        }

    }


    private int dip2Px(float dip) {
        return (int) (dip * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }
}

package com.ytx.appframework.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ytx.appframework.R;

/**
 * 地址栏
 */
public class TitleBar extends LinearLayout {

    public static final int INVALID_INT_VALUE = -1;
    public static final int INVALID_FLOAT_VALUE = -1;

    /**
     * 显示图片Flag
     **/
    public static final int FLAG_ICON = 1;
    /**
     * 显示文字Flag
     **/
    public static final int FLAG_TEXT = 2;
    /**
     * 显示文字和图片Flag
     **/
    public static final int FLAG_ALL = 3;
    /**
     * 都不显示Flag
     **/
    public static final int FLAG_NONE = 0;

    private View titleContainer;
    private ViewGroup leftGroup;
    private ViewGroup centerGroup;
    private ViewGroup rightGroup;

    private ImageView ivLeft;
    private TextView tvLeft;

    private TextView tvTitle;
    private TextView tvSmallTitle;

    private ImageView ivRight;
    private TextView tvRight;

    public TitleBar(Context context) {
        super(context);
        init(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);

        String title = typedArray.getString(R.styleable.TitleBar_ytx_title);

        float titleTextSize = (int) typedArray.getDimension(R.styleable.TitleBar_ytx_title_text_size, INVALID_FLOAT_VALUE);
        Integer titleTextColor = null;
        if (typedArray.hasValue(R.styleable.TitleBar_ytx_title_text_color)) {
            titleTextColor = typedArray.getColor(R.styleable.TitleBar_ytx_title_text_color, INVALID_INT_VALUE);
        }

        String smallTitle = typedArray.getString(R.styleable.TitleBar_ytx_small_title);
        float smallTitleTextSize = (int) typedArray.getDimension(R.styleable.TitleBar_ytx_small_title_text_size, INVALID_FLOAT_VALUE);
        Integer smallTitleTextColor = null;
        if (typedArray.hasValue(R.styleable.TitleBar_ytx_small_title_text_color)) {
            smallTitleTextColor = typedArray.getColor(R.styleable.TitleBar_ytx_small_title_text_color, INVALID_INT_VALUE);
        }
        boolean isShowSmallTitle = typedArray.getBoolean(R.styleable.TitleBar_ytx_is_show_small_title, false);

        boolean isCenterTitleBold = typedArray.getBoolean(R.styleable.TitleBar_ytx_is_title_bold, false);

        String leftText = typedArray.getString(R.styleable.TitleBar_ytx_left_text);
        float leftTextSize = typedArray.getDimension(R.styleable.TitleBar_ytx_left_text_size, INVALID_FLOAT_VALUE);
        Integer leftTextColor = null;
        if (typedArray.hasValue(R.styleable.TitleBar_ytx_left_text_color)) {
            leftTextColor = typedArray.getColor(R.styleable.TitleBar_ytx_left_text_color, INVALID_INT_VALUE);
        }

        int leftDrawableRes = typedArray.getResourceId(R.styleable.TitleBar_ytx_left_icon, INVALID_INT_VALUE);
        String rightText = typedArray.getString(R.styleable.TitleBar_ytx_right_text);
        float rightTextSize = (int) typedArray.getDimension(R.styleable.TitleBar_ytx_right_text_size, INVALID_FLOAT_VALUE);
        Integer rightTextColor = null;
        if (typedArray.hasValue(R.styleable.TitleBar_ytx_right_text_color)) {
            rightTextColor = typedArray.getColor(R.styleable.TitleBar_ytx_right_text_color, INVALID_INT_VALUE);
        }
        int rightDrawableRes = typedArray.getResourceId(R.styleable.TitleBar_ytx_right_icon, INVALID_INT_VALUE);

        int leftShow = typedArray.getInt(R.styleable.TitleBar_ytx_left_show, FLAG_ICON);
        int rightShow = typedArray.getInt(R.styleable.TitleBar_ytx_right_show, FLAG_NONE);

        typedArray.recycle();

        setTitle(title);
        setTitleColor(titleTextColor);
        setTitleTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        setTitleTextStyle(isCenterTitleBold);

        setSmallTitle(smallTitle);
        setSmallTitleColor(smallTitleTextColor);
        setSmallTitleTextSize(TypedValue.COMPLEX_UNIT_PX, smallTitleTextSize);
        setSmallTitleShow(isShowSmallTitle);

        setLeftText(leftText);
        setLeftTextColor(leftTextColor);
        setLeftTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize);
        setLeftIcon(leftDrawableRes);

        setRightText(rightText);
        setRightTextColor(rightTextColor);
        setRightTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
        setRightIcon(rightDrawableRes);

        showLeft(leftShow);
        showRight(rightShow);
    }

    private void setTitleTextStyle(boolean isCenterTitleBold) {
        if (isCenterTitleBold) {
            TextPaint tp = tvTitle.getPaint();
            tp.setFakeBoldText(true);
        }
    }

    protected int getTitleBarLayout() {
        return R.layout.widget_title_bar;
    }

    protected void init(Context context) {
        int layoutRes = getTitleBarLayout();
        if (layoutRes != 0) {
            LayoutInflater.from(context).inflate(layoutRes, this, true);

            titleContainer = findViewById(R.id.fl_title_bar_container);
            leftGroup = findViewById(R.id.title_bar_left_container);
            centerGroup = findViewById(R.id.title_bar_center_container);
            rightGroup = findViewById(R.id.title_bar_right_container);

            ivLeft = findViewById(R.id.iv_title_left);
            tvLeft = findViewById(R.id.tv_title_left);

            tvTitle = findViewById(R.id.tv_title_center);
            tvSmallTitle = findViewById(R.id.tv_small_title_center);

            ivRight = findViewById(R.id.iv_title_right);
            tvRight = findViewById(R.id.tv_title_right);
        }
    }

    /**
     * 设置标题颜色
     *
     * @param color
     */
    public void setTitleColor(@ColorInt Integer color) {
        if (color != null && tvTitle != null) {
            tvTitle.setTextColor(color);
        }
    }

    /**
     * 设置标题颜色
     *
     * @param color
     */
    public void setSmallTitleColor(@ColorInt Integer color) {
        if (color != null && tvSmallTitle != null) {
            tvSmallTitle.setTextColor(color);
        }
    }

    /**
     * 设置标题文字大小
     *
     * @param size
     */
    public void setTitleTextSize(float size) {
        if (tvTitle != null) {
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        }
    }

    public void setTitleTextSize(int type, float textSize) {
        if (textSize != INVALID_FLOAT_VALUE && tvTitle != null) {
            tvTitle.setTextSize(type, textSize);
        }
    }

    /**
     * 设置标题文字大小
     *
     * @param size
     */
    public void setSmallTitleTextSize(float size) {
        if (tvSmallTitle != null) {
            tvSmallTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        }
    }

    private void setSmallTitleTextSize(int type, float textSize) {
        if (textSize != INVALID_FLOAT_VALUE && tvSmallTitle != null) {
            tvSmallTitle.setTextSize(type, textSize);
        }
    }

    /**
     * 设置titlebar背景颜色
     *
     * @param color
     */
    public void setTitleBarBgColor(@ColorInt Integer color) {
        if (color != null && titleContainer != null) {
            titleContainer.setBackgroundColor(color);
        }
    }

    /**
     * 设置标题
     */
    public void setTitle(@StringRes int titleRes) {
        setTitle(getContext().getString(titleRes));
    }

    /**
     * 设置标题
     *
     * @param titleStr
     */
    public void setTitle(String titleStr) {
        if (tvTitle != null) {
            tvTitle.setText(titleStr);
        }
    }

    public void setTitleAction(OnClickListener listener) {
        if (tvTitle != null) {
            tvTitle.setOnClickListener(listener);
        }
    }

    /**
     * 设置小标题
     */
    public void setSmallTitle(@StringRes int titleRes) {
        setSmallTitle(getContext().getString(titleRes));
    }

    /**
     * 设置小标题
     *
     * @param titleStr
     */
    public void setSmallTitle(String titleStr) {
        if (tvSmallTitle != null) {
            tvSmallTitle.setText(titleStr);
        }
    }

    public void setSmallTitleShow(boolean isShow) {
        if (tvSmallTitle != null) {
            tvSmallTitle.setVisibility(isShow ? VISIBLE : GONE);
        }
    }

    /**
     * 设置左边图标
     */
    public void setLeftIcon(@DrawableRes int imgRes) {
        if (imgRes != INVALID_INT_VALUE && ivLeft != null) {
            ivLeft.setImageResource(imgRes);
        }
    }

    /**
     * 设置左边icon
     *
     * @param drawable
     */
    public void setLeftIcon(Drawable drawable) {
        if (ivLeft != null) {
            ivLeft.setImageDrawable(drawable);
        }
    }

    /**
     * 设置左边文字
     */
    public void setLeftText(@StringRes int leftRes) {
        setLeftText(getContext().getString(leftRes));
    }

    /**
     * 设置左边文字
     *
     * @param text
     */
    public void setLeftText(String text) {
        if (tvLeft != null) {
            tvLeft.setText(text);
        }
    }

    /**
     * 设置左边文字颜色
     *
     * @param color
     */
    public void setLeftTextColor(@ColorInt Integer color) {
        if (color != null && tvLeft != null) {
            tvLeft.setTextColor(color);
        }
    }

    /**
     * 设置左边文字大小
     *
     * @param size
     */
    public void setLeftTextSize(float size) {
        if (tvLeft != null) {
            tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        }
    }

    public void setLeftTextSize(int type, float textSize) {
        if (textSize != INVALID_FLOAT_VALUE && tvLeft != null) {
            tvLeft.setTextSize(type, textSize);
        }
    }

    public void setLeftBgColor(@ColorInt Integer color) {
        if (color != null && tvLeft != null) {
            tvLeft.setBackgroundColor(color);
        }
    }

    public void setLeftBackground(Drawable drawable) {
        if (tvLeft != null) {
            tvLeft.setBackground(drawable);
        }
    }

    /**
     * 设置右图标
     */
    public void setRightIcon(@DrawableRes int imgRes) {
        if (imgRes != INVALID_INT_VALUE && ivRight != null) {
            ivRight.setImageResource(imgRes);
        }
    }

    /**
     * 设置右边icon
     *
     * @param drawable
     */
    public void setRightIcon(Drawable drawable) {
        if (ivRight != null) {
            ivRight.setImageDrawable(drawable);
        }
    }

    /**
     * 设置右边文字
     */
    public void setRightText(@StringRes int rightRes) {
        setRightText(getContext().getString(rightRes));
    }

    /**
     * 设置右边文字
     *
     * @param text
     */
    public void setRightText(String text) {
        if (tvRight != null) {
            tvRight.setText(text);
        }
    }

    /**
     * 设置右边文字大小
     *
     * @param size
     */
    public void setRightTextSize(float size) {
        if (tvRight != null) {
            tvRight.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size);
        }
    }

    public void setRightTextSize(int type, float textSize) {
        if (textSize != INVALID_FLOAT_VALUE && tvRight != null) {
            tvRight.setTextSize(type, textSize);
        }
    }

    /**
     * 设置右边文字颜色
     *
     * @param color
     */
    public void setRightTextColor(@ColorInt Integer color) {
        if (color != null && tvRight != null) {
            tvRight.setTextColor(color);
        }
    }

    public void setRightBgColor(@ColorInt Integer color) {
        if (color != null && tvRight != null) {
            tvRight.setBackgroundColor(color);
        }
    }

    public void setRightBackground(Drawable drawable) {
        if (tvRight != null) {
            tvRight.setBackground(drawable);
        }
    }

    /**
     * 设置左点击事件
     */
    public void setLeftIconAction(OnClickListener listener) {
        if (ivLeft != null) {
            ivLeft.setOnClickListener(listener);
        }
    }

    /**
     * 设置左边文字点击action
     *
     * @param listener
     */
    public void setLeftTextAction(OnClickListener listener) {
        if (tvLeft != null) {
            tvLeft.setOnClickListener(listener);
        }
    }

    /**
     * 设置左图标及点击事件
     */
    public void setLeftIconAndAction(@DrawableRes int imgRes, OnClickListener listener) {
        setLeftIcon(imgRes);
        setLeftIconAction(listener);
    }

    /**
     * 设置左边icon和点击action
     *
     * @param drawable
     * @param listener
     */
    public void setLeftIconAndAction(@NonNull Drawable drawable, OnClickListener listener) {
        setLeftIcon(drawable);
        setLeftIconAction(listener);
    }

    /**
     * 设置左边文字和点击事件
     *
     * @param leftRes
     * @param listener
     */
    public void setLeftTextAndAction(@StringRes int leftRes, OnClickListener listener) {
        setLeftText(leftRes);
        setLeftTextAction(listener);
    }

    /**
     * 设置左边文字和点击事件
     *
     * @param text
     * @param listener
     */
    public void setLeftTextAndAction(@NonNull String text, OnClickListener listener) {
        setLeftText(text);
        setLeftTextAction(listener);
    }

    /**
     * 设置右点击事件
     */
    public void setRightIconAction(OnClickListener listener) {
        if (ivRight != null) {
            ivRight.setOnClickListener(listener);
        }
    }

    /**
     * 设置右边文字点击action
     *
     * @param listener
     */
    public void setRightTextAction(OnClickListener listener) {
        if (tvRight != null) {
            tvRight.setOnClickListener(listener);
        }
    }

    /**
     * 设置右图标及点击事件
     */
    public void setRightIconAndAction(@DrawableRes int imgRes, OnClickListener listener) {
        setRightIcon(imgRes);
        setRightIconAction(listener);
    }

    /**
     * 设置右边icon和点击action
     *
     * @param drawable
     * @param listener
     */
    public void setRightIconAndAction(@NonNull Drawable drawable, OnClickListener listener) {
        setRightIcon(drawable);
        setRightIconAction(listener);
    }

    /**
     * 设置右边文字及点击事件
     */
    public void setRightTextAndAction(@StringRes int rightRes, @NonNull OnClickListener listener) {
        setRightText(rightRes);
        setRightTextAction(listener);
    }

    /**
     * 设置右边文字和点击action
     *
     * @param text
     * @param listener
     */
    public void setRightTextAndAction(@NonNull String text, @NonNull OnClickListener listener) {
        setRightText(text);
        setRightTextAction(listener);
    }

    /**
     * 隐藏左边icon和text
     */
    public void hideLeft() {
        showLeft(FLAG_NONE);
    }

    /**
     * 显示左边icon
     */
    public void showLeft() {
        showLeft(FLAG_ICON);
    }

    /**
     * 显示右icon
     */
    public void showRight() {
        showRight(FLAG_ICON);
    }

    /**
     * 隐藏右边icon和text
     */
    public void hideRight() {
        showRight(FLAG_NONE);
    }

    /**
     * 显示右边的ImageView
     *
     * @param flag
     */
    public void showRight(int flag) {
        if (ivRight != null) {
            ivRight.setVisibility((flag & FLAG_ICON) == FLAG_ICON ? View.VISIBLE : View.GONE);
        }

        if (tvRight != null) {
            tvRight.setVisibility((flag & FLAG_TEXT) == FLAG_TEXT ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 显示左边ImageView
     *
     * @param flag
     */
    public void showLeft(int flag) {
        if (ivLeft != null) {
            ivLeft.setVisibility((flag & FLAG_ICON) == FLAG_ICON ? View.VISIBLE : View.GONE);
        }

        if (tvLeft != null) {
            tvLeft.setVisibility((flag & FLAG_TEXT) == FLAG_TEXT ? View.VISIBLE : View.GONE);
        }
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public TextView getTvLeft() {
        return tvLeft;
    }

    public ImageView getIvLeft() {
        return ivLeft;
    }

    public ImageView getIvRight() {
        return ivRight;
    }
}
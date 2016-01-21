package com.github.dotkebi.clearableedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * @author by dotkebi on 2016. 1. 21..
 *
 * fork from https://github.com/yanchenko/droidparts/blob/develop/droidparts/src/org/droidparts/widget/ClearableEditText.java
 *
 */
public class ClearableEditText extends EditText implements View.OnTouchListener, View.OnFocusChangeListener {
    private Context context;

    private int buttonWidth;
    private int buttonHeight;
    private int drawableId = android.R.drawable.presence_offline;

    private Drawable clearButton;

    public interface OnClearTextListener {
        void didClearText();
    }

    private OnClearTextListener onClearTextListener;
    public void setOnClearTextListener(OnClearTextListener onClearTextListener) {
        this.onClearTextListener = onClearTextListener;
    }


    private OnTouchListener onTouchListener;
    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    private OnFocusChangeListener onFocusChangeListener;
    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }

    /**
     * constructor
     */
    public ClearableEditText(Context context) {
        super(context);
        init(context);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        int[] textSizeAttr = new int[] { android.R.attr.drawable };
        int indexOfAttrTextSize = 0;

        TypedArray a = context.obtainStyledAttributes(attributeSet, textSizeAttr);
        drawableId = a.getResourceId(indexOfAttrTextSize, android.R.drawable.presence_offline);
        a.recycle();

        init(context);
    }

    private void init(Context context) {
        this.context = context;

        clearButton = getCompoundDrawables()[2];
        if (clearButton == null) {
            clearButton = ContextCompat.getDrawable(context, drawableId);
        }
        buttonWidth = (int) (clearButton.getIntrinsicWidth() * 1.1);
        buttonHeight = (int) (clearButton.getIntrinsicHeight() * 1.1);
        clearButton.setBounds(0, 0, buttonWidth, buttonHeight);

        setClearIconVisible(false);
        setOnTouchListener(this);
        setOnFocusChangeListener(this);
        addTextChangedListener(hasContents);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            int right = getWidth() - getPaddingRight();
            int left = right - buttonWidth;
            int bottom = getHeight() - getPaddingBottom();
            int top = bottom - buttonHeight;

            boolean tappedX = new Rect(left, top, right, bottom).contains((int) event.getX(), (int) event.getY());
            if (tappedX) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setText("");
                    if (onClearTextListener != null) {
                        onClearTextListener.didClearText();
                    }
                }
                return true;
            }
        }
        if (onTouchListener != null) {
            onTouchListener.onTouch(v, event);
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        setClearIconVisible(
                (hasFocus) && !TextUtils.isEmpty(getText())
        );
        if (onFocusChangeListener != null) {
            onFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    private TextWatcher hasContents = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setClearIconVisible(
                    isFocused() && !TextUtils.isEmpty(getText())
            );
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    private void setClearIconVisible(boolean visible) {
        boolean wasVisible = (getCompoundDrawables()[2] != null);
        if (visible != wasVisible) {
            Drawable x = visible ? clearButton : null;
            setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], x, getCompoundDrawables()[3]);
        }
    }
}
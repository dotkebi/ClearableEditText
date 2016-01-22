package com.github.dotkebi.clearableedittext;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnFocusChangeListener{

    private EditText editText;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.edit_contents);
        imageView = (ImageView) findViewById(R.id.edit_icon);

        imageView.setOnTouchListener(this);
        editText.setOnFocusChangeListener(this);
        editText.addTextChangedListener(contentWatcher);


        editText.setText("sdfsdfsdfsfsdfsdfsdfsdfsdfsdfsdf");

        ClearableEditText clearableEditText = (ClearableEditText) findViewById(R.id.test);
        clearableEditText.setText("jjjfjfjjfksjfksjfksjfkshfkshksh");


        //imageView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        setClearIconVisible(
                hasFocus && hasContents()
        );
    }

    private boolean isTouchInView(View view, MotionEvent event) {
        Rect hitBox = new Rect();
        view.getGlobalVisibleRect(hitBox);
        return hitBox.contains((int) event.getRawX(), (int) event.getRawY());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
            if (isTouchInView(imageView, event)) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    editText.setText("");
                }
                return true;
            }

        return false;
    }

    private TextWatcher contentWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            setClearIconVisible(
                    editText.isFocused() && hasContents()
            );
        }

        @Override
        public void afterTextChanged(Editable s) {
            setClearIconVisible(
                    editText.isFocused() && hasContents()
            );
        }
    };

    private void setClearIconVisible(boolean visible) {
        imageView.setVisibility((visible) ? View.VISIBLE : View.INVISIBLE);
    }

    private boolean hasContents() {
        return !TextUtils.isEmpty(editText.getText());
    }
}

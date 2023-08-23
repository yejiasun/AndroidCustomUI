package com.tengteng.ui.text;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;

import com.tengteng.ui.R;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 渐变文字
 */
public class TextActivity extends AppCompatActivity {
private CustomTextView customTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        customTextView =findViewById(R.id.customTextView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startChangeColor();
            }
        },2000);
    }

    /**
     * 渐变式修改颜色
     */
    private void startChangeColor() {
        ObjectAnimator.ofFloat(customTextView,"percent",0,1).setDuration(5000).start();
    }
}
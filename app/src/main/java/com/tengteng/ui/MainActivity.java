package com.tengteng.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tengteng.ui.fish.FishActivity;
import com.tengteng.ui.flowlayout.FlowLayoutActivity;
import com.tengteng.ui.recycleview.RecycleViewActivity;
import com.tengteng.ui.text.TextActivity;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_text_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到渐变文字页面
                gotoNext(TextActivity.class);
            }
        });
        findViewById(R.id.tv_recycleview_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到自定义RecycleView页面
                gotoNext(RecycleViewActivity.class);
            }
        });
        findViewById(R.id.tv_fish_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到fish页面
                gotoNext(FishActivity.class);
            }
        });
        findViewById(R.id.tv_flowlayout_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到流式布局页面
                gotoNext(FlowLayoutActivity.class);
            }
        });

    }

    /**
     * 跳转到下一页
     *
     * @param nextActivity
     */
    private void gotoNext(Class<?> nextActivity) {
        startActivity(new Intent(this, nextActivity));
    }
}
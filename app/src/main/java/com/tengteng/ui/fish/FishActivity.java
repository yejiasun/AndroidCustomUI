package com.tengteng.ui.fish;

import android.os.Bundle;
import android.widget.ImageView;

import com.tengteng.ui.R;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author yejiasun
 * 描述：会摆动的鱼
 */
public class FishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fish);
        //测试绘画鱼的样式
        ImageView iv = findViewById(R.id.iv_fish);
        iv.setImageDrawable(new CustomFishDrawable());
    }
}
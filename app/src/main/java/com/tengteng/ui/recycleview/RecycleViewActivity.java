package com.tengteng.ui.recycleview;

import android.os.Bundle;
import android.renderscript.ScriptGroup;

import com.tengteng.ui.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 自定义吸顶 RrecycleView
 */
public class RecycleViewActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<CustomItemBean> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview);
        fackDataTTList();
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new CustomAdapter(this, mList));
        mRecyclerView.addItemDecoration(new CustomDecoration(this,60));

    }

    /***
     * 创建展示数据
     */
    private void fackDataTTList() {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 16; j++) {
                if (i % 2 == 0) {
                    mList.add(new CustomItemBean("Summer ", "Sun" + j));
                } else {
                    mList.add(new CustomItemBean("Winter ", "Moon" + j));

                }
            }
        }
    }


}
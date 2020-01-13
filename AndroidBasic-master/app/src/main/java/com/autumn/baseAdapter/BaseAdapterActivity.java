package com.autumn.baseAdapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.autumn.R;

import java.util.ArrayList;
import java.util.List;

public class BaseAdapterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_adapter);
        List<ItemBean> itemBeans = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            itemBeans.add(new ItemBean(R.mipmap.ic_launcher,"我是标题 "+i,"我是内容"));
        }

        ListView listView = findViewById(R.id.lv_base_adapter);
        listView.setAdapter(new MyAdapter(this, itemBeans));
    }

}

package com.autumn.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.autumn.R;

public class ContainerActivity extends AppCompatActivity {

    AFragment aFragment;
    BFragment bFragment;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_container);

            aFragment = AFragment.newInstance("i am bob");

//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if(fragment==null){
//                        fragment = new BFragment();
//                    }else{
//                        if (fragment instanceof AFragment){
//                            fragment = new BFragment();
//                        }else{
//                            fragment = new AFragment();
//                        }
//                    }
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fragment).commitAllowingStateLoss();
//
//                }
//            });
           getSupportFragmentManager().beginTransaction().add(R.id.fl_container, aFragment,"ad").commitAllowingStateLoss();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

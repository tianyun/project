package com.autumn.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.autumn.R;

public class AFragment extends Fragment {

    private TextView mTvTitle;
    private Activity mActivity;
    private Button mButtonChangeToB;
    private Button mButtonChangeText;
    private BFragment bFragment;

    public static AFragment newInstance(String title){
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        AFragment aFragment = new AFragment();
        aFragment.setArguments(bundle);
        return aFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvTitle = view.findViewById(R.id.tv_title_A);
        mButtonChangeToB = view.findViewById(R.id.btn_change_b);
        mButtonChangeText = view.findViewById(R.id.btn_txt_reset);

        mButtonChangeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvTitle.setText("i am new===="); ;
            }
        });

        mButtonChangeToB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bFragment==null){
                    bFragment = new BFragment();
                }
                Fragment fragment = getFragmentManager().findFragmentByTag("ad");
                if (fragment!=null){
                    getFragmentManager().beginTransaction().hide(fragment).add(R.id.fl_container,bFragment).addToBackStack(null).commitAllowingStateLoss();
                }

            }
        });

        if(getArguments()!=null){
            mTvTitle.setText(getArguments().getString("title"));
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消异步

    }
}

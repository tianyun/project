package com.autumn.handler;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.autumn.R;
import com.autumn.util.ToastUtil;

public class HandlerActivity extends AppCompatActivity {

    private TextView textView;
    private Button buttonCh;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            Toast.makeText(getApplicationContext(),"22222",1).show();
            return true;
        }
    }){
        @Override
        public void handleMessage(Message msg) {
            ToastUtil.showMsg(getApplicationContext(),""+msg.arg1);

        }
    };

    private String TAG ="handlerActivity";

    private ImageView imageView;

    private int imgage[] ={R.drawable.image1, R.drawable.image2,R.drawable.image3};

    private int index;

    private MyRunnable myRunnable = new MyRunnable();

    class Person{
        private int age;
        private String name;

        public Person(int age, String name) {
            this.age = age;
            this.name = name;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "age=" + age +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    class MyRunnable implements Runnable{

        @Override
        public void run() {
            index++;
            index = index%3;
            imageView.setImageResource(imgage[index]);
            handler.postDelayed(myRunnable, 1000);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        textView = findViewById(R.id.tv_handler_title);
        imageView = findViewById(R.id.handler_image_view);
        buttonCh  = findViewById(R.id.btn_handler_ch);

//        handler.postDelayed(myRunnable,1000);
        buttonCh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message message = handler.obtainMessage();
                message.arg1=1;
                handler.sendMessage(message);
            }
        });

//        new Thread(){
//            public void run(){
//                try{
//                    Thread.sleep(2000);
//                    Message message = new Message();
//                    message.arg1=100;
//                    message.arg2=400;
//                    Person person = new Person(11,"zty");
//                    message.obj = person;
//
//                    handler.sendMessage(message);
//                }catch (InterruptedException e){
//                    e.printStackTrace();
//                }
//            }
//
//        }.start();

//        Log.i(TAG,"=====================");
//        Log.i(TAG, "当前线程ID: "+Thread.currentThread().getId());
//        Log.i(TAG, "主线程ID: "+getMainLooper().getThread().getId());
//        Log.i(TAG, "当前Activity所在栈的ID: "+getTaskId());
//        Log.i(TAG, "当前调用该应用程序的用户号: "+getApplicationInfo().uid);
//        Log.i(TAG, "当前调用该应用程序的进程名: "+getApplicationInfo().processName);
//
//        new Thread(){
//
//            @Override
//            public void run(){
//                try{
//                    Log.i(TAG,"=====================");
//                    Log.i(TAG, "当前线程ID: "+Thread.currentThread().getId());
//                    Log.i(TAG, "主线程ID: "+getMainLooper().getThread().getId());
//                    Log.i(TAG, "当前Activity所在栈的ID: "+getTaskId());
//                    Log.i(TAG, "当前调用该应用程序的用户号: "+getApplicationInfo().uid);
//                    Log.i(TAG, "当前调用该应用程序的进程名: "+getApplicationInfo().processName);
//                    Thread.sleep(1000);
//
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.i(TAG,"=====================");
//                            Log.i(TAG, "当前线程ID: "+Thread.currentThread().getId());
//                            Log.i(TAG, "主线程ID: "+getMainLooper().getThread().getId());
//                            Log.i(TAG, "当前Activity所在栈的ID: "+getTaskId());
//                            Log.i(TAG, "当前调用该应用程序的用户号: "+getApplicationInfo().uid);
//                            Log.i(TAG, "当前调用该应用程序的进程名: "+getApplicationInfo().processName);
//                            textView.setText("update thread");
//
//                        }
//                    });
//                }catch (InterruptedException e){
//                    e.printStackTrace();
//                }
//            }
//
//        }.start();

    }


}

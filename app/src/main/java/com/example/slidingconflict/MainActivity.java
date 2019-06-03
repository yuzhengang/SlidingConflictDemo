package com.example.slidingconflict;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    XHorizontalScrollView  xHorizontalScrollView;
    DisplayMetrics metric;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        initView();
    }
    private  void  initView(  ){
        LayoutInflater inflater=getLayoutInflater();
        xHorizontalScrollView=findViewById(R.id.container);
        int screenwidth = metric.widthPixels;     // 屏幕宽度（像素）
        for (int i=0;i<3;i++){
            ViewGroup  layout= (ViewGroup) inflater.inflate(R.layout.content_layout,xHorizontalScrollView,false);
            layout.getLayoutParams().width=screenwidth;
            if(i==0){
                layout.setBackgroundColor(getResources().getColor(R.color.red));
            }else if(i==1){
                layout.setBackgroundColor(getResources().getColor(R.color.blue));
            }else{
                layout.setBackgroundColor(getResources().getColor(R.color.green));
            }
            createList(layout);
            xHorizontalScrollView.addView(layout);
        }
    }

   private  void  createList(ViewGroup  layout){
       ListView  listView=layout.findViewById(R.id.listview);
       ArrayList<String>   datas=new ArrayList<>();
       for (int i=0;i<50;i++){
           datas.add("name"+i);
       }
       String[] desc = new String[]{};
       desc = datas.toArray(desc);
       ArrayAdapter<String>   arrayadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, desc);
       listView.setAdapter(arrayadapter);
   }
}









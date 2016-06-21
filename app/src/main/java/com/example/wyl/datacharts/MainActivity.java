package com.example.wyl.datacharts;

import android.app.Activity;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity  extends Activity {
    private MyView canvasView;
    List<Point> lp,lp2;
    boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getViews();

    }

    private void getViews() {
        canvasView = (MyView) findViewById(R.id.canvasView);
        lp = new ArrayList<>();

        Point p1= new Point(600,500);
        Point p2= new Point(440,400);
        Point p3= new Point(600,500);
        Point p4= new Point(550,500);
        Point p5= new Point(490,400);
        Point p6= new Point(600,500);
        Point p7= new Point(630,510);
        lp.add(p1);
        lp.add(p2);
        lp.add(p3);
        lp.add(p4);
        lp.add(p5);
        lp.add(p6);
        lp.add(p7);

        lp2= new ArrayList<>();

        Point p12= new Point(400,300);
        Point p22= new Point(470,450);
        Point p32= new Point(650,500);
        Point p42= new Point(510,500);
        Point p52= new Point(260,250);
        Point p62= new Point(640,540);
        Point p72= new Point(650,530);

        lp2.add(p12);
        lp2.add(p22);
        lp2.add(p32);
        lp2.add(p42);
        lp2.add(p52);
        lp2.add(p62);
        lp2.add(p72);

        canvasView.setPaintsList(lp);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                canvasView.setPaintsList(lp2);
                canvasView.invalidate();
            }
        },1000);

        (findViewById(R.id.btnClearPoints)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(flag==false)
            {
                canvasView.setPaintsList(lp2);
                canvasView.invalidate();
                flag = true;
            }else
            {
                canvasView.setPaintsList(lp);
                canvasView.invalidate();
                flag = false;
            }

            }
        });
    }
}
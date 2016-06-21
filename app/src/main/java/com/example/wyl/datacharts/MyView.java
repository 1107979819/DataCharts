package com.example.wyl.datacharts;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
/**
 * Created by WYL on 2016/6/21.
 */
public class MyView extends View {
    /**
     * 重要参数，两点之间分为几段描画，数字愈大分段越多，描画的曲线就越精细.
     */
    private static final int STEPS = 12;
    Paint paint;
    Path linePath;
    Path curvePath;
    List<Point> points;
    List<Integer> points_x;
    List<Integer> points_y;

    private int swidth  =1080;
    boolean drawLineFlag;
    boolean drawCurveFlag;

    /**
     * struct.
     *
     * @param context
     */
    public MyView(Context context) {
        super(context);

        initObj();
    }

    /**
     * struct.
     *
     * @param context
     * @param attrs
     */
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initObj();
    }

    /**
     * struct.
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public MyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initObj();
    }

    /**
     * 计算曲线.
     *
     * @param x
     * @return
     */
    private List<Cubic> calculate(List<Integer> x) {
        int n = x.size() - 1;
        float[] gamma = new float[n + 1];
        float[] delta = new float[n + 1];
        float[] D = new float[n + 1];
        int i;
		/*
		 * We solve the equation [2 1 ] [D[0]] [3(x[1] - x[0]) ] |1 4 1 | |D[1]|
		 * |3(x[2] - x[0]) | | 1 4 1 | | . | = | . | | ..... | | . | | . | | 1 4
		 * 1| | . | |3(x[n] - x[n-2])| [ 1 2] [D[n]] [3(x[n] - x[n-1])]
		 *
		 * by using row operations to convert the matrix to upper triangular and
		 * then back sustitution. The D[i] are the derivatives at the knots.
		 */

        gamma[0] = 1.0f / 2.0f;
        for (i = 1; i < n; i++) {
            gamma[i] = 1 / (4 - gamma[i - 1]);
        }
        gamma[n] = 1 / (2 - gamma[n - 1]);

        delta[0] = 3 * (x.get(1) - x.get(0)) * gamma[0];
        for (i = 1; i < n; i++) {
            delta[i] = (3 * (x.get(i + 1) - x.get(i - 1)) - delta[i - 1])
                    * gamma[i];
        }
        delta[n] = (3 * (x.get(n) - x.get(n - 1)) - delta[n - 1]) * gamma[n];

        D[n] = delta[n];
        for (i = n - 1; i >= 0; i--) {
            D[i] = delta[i] - gamma[i] * D[i + 1];
        }

		/* now compute the coefficients of the cubics */
        List<Cubic> cubics = new LinkedList<Cubic>();
        for (i = 0; i < n; i++) {
            Cubic c = new Cubic(x.get(i), D[i], 3 * (x.get(i + 1) - x.get(i))
                    - 2 * D[i] - D[i + 1], 2 * (x.get(i) - x.get(i + 1)) + D[i]
                    + D[i + 1]);
            cubics.add(c);
        }
        return cubics;
    }



    private List<Cubic> calculateXTime(int listSize){
        int n = listSize - 1;
        float rw = swidth/n;
        Log.i("Test",rw+" "+swidth);

        float[] gamma = new float[n + 1];
        float[] delta = new float[n + 1];
        float[] D = new float[n + 1];
        int i;
		/*
		 * We solve the equation [2 1 ] [D[0]] [3(x[1] - x[0]) ] |1 4 1 | |D[1]|
		 * |3(x[2] - x[0]) | | 1 4 1 | | . | = | . | | ..... | | . | | . | | 1 4
		 * 1| | . | |3(x[n] - x[n-2])| [ 1 2] [D[n]] [3(x[n] - x[n-1])]
		 *
		 * by using row operations to convert the matrix to upper triangular and
		 * then back sustitution. The D[i] are the derivatives at the knots.
		 */

        gamma[0] = 1.0f / 2.0f;
        for (i = 1; i < n; i++) {
            gamma[i] = 1 / (4 - gamma[i - 1]);
        }
        gamma[n] = 1 / (2 - gamma[n - 1]);

//        delta[0] = 3 * (x.get(1) - x.get(0)) * gamma[0];
        delta[0] = 3 * (rw ) * gamma[0];


        for (i = 1; i < n; i++) {
//            delta[i] = (3 * (x.get(i + 1) - x.get(i - 1)) - delta[i - 1])  * gamma[i];
            delta[i] = (3 * (rw*(i + 1) - rw*(i - 1)) - delta[i - 1])  * gamma[i];
        }
//        delta[n] = (3 * (x.get(n) - x.get(n - 1)) - delta[n - 1]) * gamma[n];
        delta[n] = (3 * (rw*(n) - rw*(n - 1)) - delta[n - 1]) * gamma[n];

        D[n] = delta[n];
        for (i = n - 1; i >= 0; i--) {
            D[i] = delta[i] - gamma[i] * D[i + 1];
        }

		/* now compute the coefficients of the cubics */
        List<Cubic> cubics = new LinkedList<Cubic>();
        for (i = 0; i < n; i++) {
//            Cubic c = new Cubic(x.get(i), D[i], 3 * (x.get(i + 1) - x.get(i))- 2 * D[i] - D[i + 1], 2 * (x.get(i) - x.get(i + 1)) + D[i] + D[i + 1]);
            Cubic c = new Cubic(rw*(i), D[i], 3 * (rw*(i + 1) - rw*(i))- 2 * D[i] - D[i + 1], 2 * (rw*(i) - rw*(i + 1)) + D[i] + D[i + 1]);
            cubics.add(c);
        }
        return cubics;
    }


    private List<Cubic> calculateYTime(int listSize){
        int n = listSize - 1;
        float rw = swidth/n;
        Log.i("Test",rw+" "+swidth);

        float[] gamma = new float[n + 1];
        float[] delta = new float[n + 1];
        float[] D = new float[n + 1];
        int i;
		/*
		 * We solve the equation [2 1 ] [D[0]] [3(x[1] - x[0]) ] |1 4 1 | |D[1]|
		 * |3(x[2] - x[0]) | | 1 4 1 | | . | = | . | | ..... | | . | | . | | 1 4
		 * 1| | . | |3(x[n] - x[n-2])| [ 1 2] [D[n]] [3(x[n] - x[n-1])]
		 *
		 * by using row operations to convert the matrix to upper triangular and
		 * then back sustitution. The D[i] are the derivatives at the knots.
		 */

        gamma[0] = 1.0f / 2.0f;
        for (i = 1; i < n; i++) {
            gamma[i] = 1 / (4 - gamma[i - 1]);
        }
        gamma[n] = 1 / (2 - gamma[n - 1]);

//        delta[0] = 3 * (x.get(1) - x.get(0)) * gamma[0];
        delta[0] = 3 * ((swidth-rw )) * gamma[0];


        for (i = 1; i < n; i++) {
//            delta[i] = (3 * (x.get(i + 1) - x.get(i - 1)) - delta[i - 1])  * gamma[i];
            delta[i] = (3 * (swidth-(rw*(i + 1) )- (swidth-rw*(i - 1))) - delta[i - 1])  * gamma[i];
        }
//        delta[n] = (3 * (x.get(n) - x.get(n - 1)) - delta[n - 1]) * gamma[n];
        delta[n] = (3 * ((swidth-rw*(n)) - (swidth-rw*(n - 1))) - delta[n - 1]) * gamma[n];

        D[n] = delta[n];
        for (i = n - 1; i >= 0; i--) {
            D[i] = delta[i] - gamma[i] * D[i + 1];
        }

		/* now compute the coefficients of the cubics */
        List<Cubic> cubics = new LinkedList<Cubic>();
        for (i = 0; i < n; i++) {
//            Cubic c = new Cubic(x.get(i), D[i], 3 * (x.get(i + 1) - x.get(i))- 2 * D[i] - D[i + 1], 2 * (x.get(i) - x.get(i + 1)) + D[i] + D[i + 1]);
            Cubic c = new Cubic((swidth-rw*(i)), D[i], 3 * ((swidth-rw*(i + 1)) -(swidth- rw*(i)))- 2 * D[i] - D[i + 1], 2 * (swidth-(rw*(i)) - (swidth-rw*(i + 1))) + D[i] + D[i + 1]);
            cubics.add(c);
        }
        return cubics;
    }
    /**
     * 画曲线.
     *
     * @param canvas
     */
    private void drawCurve(Canvas canvas) {
        paint.setColor(Color.RED);
        points_x.clear();
        points_y.clear();
        for (int i = 0; i < points.size(); i++) {
            points_x.add(points.get(i).x);
            points_y.add(points.get(i).y);
        }

        List<Cubic> calculate_time = calculateXTime(points_x.size());
        List<Cubic> calculate_y = calculate(points_y);


        //        Collections.reverse(points_x);
//        calculate_time.clear();
//        calculate_time = calculateYTime(points_x.size());
        List<Cubic> calculate_x = calculate(points_x);

        Log.i("Test",">>---------------------------------<<");
        float lastEndPointX=0;//这条线最后一个点的x轴
        curvePath .moveTo(calculate_time.get(0).eval(0), calculate_y.get(0).eval(0));
        for (int i = 0; i < calculate_time.size(); i++) {
            for (int j = 1; j <= STEPS; j++) {
                float u = j / (float) STEPS;
                Log.i("Test","U:"+u+" "+STEPS);
                curvePath.lineTo(calculate_time.get(i).eval(u), calculate_y.get(i) .eval(u));
                lastEndPointX = calculate_time.get(i).eval(u);
//                canvas.drawCircle(calculate_time.get(i).eval(u), calculate_y.get(i) .eval(u),10, paint);
            }
            Log.i("Test","- - ");
        }
//        canvas.drawPath(curvePath, paint);


        curvePath.lineTo(lastEndPointX,calculate_x.get(calculate_time.size()-1).eval(1));
//        curvePath.lineTo(0,800);
//        curvePath.lineTo(0,calculate_y.get(0).eval(0));



//        curvePath .moveTo(lastEndPointX,calculate_x.get(calculate_time.size()-1).eval(1));
        Log.i("Test","----------------88------------------");
        for (int i =calculate_time.size()-1; i >=0; i--) {//i>=0


            for (int j = STEPS-1; j >=0; j--) {
                float u = j / (float) STEPS;
                Log.i("Test","U:"+u+" "+STEPS);
                curvePath.lineTo(calculate_time.get(i).eval(u), calculate_x.get(i) .eval(u));

//                canvas.drawCircle(calculate_time.get(i).eval(u), calculate_x.get(i) .eval(u),10, paint);
            }
            Log.i("Test","- - ");

        }

        curvePath.close();
        canvas.drawPath(curvePath, paint);
        Log.i("Test","<<--------------------------------->>");


    }

    /**
     * 画点.
     *
     * @param canvas
     */
    private void drawPoints(Canvas canvas) {
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            canvas.drawCircle(swidth/(points.size()-1)*i, p.y, 5, paint);
            canvas.drawCircle(swidth/(points.size()-1)*i, p.x, 5, paint);
        }
    }

    /**
     * 初始化.
     */
    private void initObj() {
        paint = new Paint();
        linePath = new Path();
        curvePath = new Path();
        points = new LinkedList<Point>();
        points_x = new LinkedList<Integer>();
        points_y = new LinkedList<Integer>();
        drawLineFlag = true;
        drawCurveFlag = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        swidth  =getWidth();

        canvas.scale(0.98f,1f);
//        canvas.translate(100,0);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5);

        drawPoints(canvas);

        linePath.reset();
        curvePath.reset();

        drawCurve(canvas);
    }



    public void setPaintsList(List<Point> ps)
    {
        points.clear();
        points.addAll(ps);
    }

}
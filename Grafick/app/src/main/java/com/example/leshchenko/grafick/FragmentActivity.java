package com.example.leshchenko.grafick;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


public class FragmentActivity extends Fragment {
    private final Handler mHandler = new Handler();
    private Runnable mTimer;
    //Объкты линий
    private LineGraphSeries<DataPoint> mSeries1;
    private LineGraphSeries<DataPoint> mSeries2;
    // тект над графиком
    private TextView textPM10;
    private  TextView textPM2 ;

    ArrayList<Double>lineA;
    ArrayList<Double>lineB;
    private double graphValueXLineA = 2d;
    private double graphValueXLineB = 5d;
    private String pm10;
    private String pm2;


    private OnFragmentInteractionListener mListener;


    public FragmentActivity() {

    }


    public static FragmentActivity newInstance() {
        FragmentActivity fragment = new FragmentActivity();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment, container, false);
        GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
        //Текст куда записываю рандомные числа
         textPM10 = (TextView) rootView.findViewById(R.id.text_view_pm10);
         textPM2 = (TextView) rootView.findViewById(R.id.text_view_pm2);

        //Создаю первую линию
        mSeries1 = new LineGraphSeries<>();
        //Делаю этой лини настройки, цвет,включение точек, радиус точки,вывод на график последние показание точки, плюс тост при нажатие на точку.
        mSeries1.setColor(Color.CYAN);
        mSeries1.setDrawDataPoints(true);
        mSeries1.setDataPointsRadius(20);
        mSeries1.setThickness(7);
        mSeries1.setTitle("PM2 = " + pm2);
        mSeries1.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(), "PM5: "+dataPoint, Toast.LENGTH_SHORT).show();
            }
        });
        //Создаю вторую линию и настройки к ней
       mSeries2 = new LineGraphSeries<>();

        mSeries2.setColor(Color.GREEN);
        mSeries2.setDrawDataPoints(true);
        mSeries2.setDataPointsRadius(20);
        mSeries2.setThickness(7);
        mSeries2.setTitle("PM10 = " + pm10);

        // Значение по вертикали и горизонтали, подписи.
        graph.getLegendRenderer().setVisible(true);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
        {
            @Override
            public String formatLabel(double value,boolean isValueX){
                if(isValueX){
                    return "Min " + super.formatLabel(value,isValueX);
                }else {
                    return "Pm " + super.formatLabel(value, isValueX);
                }
            }
        });
        // Добавляю в график линию 1 и 2, а также настройки графика, сролл, мин и мак значения по х и y
        graph.addSeries(mSeries1);
        graph.addSeries(mSeries2);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
        graph.setBackgroundColor(Color.GRAY);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().isXAxisBoundsManual();
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(10);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);
        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // тут происходит магия постоянной работы
        mTimer = new Runnable() {
            @Override
            public void run() {
                graphValueXLineA += 1d;
                graphValueXLineB += 1d;
                    mSeries1.appendData(new DataPoint(graphValueXLineA,pointYLineA()),true,40);
                    mSeries2.appendData(new DataPoint(graphValueXLineB,pointYLineB()),true,40);

                mHandler.postDelayed(this, 300);
            }
        };
        mHandler.postDelayed(mTimer, 15000);
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer);
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private double pointYLineA(){
        double mLastRandom = 2;
        mLastRandom += mRand.nextDouble()*0.5 - 0.25;
        return mLastRandom;
    }


    private double pointYLineB(){
        double mLastRandom = 20;
         mLastRandom += mRand.nextDouble()*0.5 - 0.25;
        return mLastRandom;
    }
    Random mRand = new Random();
}

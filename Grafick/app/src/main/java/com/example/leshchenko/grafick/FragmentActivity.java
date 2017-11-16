package com.example.leshchenko.grafick;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
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
import java.util.concurrent.TimeUnit;


public class FragmentActivity extends Fragment {
    private final static String TAG = " FragmentActivity ";
    private final Handler mHandler = new Handler();
    private Runnable mTimer;
    private double timeA;
    private Date date;
    private final Date DATA = new Date();
    //Объкты линий
    private LineGraphSeries<DataPoint> mSeries1;
    private LineGraphSeries<DataPoint> mSeries2;
    // тект над графиком
    private TextView textPM10;
    private  TextView textPM2 ;

    ArrayList<Double>lineA;
    ArrayList<Double>lineB;
    private int graphValueXLineA = 2;
    private int graphValueXLineB = 3;
    private String pm10;
    private String pm2;
    private  int x = 1;
    private  double timeB;


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
        final GraphView graph = (GraphView) rootView.findViewById(R.id.graph);

        Log.e(TAG,"Start Date =" + DATA);
        timeA = DATA.getTime();
        Log.e(TAG,"Start MillSec = " + timeA);
        //Текст куда записываю рандомные числа
         textPM10 = (TextView) rootView.findViewById(R.id.text_view_pm10);
         textPM2 = (TextView) rootView.findViewById(R.id.text_view_pm2);

        //Создаю первую линию
        mSeries1 = new LineGraphSeries<>();
        mSeries1.setColor(Color.CYAN);
        mSeries1.setDrawDataPoints(true);
        mSeries1.setDataPointsRadius(20);
        mSeries1.setThickness(7);
        mSeries1.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(), "PM5: " + dataPoint, Toast.LENGTH_SHORT).show();
            }
        });

        Log.e(TAG,"graph Series1 " + graph.toString());

        //Создаю вторую линию и настройки к ней
       mSeries2 = new LineGraphSeries<>();
        mSeries2.setColor(Color.GREEN);
        mSeries2.setDrawDataPoints(true);
        mSeries2.setDataPointsRadius(20);
        mSeries2.setThickness(7);
        mSeries2.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(getActivity(), "PM10: " + dataPoint, Toast.LENGTH_SHORT).show();
            }
        });

      graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(20);
      graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(1);
        graph.getViewport().setMaxY(1000);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScrollableY(true);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);


        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
        {
            @Override
            public String formatLabel(double value,boolean isValueX){
                if (isValueX) {
                    return value != 0 ? super.formatLabel(value, isValueX) + "\nSec" : super.formatLabel(0, isValueX);
                } else {
                    Html.fromHtml("<sup>3</sup>");
                    return value != 0 ? super.formatLabel(value, isValueX) + "\n" + Html.fromHtml("<sup>pm</sup>") : super.formatLabel(0, isValueX);
                }
            }
        });
        graph.addSeries(mSeries1);
        graph.addSeries(mSeries2);
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
        try {
                mTimer = new Runnable() {
                    @Override
                    public void run() {
                        date = new Date();
                        timeB = date.getTime();
                        graphValueXLineA = timer(timeA, timeB);
                        graphValueXLineB = timer(timeA, timeB) - 1;
                        Log.e(TAG,"graphValueXLineA " + graphValueXLineA);
                        Log.e(TAG,"graphValueXLineB " + graphValueXLineB);
                        mSeries1.appendData(new DataPoint(graphValueXLineA, pointYLineA()), false, 180);
                        mSeries2.appendData(new DataPoint(graphValueXLineB, pointYLineB()), false, 180);
                        mHandler.postDelayed(this, 15_000);
                    }
                };
          mHandler.postDelayed(mTimer, 5_000);
            }catch(Exception e){
                e.printStackTrace();
        }
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
        mLastRandom += mRand.nextDouble()*900 - 2.25;
        textPM2.setText(String.valueOf(mLastRandom));
        return mLastRandom;
    }


    private double pointYLineB(){
        double mLastRandom = 5;
         mLastRandom += mRand.nextDouble()*500 - 0.25;
        textPM10.setText(String.valueOf(mLastRandom));
        return mLastRandom;
    }
    private int timer(double timeA, double timeB){
        //double result;
        int convertTime = 0;
        convertTime += (timeB - timeA) / 1000;
        Log.e(TAG,"convertTime " + convertTime);
       // String getStringTime = formatDuration(convertTime);
      //  result = Double.parseDouble(getStringTime);
        return convertTime;
    }

    private static String formatDuration(long duration) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60;
        return String.format("%02d.%02d" , minutes, seconds);
    }

    Random mRand = new Random();
}

package com.allanlopez.chartapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements OnChartGestureListener, OnChartValueSelectedListener {

    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineChart = (LineChart)findViewById(R.id.lineChart);
        lineChart.setOnChartGestureListener(this);
        lineChart.setOnChartValueSelectedListener(this);
        lineChart.setDrawGridBackground(false);

        data();
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        Description d = new Description();
        d.setText("Esto es una app demo");
        lineChart.setDescription(d);

        lineChart.setNoDataText("No Hay datos");
        LimitLine limitLine = new LimitLine(10f, "Maximo");
        limitLine.setLineWidth(5f);
        limitLine.setEnabled(true);
        limitLine.enableDashedLine(10f, 10f, 0f);
        limitLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        limitLine.setTextSize(12f);

        LimitLine downLimit = new LimitLine(-20f, "Minimo");
        downLimit.setLineWidth(6f);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.addLimitLine(limitLine);
        yAxis.addLimitLine(downLimit);
        yAxis.setAxisMaximum(20f);
        yAxis.setAxisMinimum(0f);
        yAxis.enableGridDashedLine(10f, 10f, 0f);
        yAxis.setDrawZeroLine(false);
        yAxis.setDrawLimitLinesBehindData(false);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.animateX(300, Easing.EasingOption.EaseInOutBack);
        lineChart.invalidate();

    }

    public void data(){
        ArrayList<String> xData = xValues();
        ArrayList<Entry> yData = yValues();
        LineDataSet set = new LineDataSet(yData, "data ONE");
        set.setColor(Color.BLACK);
        set.setCircleColor(Color.BLACK);
        set.setLineWidth(2f);
        set.setCircleRadius(3f);
        set.setDrawCircleHole(true);
        set.setValueTextSize(10f);
        set.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);

    }

    public ArrayList<String> xValues(){
        ArrayList<String> list = new ArrayList<>();
        list.add("10");
        list.add("20");
        list.add("30");
        list.add("40");
        list.add("50");
        return  list;

    }

    public ArrayList<Entry> yValues(){
        ArrayList<Entry> list = new ArrayList<>();
        list.add(new Entry(60, 0));
        list.add(new Entry(45, 1));
        list.add(new Entry(75.6f, 2));
        list.add(new Entry(100, 2));
        list.add(new Entry(150, 4));
        return  list;

    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}

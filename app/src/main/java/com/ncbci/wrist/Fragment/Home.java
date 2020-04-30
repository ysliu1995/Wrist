package com.ncbci.wrist.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.ncbci.wrist.C2TL;
import com.ncbci.wrist.CustomBarChartRender;
import com.ncbci.wrist.R;

import java.util.ArrayList;

public class Home extends Fragment {

    static final String TAG = "Home";
    private View v;
    private BarChart sleepChart;
    private String[] dateList = { "04/12", "04/13", "04/14", "04/15", "204/16", "04/17", "04/18" };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        v = view;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initChart();

//        new C2TL(v.getContext()).execute("好好玩");

    }
    private void initView() {
        sleepChart = v.findViewById(R.id.sleepChart);
    }
    private void initChart() {

        //initial Physical Activity chart
        int[] fakeData = {89, 70, 90, 90, 57, 82, 57};
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for(int i=0;i<fakeData.length;i++) {
            barEntries.add(new BarEntry(i, fakeData[i]));
        }

        ArrayList<String> xLabel = new ArrayList<>();
        for(int i=0;i<dateList.length;i++) {
            xLabel.add(dateList[i]);
        }



        //initial sleep chart
        sleepChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        sleepChart.getAxisRight().setEnabled(false);
        sleepChart.getDescription().setEnabled(false);
        sleepChart.getLegend().setEnabled(false);
        sleepChart.getAxisLeft().setDrawGridLines(false);
        sleepChart.getAxisLeft().setDrawLabels(false);
        sleepChart.getAxisRight().setDrawGridLines(false);
        sleepChart.getXAxis().setDrawGridLines(false);
//        sleepChart.getXAxis().setLabelCount(24);
        sleepChart.getAxisLeft().setAxisMinimum(0);
        sleepChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabel));
//        sleepChart.getXAxis().setLabelRotationAngle(-30);

        BarDataSet bardataset = new BarDataSet(barEntries, "Sleep");
        bardataset.setColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
        BarData data = new BarData(bardataset);
//        data.setDrawValues(false);
        sleepChart.setData(data);
        sleepChart.animateY(1000);

        CustomBarChartRender barChartRender = new CustomBarChartRender(sleepChart, sleepChart.getAnimator(), sleepChart.getViewPortHandler());
        barChartRender.setRadius(10);
        sleepChart.setRenderer(barChartRender);

    }
}

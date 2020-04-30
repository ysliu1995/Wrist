package com.ncbci.wrist.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.ncbci.wrist.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Activity extends Fragment {

    static final String TAG = "Activity";
    private View v;
    private HorizontalBarChart PAlevel;
    private LineChart cpmChart;
    private MaterialCalendarView activityCalendarView;
    private int Index = 0;
    private float[] sed = {0.72f, 0.5f, 0.65f, 0.62f, 0.69f, 0.75f, 0.72f};
    private float[] lpa = {0.27f, 0.44f, 0.36f, 0.39f, 0.27f, 0.25f, 0.24f};
    private float[] mvpa = {0.02f, 0.01f, 0, 0.01f, 0.05f, 0.01f, 0.05f};
    private String[] dateList = { "2020/04/12", "2020/04/13", "2020/04/14", "2020/04/15", "2020/04/16", "2020/04/17", "2020/04/18" };
//    private int[][] stage = { {} };
            @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, null);
        v = view;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initChart();

        activityCalendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);
        for(int i = 0;i<dateList.length;i++)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            try {
                Date date = sdf.parse(dateList[i]);
                activityCalendarView.setDateSelected(date, true);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    private void initView() {
        PAlevel = v.findViewById(R.id.PAlevel);
        activityCalendarView = v.findViewById(R.id.activityCalendarView);
        cpmChart = v.findViewById(R.id.cpmChart);

        activityCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                String select_day = dateFormat.format(date.getDate());
                activityCalendarView.setDateSelected(date, !selected);
                switch(select_day) {
                    case "2020/04/12":
                        Index = 0;
                        break;
                    case "2020/04/13":
                        Index = 1;
                        break;
                    case "2020/04/14":
                        Index = 2;
                        break;
                    case "2020/04/15":
                        Index = 3;
                        break;
                    case "2020/04/16":
                        Index = 4;
                        break;
                    case "2020/04/17":
                        Index = 5;
                        break;
                    case "2020/04/18":
                        Index = 6;
                        break;
                }
                PAlevel.getBarData().clearValues();
                ArrayList<BarEntry> barEntries = new ArrayList<>();
                barEntries.add(new BarEntry(2, mvpa[Index] * 1440));
                barEntries.add(new BarEntry(1, lpa[Index] * 1440));
                barEntries.add(new BarEntry(0, sed[Index] * 1440));
                BarDataSet bardataset = new BarDataSet(barEntries, "level");
                bardataset.setColors(ColorTemplate.VORDIPLOM_COLORS);
                BarData data = new BarData(bardataset);
//                data.setDrawValues(false);
                PAlevel.setData(data);
                PAlevel.animateY(500);

//                cpmChart.getLineData().clearValues();
//                ArrayList<Entry> entries = new ArrayList<>();
//                for(int i=0;i<stage[Index].length;i++){
//                    entries.add(new Entry(i, stage[Index][i]));
//                }
//                LineDataSet dataSet = new LineDataSet(entries, "cpm");
//                dataSet.setColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
//                dataSet.setFillColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
//                dataSet.setValueTextColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimaryDark));
//                dataSet.setFillAlpha(150);
//                dataSet.setDrawFilled(true);
//                dataSet.setDrawValues(false);
//                dataSet.setLineWidth(2f);
//                dataSet.setDrawCircles(false);
//                dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//                LineData data1 = new LineData(dataSet);
//                cpmChart.setData(data1);
//                cpmChart.animateY(500);
//                cpmChart.invalidate();
            }
        });
    }

    private void initChart() {
        //initial physical activity level chart
        int[] fakePAData = {0, 0, 0};
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for(int i=0;i<fakePAData.length;i++) {
            barEntries.add(new BarEntry(i, fakePAData[i]));
        }

        ArrayList<String> xLabel = new ArrayList<>();
        xLabel.add("靜態活動");
        xLabel.add("輕度活動");
        xLabel.add("中度活動");

        PAlevel.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        PAlevel.getAxisRight().setEnabled(false);
        PAlevel.getDescription().setEnabled(false);
        PAlevel.getLegend().setEnabled(false);
        PAlevel.getAxisLeft().setDrawGridLines(false);
        PAlevel.getAxisLeft().setDrawLabels(false);
        PAlevel.getAxisRight().setDrawGridLines(false);
        PAlevel.getXAxis().setDrawGridLines(false);
        PAlevel.getAxisLeft().setAxisMinimum(0);
        PAlevel.getXAxis().setLabelCount(3);
        PAlevel.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabel));

        BarDataSet bardataset = new BarDataSet(barEntries, "level");
        bardataset.setColors(ColorTemplate.VORDIPLOM_COLORS);
        BarData data = new BarData(bardataset);
//        data.setDrawValues(false);
        PAlevel.setData(data);
        PAlevel.animateY(1000);

        //initial cpm chart
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 15));

        LineDataSet dataSet = new LineDataSet(entries, "cpm");
        dataSet.setColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
        dataSet.setFillColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimary));
        dataSet.setValueTextColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimaryDark));
        dataSet.setFillAlpha(150);
        dataSet.setDrawFilled(true);
        dataSet.setDrawValues(false);
        dataSet.setLineWidth(2f);
        dataSet.setDrawCircles(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        cpmChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        cpmChart.getXAxis().setDrawLabels(false);
        cpmChart.getAxisRight().setEnabled(false);
        cpmChart.getDescription().setEnabled(false);
        cpmChart.getLegend().setEnabled(false);
        cpmChart.getAxisLeft().setDrawGridLines(false);
        cpmChart.getAxisRight().setDrawGridLines(false);
        cpmChart.getXAxis().setDrawGridLines(false);

        LineData data1 = new LineData(dataSet);
        cpmChart.setData(data1);
        cpmChart.invalidate();

        cpmChart.setVisibility(View.INVISIBLE);

    }
}

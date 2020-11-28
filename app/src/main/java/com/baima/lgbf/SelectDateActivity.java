package com.baima.lgbf;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * 选择日期的活动
 * 包括年月日时分
 */
public class SelectDateActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String EXTRA_TIMESTAMP = "timestamp";
    private static final Integer[] MONTHS_BIG = {1, 3, 5, 7, 8, 10, 12};
    private static final Integer[] MONTHS_SMALL = {4, 6, 9, 11};
    private Spinner sn_year;
    private Spinner sn_month;
    private Spinner sn_day;
    private Spinner sn_hour;
    private Spinner sn_minute;

    private List<String> titleList1;
    private ArrayAdapter<String> adapter1;
    private List<String> titleList2;
    private ArrayAdapter<String> adapter2;
    private List<String> titleList3;
    private ArrayAdapter<String> adapter3;
    private List<String> titleList4;
    private ArrayAdapter<String> adapter4;
    private ArrayAdapter<String> adapter5;
    private List<String> titleList5;
    private TextView tv_ok;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    /**
     * 年下拉框最小的年
     */
    private int minYear;
    /**
     * 年下拉 框要显示的年的数量
     */
    private int yearCount = 200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);

        initViews();

        initData();
    }

    /**
     * 初始化组件
     */
    private void initViews() {
        sn_year = findViewById(R.id.spinner_year);
        sn_month = findViewById(R.id.spinner_month);
        sn_day = findViewById(R.id.spinner_day);
        sn_hour = findViewById(R.id.spinner_minute);
        sn_minute = findViewById(R.id.sn_minute);
        tv_ok = findViewById(R.id.tv_ok);

        sn_year.setOnItemSelectedListener(this);
        sn_month.setOnItemSelectedListener(this);
        sn_day.setOnItemSelectedListener(this);
        sn_hour.setOnItemSelectedListener(this);
        sn_minute.setOnItemSelectedListener(this);
        tv_ok.setOnClickListener(this);
    }


    /**
     * 初始化数据
     */
    private void initData() {
        titleList1 = new ArrayList<>();
        adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titleList1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sn_year.setAdapter(adapter1);

        titleList2 = new ArrayList<>();
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titleList2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sn_month.setAdapter(adapter2);

        titleList3 = new ArrayList<>();
        adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titleList3);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sn_day.setAdapter(adapter3);

        titleList4 = new ArrayList<>();
        adapter4 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titleList4);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sn_hour.setAdapter(adapter4);

        titleList5 = new ArrayList<>();
        adapter5 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titleList5);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sn_minute.setAdapter(adapter5);

        Calendar calendar = Calendar.getInstance();
        Intent intent = getIntent();
        //如果有时间戳传过来就使用，如果 没有就用当前时间戳
        long timestamp = intent.getLongExtra(EXTRA_TIMESTAMP, calendar.getTimeInMillis());
        calendar.setTimeInMillis(timestamp);

//，下拉 框设置对应数据
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

//        年，
        minYear = year - yearCount / 2;
        for (int i = 0; i < yearCount; i++) {
            titleList1.add((i + minYear) + "年");
        }
        adapter1.notifyDataSetChanged();
        sn_year.setSelection(yearCount / 2);

        //月
        for (
                int i = 0;
                i < 12; i++)

        {
            adapter2.add((i + 1) + "月");
        }
        adapter2.notifyDataSetChanged();
        sn_month.setSelection(month);

        //日
        refreshDayData();
        sn_day.setSelection(day - 1);

        //时
        for (
                int i = 0;
                i < 24; i++)

        {
            titleList4.add(i + "时");
        }
        adapter4.notifyDataSetChanged();
        sn_hour.setSelection(hour);

        //分
        for (
                int i = 0;
                i < 60; i++)

        {
            titleList5.add(i + "分");
        }
        adapter5.notifyDataSetChanged();
        sn_minute.setSelection(minute);
    }

    /**
     * 刷新日下拉 框的数据
     */
    private void refreshDayData() {
        int days = 0;
        if (month + 1 == 2) {
            //二月
            days = year % 4 == 0 ? 29 : 28;
        } else {
            //大月小月
            days = Arrays.asList(MONTHS_BIG).contains(month + 1) ? 31 : 30;
        }

        titleList3.clear();
        for (int i = 0; i < days; i++) {
            titleList3.add((i + 1) + "日");
        }
        adapter3.notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_ok) {
            onSelectFinish();
        }
    }

    private void onSelectFinish() {
        //返回选择日期对应的时间戳
        Calendar calendar = Calendar.getInstance();
        int day = sn_day.getSelectedItemPosition() + 1;
        int hour = sn_hour.getSelectedItemPosition();
        int minute = sn_minute.getSelectedItemPosition();

        calendar.set(year, month, day, hour, minute, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIMESTAMP, calendar.getTimeInMillis());
        setResult(RESULT_OK, intent);
        finish();

    }

    ;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int parentId = parent.getId();
        if (parentId == R.id.spinner_year) {
            year = sn_year.getSelectedItemPosition() + minYear;
            refreshDayData();
        } else if (parentId == R.id.spinner_month) {
            month = sn_month.getSelectedItemPosition();
            refreshDayData();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

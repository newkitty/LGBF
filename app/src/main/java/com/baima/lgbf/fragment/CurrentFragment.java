package com.baima.lgbf.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baima.lgbf.R;
import com.baima.lgbf.SelectDateActivity;
import com.baima.lgbf.SelectRSGZActivity;
import com.baima.lgbf.enums.BaXue;
import com.baima.lgbf.util.LGBFUtil;

import java.util.Calendar;

public class CurrentFragment extends Fragment implements View.OnClickListener {

    private TextView tv_date;
    private TextView tv_refresh;
    private TextView tv_rsgz;
    private TextView tv_bagua;
    private TextView tv_xuewei;
    private TextView tv_location;
    private TextView tv_features;
    private TextView tv_clinical;
    /**
     * 第二个穴位的信息
     */
    private LinearLayout ll_second_info;
    private TextView tv_location_second;
    private TextView tv_features_second;
    private TextView tv_clinical_second;

    private String[] rgz;
    private String[] sgz;
    /**
     * 显示的时间对应的时间戳
     */
    private long timestamp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current, container, false);
        tv_date = view.findViewById(R.id.tv_date);
        tv_refresh = view.findViewById(R.id.tv_refresh);
        tv_rsgz = view.findViewById(R.id.tv_rsgz);
        tv_bagua = view.findViewById(R.id.tv_bagua);
        tv_xuewei = view.findViewById(R.id.tv_xuewei);

        tv_location = view.findViewById(R.id.tv_location);
        tv_features = view.findViewById(R.id.tv_features);
        tv_clinical = view.findViewById(R.id.tv_clinical);

        //第二个穴位
        ll_second_info = view.findViewById(R.id.ll_second_info);
        tv_location_second = view.findViewById(R.id.tv_location_second);
        tv_features_second = view.findViewById(R.id.tv_features_second);
        tv_clinical_second = view.findViewById(R.id.tv_clinical_second);

        tv_date.setOnClickListener(this);
        tv_refresh.setOnClickListener(this);
        tv_rsgz.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        timestamp = System.currentTimeMillis();
        refreshTime();
        refreshData();
    }

    private void refreshData() {
        tv_rsgz.setText(String.format("%s%s日%s%s时", rgz[0], rgz[1], sgz[0], sgz[1]));

        String baGua = LGBFUtil.getBaGua(rgz[0], rgz[1], sgz[0], sgz[1]);
        tv_bagua.setText(baGua + "卦");

        //穴位
        BaXue[] baXues = LGBFUtil.getBaXues(rgz[0], rgz[1], sgz[0], sgz[1]);
        //第一个穴位
        BaXue baXue = baXues[0];

        tv_xuewei.setText(baXues[0].name);
        tv_location.setText(baXue.location);
        tv_features.setText(baXue.features);
        tv_clinical.setText(baXue.clinical);

        ll_second_info.setVisibility(View.GONE);
        if (baXues.length > 1) {
            //如果 是两个穴位
            BaXue baXue1 = baXues[1];
            tv_xuewei.setText(String.format("女:%s, 男:%s", baXue.name, baXue1.name));
            tv_location_second.setText(baXue1.location);
            tv_features_second.setText(baXue1.features);
            tv_clinical_second.setText(baXue1.clinical);
            ll_second_info.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_date) {
            Intent intent = new Intent(getActivity(), SelectDateActivity.class);
            intent.putExtra(SelectDateActivity.EXTRA_TIMESTAMP, timestamp);
            startActivityForResult(intent, 0);
        } else if (id == R.id.tv_rsgz) {
            //选择日时干支
            Intent intent = new Intent(getActivity(), SelectRSGZActivity.class);
            startActivityForResult(intent, 1);
        } else if (id == R.id.tv_refresh) {
            //刷新，刷新日期刷新数据
            timestamp = System.currentTimeMillis();
            refreshTime();
            refreshData();
        }
    }

    private void refreshTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        tv_date.setText(String.format("%s年%s月%s日 %s时%s分", year, month, day, hour, minute));

        //获取 日时干支
        long timeInMillis = calendar.getTimeInMillis();
        sgz = LGBFUtil.getSGZ(timeInMillis);
        rgz = LGBFUtil.getRGZ(timeInMillis);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                //选择时间
                if (resultCode == getActivity().RESULT_OK) {
                    timestamp = data.getLongExtra(SelectDateActivity.EXTRA_TIMESTAMP, 0);
                    refreshTime();
                    refreshData();
                }
                break;
            case 1:
                //选择日时干支
                if (resultCode == getActivity().RESULT_OK) {
                    rgz = data.getStringArrayExtra(SelectRSGZFragment.EXTRA_RGZ);
                    sgz = data.getStringArrayExtra(SelectRSGZFragment.EXTRA_SGZ);
                    tv_date.setText("未知时间");
                    refreshData();
                }
                break;
        }
    }
}

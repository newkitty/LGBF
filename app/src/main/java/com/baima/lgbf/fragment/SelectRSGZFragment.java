package com.baima.lgbf.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.baima.lgbf.R;
import com.baima.lgbf.util.LGBFUtil;

import java.util.ArrayList;
import java.util.List;

public class SelectRSGZFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    public static final String EXTRA_RGZ = "rgz";
    public static final String EXTRA_SGZ = "sgz";

    private Spinner sn_rtg;
    private Spinner sn_rdz;
    private Spinner sn_sgz;
    private TextView tv_ok;
    /**
     * 日支集合
     */
    private List<String> rdzs;
    private ArrayAdapter<String> adapterRdzs;
    /**
     * 时干支集合
     */
    private List<String> sgzs;
    private ArrayAdapter<String> adapterSgzs;
    /**
     * 时干支的二维数组
     * 0索引是时干， 1索引是时支
     */
    private String[][] sgzArray2;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initViews();
    }

    private View initViews() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_select_rsgz, null);
        sn_rtg = view.findViewById(R.id.sn_rtg);
        sn_rdz = view.findViewById(R.id.sn_rdz);
        sn_sgz = view.findViewById(R.id.sn_sgz);

        tv_ok = view.findViewById(R.id.tv_ok);


        //日天干下拉 框
        ArrayAdapter<String> adapterRtgs = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, LGBFUtil.tgs);
        sn_rtg.setAdapter(adapterRtgs);

        //日地支下拉框
        rdzs = new ArrayList<>();
        adapterRdzs = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, rdzs);
        sn_rdz.setAdapter(adapterRdzs);

        //时干支下拉 框
        sgzs = new ArrayList<>();
        adapterSgzs = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, sgzs);
        sn_sgz.setAdapter(adapterSgzs);


        sn_rtg.setOnItemSelectedListener(this);
        sn_rdz.setOnItemSelectedListener(this);
        sn_sgz.setOnItemSelectedListener(this);
        tv_ok.setOnClickListener(this);
        return view;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int parentId = parent.getId();
        if (parentId == R.id.sn_rtg) {
            //日干的下拉框，
            //日支下拉框设置对应数据
            rdzs.clear();
            rdzs.addAll(LGBFUtil.getDZsByTGIndex(position));
            adapterRdzs.notifyDataSetChanged();

            //时干支下拉框设置对应数据
            sgzs.clear();
            sgzArray2 = LGBFUtil.getSGZs(position);
            for (String[] array : sgzArray2) {
                sgzs.add(array[0] + array[1]);
            }
            adapterSgzs.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_ok) {
            //确定
            String rtg = LGBFUtil.tgs[sn_rtg.getSelectedItemPosition()];
            String rdz = rdzs.get(sn_rdz.getSelectedItemPosition());
            String[] sgz = sgzArray2[sn_sgz.getSelectedItemPosition()];

            Intent intent = new Intent();
            intent.putExtra(EXTRA_RGZ, new String[]{rtg, rdz});
            intent.putExtra(EXTRA_SGZ, sgz);
            getActivity().setResult(getActivity().RESULT_OK, intent);
            getActivity().finish();
        }
    }
}

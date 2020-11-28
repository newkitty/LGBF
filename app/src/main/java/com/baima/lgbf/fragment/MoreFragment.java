package com.baima.lgbf.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.baima.lgbf.HelpActivity;
import com.baima.lgbf.util.DisclaimerUtil;
import com.baima.lgbf.util.PackageUtil;

public class MoreFragment extends ListFragment {

    private String[] titles = {"帮助", "版本", "免责", "关于"};
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String versionName = PackageUtil.getVersionName(getActivity());
        titles[1] = "版本" + versionName;

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, titles);
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        switch (position) {
            case 0:
startActivity(new Intent(getActivity(), HelpActivity.class));

                break;
            case 2:
                DisclaimerUtil.showDisclaimerDialog(getActivity());
                break;
        }
    }
}

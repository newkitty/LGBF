package com.baima.lgbf;

import android.app.ListActivity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_help);

        List<String> list = new ArrayList<>();
        AssetManager assets = getAssets();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = assets.open("readme.md");
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.equals("")) {
                    list.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(HelpActivity.this, android.R.layout.simple_list_item_1, list);
            setListAdapter(adapter);
        }
    }
}

package com.example.zuifuli_jd;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import static android.view.KeyEvent.KEYCODE_BACK;

public class Main2Activity extends AppCompatActivity {
    private static final String TAG = "Main2Activity";

    Spinner countSpinner;
    Spinner valueSpinner;
    Switch s;
    EditText ms;
    EditText start;
    EditText end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        countSpinner = findViewById(R.id.count_spinner);
        ArrayAdapter<String> countSpinnerAdapter = new ArrayAdapter<>(Main2Activity.this,
                R.layout.support_simple_spinner_dropdown_item, Constant.countArray);
        countSpinner.setAdapter(countSpinnerAdapter);

        valueSpinner = findViewById(R.id.value_spinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(Main2Activity.this,
                R.layout.support_simple_spinner_dropdown_item, Constant.valueArray);
        valueSpinner.setAdapter(spinnerAdapter);

        s = findViewById(R.id.switch1);
        ms = findViewById(R.id.editText);
        start = findViewById(R.id.editText2);
        end = findViewById(R.id.editText3);
        SharedPreferences perf = getSharedPreferences("config", MODE_PRIVATE);
        countSpinner.setSelection(perf.getInt("num", 0));
        valueSpinner.setSelection(perf.getInt("value", 0));
        s.setChecked(perf.getBoolean("switch", false));
        ms.setText(perf.getString("ms", "500"));
        start.setText(perf.getString("start", "20:59:55"));
        end.setText(perf.getString("end", "21:00:15"));

        if (!s.getShowText()) {
            ms.setEnabled(false);
            start.setEnabled(false);
            end.setEnabled(false);
        }
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                         @Override
                                         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                             if (isChecked) {
                                                 ms.setEnabled(true);
                                                 start.setEnabled(true);
                                                 end.setEnabled(true);
                                             } else {
                                                 ms.setEnabled(false);
                                                 start.setEnabled(false);
                                                 end.setEnabled(false);
                                             }
                                         }
                                     }
        );


    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        saveData();
//    }

    private void saveData() {
        Log.i(TAG, " start save data");
        SharedPreferences.Editor editor = getSharedPreferences("config", MODE_PRIVATE).edit();


        editor.putInt("num", countSpinner.getSelectedItemPosition());
        editor.putInt("value", valueSpinner.getSelectedItemPosition());
        editor.putBoolean("switch", s.getShowText());
        editor.putString("ms", ms.getText().toString());
        editor.putString("start", start.getText().toString());
        editor.putString("end", end.getText().toString());
        editor.apply();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK)) {
            saveData();
        }
        return super.onKeyDown(keyCode, event);
    }
}

package com.example.zuifuli_jd;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import static android.view.KeyEvent.KEYCODE_BACK;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private WebView myWebView;

    private boolean flag;

    private TextView textView;

    private FloatingActionButton fab;

    private boolean auto_play_flag = true;

    private long ms = 1000;

    private long startTime = 0;

    private long endTime = 0;

    private String value = Constant.valueCodeArray[0];
    private String count = Constant.countArray[0];

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:


                    if (auto_play_flag) {
                        Log.i(TAG, "start auto");
                        long currentTimeMillis = System.currentTimeMillis();
                        if(currentTimeMillis >= startTime && currentTimeMillis <= endTime) {
                            Log.i(TAG, "buy_jd" + value + " " + count);
                            myWebView.loadUrl("javascript:buy_jd(" + value + ", " + count + ")");

                            Message message = mHandler.obtainMessage(1);
                            mHandler.sendMessageDelayed(message, ms);
                        } else {
                            if(currentTimeMillis < startTime) {
                                Message message = mHandler.obtainMessage(1);
                                mHandler.sendMessageDelayed(message, startTime - currentTimeMillis);
                                Log.i(TAG, "start after!!");
                                Toast.makeText(getApplicationContext(), "将于" + (startTime - currentTimeMillis) + "ms之后开始", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void stopTimer(){
        auto_play_flag = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        initFab();
        myWebView = (WebView) findViewById(R.id.web_view);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
//        myWebView.setWebContentsDebuggingEnabled(true);
        myWebView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });
        textView = findViewById(R.id.text_view);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView.setGravity(Gravity.TOP);
        myWebView.addJavascriptInterface(new WebAppInterface(this, textView), "Android");

        myWebView.loadUrl("https://h5.zuifuli.com/thirdcard");

        TextClock textClock = findViewById(R.id.text_clock);
        textClock.setFormat24Hour("HH:mm:ss");

    }

    private View.OnClickListener getCommonAction() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                initJs();
                SharedPreferences perf = getSharedPreferences("config", MODE_PRIVATE);

//                Log.i(TAG, "buy: " + Constant.valueCodeArray[perf.getInt("value", 0)] + "  " + Constant.countArray[perf.getInt("num", 0)]);
                myWebView.loadUrl("javascript:buy_jd(" + Constant.valueCodeArray[perf.getInt("value", 0)] + ", " + Constant.countArray[perf.getInt("num", 0)] + ")");
            }
        };
    }

    private void initJs() {
        if (!flag) {
            BufferedReader br = null;
            try {
                InputStream inputStream = getResources().openRawResource(R.raw.buy_jd);
                 br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null)
                {
                    sb.append(line);
                }

                myWebView.loadUrl("javascript:" + sb.toString());
                flag = true;
            } catch (Exception e) {
                Log.e(TAG, "int js failed.");
            } finally {
                if(null != br) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        Log.e(TAG, "int js failed.");
                    }
                }
            }

        }
    }


    private View.OnClickListener getAutoAction() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initJs();
                SharedPreferences perf = getSharedPreferences("config", MODE_PRIVATE);

                if(!auto_play_flag) {
                    fab.setImageResource(android.R.drawable.ic_media_pause);

                    Message message = mHandler.obtainMessage(1);
                    mHandler.sendMessageDelayed(message, ms);
                    auto_play_flag = true;
                } else {
                    fab.setImageResource(android.R.drawable.ic_media_play);
                    auto_play_flag = false;
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            myWebView.loadUrl("https://h5.zuifuli.com/login?redirectUrl=https%3A%2F%2Fh5.zuifuli.com%2Fthirdcard");
            return true;
        } else if(id == R.id.action_settings) {
            startActivity(new Intent(this, Main2Activity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initFab();

    }

    private void initFab() {
        SharedPreferences perf = getSharedPreferences("config", MODE_PRIVATE);
        Log.i(TAG, "resume");
        if(!perf.getBoolean("switch", false)) {
            fab.setOnClickListener(getCommonAction());
        } else {

            Log.i(TAG, "switch:" + perf.getBoolean("switch", false));
            ms = Long.valueOf(perf.getString("ms", "1000"));
            Calendar calendar = Calendar.getInstance();
            String startStr = perf.getString("start", "20:59:50");
            String endStr = perf.getString("end", "21:00:15");

            String[] s = startStr.split(":");
            calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(s[0]));
            calendar.set(Calendar.MINUTE, Integer.valueOf(s[1]));
            calendar.set(Calendar.SECOND, Integer.valueOf(s[2]));
            startTime = calendar.getTimeInMillis();

            String[] e = endStr.split(":");
            calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(e[0]));
            calendar.set(Calendar.MINUTE, Integer.valueOf(e[1]));
            calendar.set(Calendar.SECOND, Integer.valueOf(e[2]));
            endTime = calendar.getTimeInMillis();

            value = Constant.valueCodeArray[perf.getInt("value", 0)];
            count = Constant.countArray[perf.getInt("num", 0)];

            fab.setOnClickListener(getAutoAction());
        }
    }
}

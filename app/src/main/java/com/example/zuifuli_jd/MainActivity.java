package com.example.zuifuli_jd;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import static android.view.KeyEvent.KEYCODE_BACK;

public class MainActivity extends AppCompatActivity {

    private WebView myWebView;

    private boolean flag;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                if(!flag) {
                    myWebView.loadUrl("javascript:function buy_jd(type, sum) {var sum = sum || 3;var type = type || 20107;var request_data = {cardDefId: \"5\",skus: [{skuId: type,quantity: 5}]};var xhr = new XMLHttpRequest();xhr.open('GET', 'https://api.zuifuli.com/api/product/v1/card/defs/5/detail', false);xhr.withCredentials = true;xhr.send(null);var response = JSON.parse(xhr.response);if(response[\"code\"] !== \"0\") {Android.showToast(\">>>\" + xhr.response + \"<<<<\");} else {if(response.result.skus[0].stockQuantity > 0) {xhr.open('POST', 'https://api.zuifuli.com/api/product/v1/card/buy', false);xhr.withCredentials = true;xhr.setRequestHeader('Content-Type', 'application/json');xhr.send(JSON.stringify(request_data));Android.showToast(\">>>\" + xhr.response + \"<<<<\");} else {Android.showToast(\">>>query result is 0<<<<\");}}}");
                    flag = true;
                }

                myWebView.loadUrl("javascript:buy_jd()");
            }
        });
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
        if (id == R.id.action_settings) {
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
}

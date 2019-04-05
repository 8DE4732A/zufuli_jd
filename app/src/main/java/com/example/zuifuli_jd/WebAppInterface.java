package com.example.zuifuli_jd;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class WebAppInterface {
    MainActivity mContext;

    TextView textView;


    private List<String> messageQueue = Collections.synchronizedList(new ArrayList<String>());

    private long count  = 0;

    /** Instantiate the interface and set the context */
    WebAppInterface(MainActivity c, TextView textView) {
        mContext = c;
        this.textView = textView;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
//        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();


        messageQueue.add(count++ + ":" + toast);

        textView.setText(String.join("\n", messageQueue.subList(messageQueue.size() - 6 < 0 ? 0 : messageQueue.size() - 6, messageQueue.size())));

        if(messageQueue.size() > 300) {
            messageQueue = messageQueue.subList(messageQueue.size() - 200, messageQueue.size());
        }
    }

    @JavascriptInterface
    public void setSuccess() {
        mContext.stopTimer();
    }

}
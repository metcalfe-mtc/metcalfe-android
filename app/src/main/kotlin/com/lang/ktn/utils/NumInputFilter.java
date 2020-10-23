package com.lang.ktn.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

/**
 * Created by ziyong on 2018/12/26.
 * webview.analysis.com.wb.NumInputFilter
 * —。—
 */
public class NumInputFilter implements InputFilter {

    private String digs="1234567890.0";
    private int mLeft=16;
    private int mRight=4;

    public NumInputFilter(int mLeft, int mRight){
        this.mLeft=mLeft;
        this.mRight=mRight;
    }

    public NumInputFilter(){
        this.mLeft=16;
        this.mRight=4;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        if(!TextUtils.isEmpty(source) ){
            for (int i = 0; i < source.length(); i++) {
                if(!digs.contains(String.valueOf(source.charAt(i)))){
                    return "";
                }
            }
            StringBuilder sb = new StringBuilder(dest);
            sb.insert(dstart, source);
            String[] s41=sb.toString().split("\\.");
            if(s41.length==2){
                if(s41[1].length()>mRight){
                    return "";
                }
                if(s41[0].length()>mLeft){
                    return "";
                }
            }

            if(s41.length==1){
                if(s41[0].length()>mLeft){
                    return "";
                }
            }
            return source;
        }
        return null;
    }

}

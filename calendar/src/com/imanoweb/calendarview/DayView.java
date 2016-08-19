package com.imanoweb.calendarview;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Nilanchala on 9/11/15.
 */
public class DayView extends LinearLayout {
    private Date date;
    private List<DayDecorator> decorators;

    private TextView textViewDate;
    private TextView textViewDesc;

    public DayView(Context context) {
        this(context, null, 0);
    }

    public DayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOrientation(VERTICAL);

        setPadding(2, 2, 2, 2);

        textViewDate = new TextView(context);
        textViewDate.setTypeface(Typeface.DEFAULT_BOLD);
        textViewDesc = new TextView(context);
        textViewDesc.setTextSize(8);
        textViewDesc.setEllipsize(TextUtils.TruncateAt.END);
        textViewDesc.setLines(2);

        addView(textViewDate);
        addView(textViewDesc);

        LayoutParams params = (LayoutParams)textViewDesc.getLayoutParams();
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        textViewDesc.setLayoutParams(params);
    }

    public void bind(Date date, List<DayDecorator> decorators, String desc) {
        this.date = date;
        this.decorators = decorators;

        final SimpleDateFormat df = new SimpleDateFormat("d");
        int day = Integer.parseInt(df.format(date));
        textViewDate.setText(String.valueOf(day));
        if (desc != null){
            textViewDesc.setText(desc);
        }
    }

    public void setTextColor(int textColor){
        textViewDate.setTextColor(textColor);
        textViewDesc.setTextColor(textColor);
    }

    public void setTypeface(Typeface typeface){
        textViewDate.setTypeface(typeface);
        textViewDesc.setTypeface(typeface);
    }

    public void setText(String text){
        textViewDate.setText(text);
    }

    public String getDay(){
        return textViewDate.getText().toString();
    }

    public void decorate() {
        //Set custom decorators
        if (null != decorators) {
            for (DayDecorator decorator : decorators) {
                decorator.decorate(this);
            }
        }
    }

    public Date getDate() {
        return date;
    }
}
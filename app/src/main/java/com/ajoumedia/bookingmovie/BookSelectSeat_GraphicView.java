package com.ajoumedia.bookingmovie;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 예매 - 좌석 선택
 * 좌석 Canvas Graphic
 */
public class BookSelectSeat_GraphicView extends View {

    Context context;

    Paint text_paint, r_paint, b_paint;
    int left, top, right, bottom;

    String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
    String[] cols = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10"};

    int s_left, s_top, s_right, s_bottom;
    String row, col;

    ArrayList<HashMap<String, Object>> selectList;
    HashMap<String, Object> select;

    int numOfPerson = 0;

    public BookSelectSeat_GraphicView(Context context) {
        super(context);
        this.context = context;
        setBackgroundColor(Color.parseColor("#A6A6A6"));
        r_paint = new Paint();
        b_paint = new Paint();
        text_paint = new Paint();
        selectList = new ArrayList<>();
        select = new HashMap<>();
        numOfPerson = 0;
    }

    public BookSelectSeat_GraphicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setBackgroundColor(Color.parseColor("#A6A6A6"));
        r_paint = new Paint();
        b_paint = new Paint();
        text_paint = new Paint();
        selectList = new ArrayList<>();
        select = new HashMap<>();
        numOfPerson = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        r_paint.setColor(Color.parseColor("#D5D5D5"));
        r_paint.setAntiAlias(true);
        b_paint.setColor(Color.parseColor("#AA1212"));
        b_paint.setAntiAlias(true);
        text_paint.setColor(Color.parseColor("#FFFFFF"));
        text_paint.setAntiAlias(true);
        float textSize = text_paint.getTextSize();
        float largeTextSize = textSize * 2;
        text_paint.setTextSize(largeTextSize);


        for(int i = 0; i < 5; i++) {
            for(int j = 0; j < 10; j++) {
                left    = 20 + i * 67;
                top     = 18 + j * 62;
                right   = 82 + i * 67;
                bottom  = 75 + j * 62;
                canvas.drawRect(left, top, right, bottom, r_paint);
                canvas.drawText(rows[j]+cols[i], left+10, top+30, text_paint);
            }
        }
        for(int i = 5; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                left    = 30 + i * 67;
                top     = 18 + j * 62;
                right   = 92 + i * 67;
                bottom  = 75 + j * 62;
                canvas.drawRect(left, top, right, bottom, r_paint);
                canvas.drawText(rows[j]+cols[i], left+10, top+30, text_paint);
            }
        }
        text_paint.setTextSize(textSize);

        if (selectList.size() > 0) {
            for (HashMap<String, Object> s : selectList) {
                text_paint.setTextSize(largeTextSize);
                canvas.drawRect((Integer) s.get("s_left"), (Integer) s.get("s_top"), (Integer) s.get("s_right"), (Integer) s.get("s_bottom"), b_paint);
                canvas.drawText(String.valueOf(s.get("row")) + String.valueOf(s.get("col")), (Integer) s.get("s_left") + 10, (Integer) s.get("s_top") + 30, text_paint);
                text_paint.setTextSize(textSize);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x, y;
        x = (int) event.getX();
        y = (int) event.getY();

        int i, j;

        if(event.getAction() == MotionEvent.ACTION_UP) {

            if(event.getX()>360.0f) {
                i = (x-30) / 67;
                s_left    = 30 + i * 67;
                s_right   = 92 + i * 67;
            } else {
                i = (x-20) / 67;
                s_left    = 20 + i * 67;
                s_right   = 82 + i * 67;
            }
            j = (y-18) / 62;

            s_top     = 18 + j * 62;
            s_bottom  = 75 + j * 62;

            row = rows[j];
            col = cols[i];

            select = new HashMap<>();
            select.put("s_left", s_left);
            select.put("s_right", s_right);
            select.put("s_top", s_top);
            select.put("s_bottom", s_bottom);
            select.put("row", row);
            select.put("col", col);

            if(selectList.contains(select)){
                selectList.remove(select);
            } else {
                selectList.add(select);
            }
            invalidate();
        }

        return true;

    }

    // 선택 좌석 리스트 getter
    public ArrayList<HashMap<String, Object>> getSelectList() {
        return selectList;
    }
}

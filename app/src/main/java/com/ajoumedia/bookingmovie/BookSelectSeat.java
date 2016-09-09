package com.ajoumedia.bookingmovie;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * 예매 액티비티
 * 인원수, 좌석 선택
 */
public class BookSelectSeat extends ActionBarActivity implements View.OnClickListener, View.OnTouchListener {

    // Menu - ActionBar (actionbar_custom_b.xml)
    TextView menu_back_btn, menu_title_tv;

    BookSelectSeat_GraphicView seat_graphicView;

    TextView screen_tv, time_tv, remainder_tv,
            adult_tv, adult_num_tv, teen_tv, teen_num_tv,
            adult_plus_btn, adult_minus_btn, teen_plus_btn, teen_minus_btn,
            selected_seat_tv, seat_pay_tv, finish_btn;

    int numOfPerson = 0, numOfAdult = 0, numOfTeen = 0, pay = 0, inning;

    Boolean isAllNotBookedSeat = true;

    ArrayList<HashMap<String, Object>> selectList;

    // DB
    DBHelper dbHelper;
    BookedSeatVO bookedSeatVO;
    TicketVO ticketVO;

    Thread sThread;
    Boolean sThreadRunning = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_seat);

        // DB
        dbHelper = new DBHelper(this);

        // Menu ActionBar B
        ActionBar menuBar = getSupportActionBar();
        menuBar.setDisplayShowHomeEnabled(false);
        menuBar.setDisplayShowTitleEnabled(false);
        menuBar.setDisplayShowCustomEnabled(true);
        View menuCustomView = getLayoutInflater().inflate(R.layout.actionbar_custom_b, null);
        menuCustomView.setLayoutParams(new android.support.v7.app.ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        menuBar.setCustomView(menuCustomView);
        Toolbar parent = (Toolbar) menuCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        menu_back_btn = (TextView) findViewById(R.id.menu_back_btn);
        menu_back_btn.setText("<");
        menu_back_btn.setOnClickListener(this);
        menu_title_tv = (TextView) findViewById(R.id.menu_title);
        menu_title_tv.setText(getString(R.string.menu_bookselect));

        seat_graphicView = (BookSelectSeat_GraphicView) findViewById(R.id.book_seat_graphic);

        screen_tv = (TextView) findViewById(R.id.book_seat_screen);
        time_tv = (TextView) findViewById(R.id.book_seat_time);
        remainder_tv = (TextView) findViewById(R.id.book_seat_remainder);
        adult_tv = (TextView) findViewById(R.id.book_seat_adult_text);
        adult_num_tv = (TextView) findViewById(R.id.book_seat_adult_num);
        teen_tv = (TextView) findViewById(R.id.book_seat_teen_text);
        teen_num_tv = (TextView) findViewById(R.id.book_seat_teen_num);
        adult_plus_btn = (TextView) findViewById(R.id.book_seat_plus_adult);
        adult_minus_btn = (TextView) findViewById(R.id.book_seat_minus_adult);
        teen_plus_btn = (TextView) findViewById(R.id.book_seat_plus_teen);
        teen_minus_btn = (TextView) findViewById(R.id.book_seat_minus_teen);
        selected_seat_tv = (TextView) findViewById(R.id.book_seat_selected_seat_str);
        seat_pay_tv = (TextView) findViewById(R.id.book_seat_pay);
        seat_pay_tv.setText(String.valueOf(pay));
        finish_btn = (TextView) findViewById(R.id.book_seat_select_finish_btn);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        screen_tv.setText(bundle.getString("screen"));
        inning = bundle.getInt("inning");
        time_tv.setText(bundle.getString("time")+" ("+inning+getString(R.string.book_time_inning)+")");
        remainder_tv.setText(bundle.getString("remainder"));
        bookedSeatVO = (BookedSeatVO) bundle.getSerializable("bookedSeatVO");

        adult_plus_btn.setOnClickListener(this);
        adult_minus_btn.setOnClickListener(this);
        teen_plus_btn.setOnClickListener(this);
        teen_minus_btn.setOnClickListener(this);
        finish_btn.setOnClickListener(this);

        adult_plus_btn.setOnTouchListener(this);
        adult_minus_btn.setOnTouchListener(this);
        teen_plus_btn.setOnTouchListener(this);
        teen_minus_btn.setOnTouchListener(this);
        finish_btn.setOnTouchListener(this);

        // CustomView에서 선택된 좌석을 실시간으로 받아와, 하단에 선택된 좌석번호를 출력하기 위해서 Thread를 사용
        sThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(sThreadRunning) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Log.d("THREAD ERROR", e.getMessage());
                    }

                    // UI제어 - runOnUiThread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            selectList = seat_graphicView.getSelectList();
                            String seat_str = "";
                            for (HashMap<String, Object> s : selectList) {
                                seat_str += s.get("row");
                                seat_str += s.get("col");
                                seat_str += " ";
                            }
                            selected_seat_tv.setText(seat_str);
                        }
                    });
                }
            }
        });

        sThread.start();

    }

    @Override
    public void onClick(View v) {

        if(v == menu_back_btn) {
            finish();
        } else if(v == adult_plus_btn) {
            numOfAdult ++;
            adult_num_tv.setText(String.valueOf(numOfAdult));
            checkNumOfMan();
        } else if(v == adult_minus_btn) {
            if(numOfAdult > 0) {
                numOfAdult --;
                adult_num_tv.setText(String.valueOf(numOfAdult));
                checkNumOfMan();
            }
        } else if(v == teen_plus_btn) {
            numOfTeen ++;
            teen_num_tv.setText(String.valueOf(numOfTeen));
            checkNumOfMan();
        } else if(v == teen_minus_btn) {
            if(numOfTeen > 0) {
                numOfTeen --;
                teen_num_tv.setText(String.valueOf(numOfTeen));
                checkNumOfMan();
            }
        } else if (v == finish_btn) {
            // 인원 수와 선택된 좌석 수가 맞는지 체크
            int selectListSize = selectList.size();
            numOfPerson = numOfAdult + numOfTeen;
            if (selectListSize != numOfPerson) {
                if (selectListSize > numOfPerson) {
                    Toast.makeText(BookSelectSeat.this, "선택된 좌석 수가 인원 수보다 많습니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                } else if (selectListSize < numOfPerson) {
                    Toast.makeText(BookSelectSeat.this, "선택된 좌석 수가 인원 수보다 적습니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            } else {
                BookedSeatVO checkvo = bookedSeatVO;
                isAllNotBookedSeat = true;
                String bookedseatstr = "";
                // SQLite - 선택된 좌석이 예매된 좌석인지 체크
                for(int i = 0; i < numOfPerson; i++) {
                    checkvo.setBs_seatrow(String.valueOf(selectList.get(i).get("row")));
                    checkvo.setBs_seatcol(String.valueOf(selectList.get(i).get("col")));
                    if (dbHelper.checkIsBookedSeat(checkvo)) {
                        isAllNotBookedSeat = false;
                        bookedseatstr += (checkvo.getBs_seatrow() + checkvo.getBs_seatcol() + " ");
                    }
                }
                if (!isAllNotBookedSeat) {
                    Toast.makeText(BookSelectSeat.this,  bookedseatstr + "좌석은 예매된 좌석입니다. 다른 좌석을 선택해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // 예매
                    bookedSeatVO.setBs_booknumber(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                    String seatStr = "";
                    for(int i = 0; i < numOfPerson; i++) {
                        bookedSeatVO.setBs_seatrow(String.valueOf(selectList.get(i).get("row")));
                        bookedSeatVO.setBs_seatcol(String.valueOf(selectList.get(i).get("col")));
                        seatStr += (bookedSeatVO.getBs_seatrow()+bookedSeatVO.getBs_seatcol()+" ");
                        dbHelper.insertBookedSeat(bookedSeatVO);
                    }
                    ticketVO = new TicketVO();
                    ticketVO.setT_showdate(bookedSeatVO.getBs_showdate());
                    ticketVO.setT_theater(bookedSeatVO.getBs_theater());
                    ticketVO.setT_screen(bookedSeatVO.getBs_screen());
                    ticketVO.setT_inning(bookedSeatVO.getBs_inning());
                    ticketVO.setT_seatstr(seatStr);
                    ticketVO.setT_booknumber(bookedSeatVO.getBs_booknumber());
                    dbHelper.insertTicket(ticketVO);
                    Intent intent = new Intent(BookSelectSeat.this, MyTicketList.class);
                    startActivity(intent);
                }
            }
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 인원 입력 버튼 (+,-) 터치시 색깔 변화 효과
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if((v == adult_plus_btn) || (v == teen_plus_btn)) {
                v.setBackgroundColor(Color.parseColor("#8C8C8C"));
            } else if((v == adult_minus_btn) || (v == teen_minus_btn)) {
                v.setBackgroundColor(Color.parseColor("#BDBDBD"));
            } else if(v == finish_btn) {
                v.setBackgroundColor(Color.parseColor("#930000"));
            }
        } else if(event.getAction() == MotionEvent.ACTION_UP) {
            if((v == adult_plus_btn) || (v == teen_plus_btn)) {
                v.setBackgroundColor(Color.parseColor("#A6A6A6"));
            } else if((v == adult_minus_btn) || (v == teen_minus_btn)) {
                v.setBackgroundColor(Color.parseColor("#D5D5D5"));
            } else if(v == finish_btn) {
                v.setBackgroundColor(Color.parseColor("#AA1212"));
            }
        }

        return false;
    }

    // 인원수 체크하고, 인원수에 따라 결제금액 출력
    public void checkNumOfMan() {

        if(numOfAdult >0) {
            adult_tv.setTextColor(Color.parseColor("#4C4C4C"));
            adult_num_tv.setTextColor(Color.parseColor("#4C4C4C"));
        } else {
            adult_tv.setTextColor(Color.parseColor("#D5D5D5"));
            adult_num_tv.setTextColor(Color.parseColor("#D5D5D5"));
        }

        if(numOfTeen >0) {
            teen_tv.setTextColor(Color.parseColor("#4C4C4C"));
            teen_num_tv.setTextColor(Color.parseColor("#4C4C4C"));
        } else {
            teen_tv.setTextColor(Color.parseColor("#D5D5D5"));
            teen_num_tv.setTextColor(Color.parseColor("#D5D5D5"));
        }

        if(inning == 1) {
            pay = numOfAdult * 6000 + numOfTeen * 6000; // 조조 가격
        } else {
            pay = numOfAdult * 10000 + numOfTeen * 7000; // 일반 가격
        }
        seat_pay_tv.setText(String.valueOf(pay));
    }
}

package com.ajoumedia.bookingmovie;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 예매 내역 액티비티
 */
public class MyTicketList extends ActionBarActivity implements View.OnClickListener, View.OnTouchListener {

    // Menu - ActionBar (actionbar_custom_a.xml)
    TextView menu_home_btn, menu_theater_btn, menu_book_btn, menu_myticket_btn;

    ListView ticket_listview;

    DBHelper dbHelper;
    ArrayList<TicketVO> ticketList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myticket_list);

        // ActionBar Custom A
        ActionBar menuBar = getSupportActionBar();
        menuBar.setDisplayShowHomeEnabled(false);
        menuBar.setDisplayShowTitleEnabled(false);
        menuBar.setDisplayShowCustomEnabled(true);
        View menuCustomView = getLayoutInflater().inflate(R.layout.actionbar_custom_a, null);
        menuCustomView.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        menuBar.setCustomView(menuCustomView);
        Toolbar parent = (Toolbar) menuCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        menu_home_btn = (TextView) findViewById(R.id.menu_home_btn);
        menu_theater_btn = (TextView) findViewById(R.id.menu_theater_btn);
        menu_book_btn = (TextView) findViewById(R.id.menu_book_btn);
        menu_myticket_btn = (TextView) findViewById(R.id.menu_myticket_btn);
        menu_myticket_btn.setBackgroundColor(Color.parseColor("#930000"));
        menu_home_btn.setOnClickListener(this);
        menu_theater_btn.setOnClickListener(this);
        menu_book_btn.setOnClickListener(this);
        menu_myticket_btn.setOnClickListener(this);
        menu_home_btn.setOnTouchListener(this);
        menu_theater_btn.setOnTouchListener(this);
        menu_book_btn.setOnTouchListener(this);
        menu_myticket_btn.setOnTouchListener(this);

        ticket_listview = (ListView) findViewById(R.id.myticket_list_listview);

        // SQLite - 예매 내역 불러오기
        dbHelper = new DBHelper(this);
        ticketList = dbHelper.getAllTicketList();
        if (ticketList.size() == 0) {
            Toast.makeText(MyTicketList.this, "예매하신 내역이 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            TicketListAdapter ticketListAdapter = new TicketListAdapter(this, ticketList);
            ticket_listview.setAdapter(ticketListAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = getIntent();

        if(v == menu_home_btn) {
            // HOME click - MovieList Activity로 이동
            intent = new Intent(MyTicketList.this, MovieList.class);
        } else if(v == menu_theater_btn) {
            // 영화관 click - TheaterList Activity로 이동
            intent = new Intent(MyTicketList.this, TheaterList.class);
        } else if(v == menu_book_btn) {
            // 예매 click - BookSelectList Activity로 이동
            intent = new Intent(MyTicketList.this, BookSelectList.class);
        } else if(v == menu_myticket_btn) {
            // MY티켓 click - - 새로 고침
            intent = getIntent();
        }

        finish();
        startActivity(intent);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // ActionBar Menu 버튼 터치 시 Color 변화 효과
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if((v == menu_home_btn) || (v == menu_theater_btn) || (v == menu_book_btn)) {
                v.setBackgroundColor(Color.parseColor("#930000"));
            }
        } else if(event.getAction() == MotionEvent.ACTION_UP) {
            if ((v == menu_home_btn) || (v == menu_theater_btn) || (v == menu_book_btn)) {
                v.setBackgroundColor(Color.parseColor("#AA1212"));
            }
        }
        return false;
    }

    // 예매 내역 Listview Adapter
    public class TicketListAdapter extends BaseAdapter {

        Context context;
        ArrayList<TicketVO> tList;

        public TicketListAdapter(Context context, ArrayList<TicketVO> tList) {
            this.context = context;
            this.tList = tList;
        }

        @Override
        public int getCount() {
            return tList.size();
        }

        @Override
        public Object getItem(int position) {
            return tList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.myticket_list_listview_col, parent, false);
            }

            TextView movietitle_tv = (TextView) convertView.findViewById(R.id.myticket_list_col_movietitle);
            ImageView movieposter_iv = (ImageView) convertView.findViewById(R.id.myticket_list_col_poster);
            TextView b_number_tv = (TextView) convertView.findViewById(R.id.myticket_list_col_b_number);
            TextView theater_tv = (TextView) convertView.findViewById(R.id.myticket_list_col_theater);
            TextView screen_tv = (TextView) convertView.findViewById(R.id.myticket_list_col_screen);
            TextView showdate_tv = (TextView) convertView.findViewById(R.id.myticket_list_col_showdate);
            TextView inning_tv = (TextView) convertView.findViewById(R.id.myticket_list_col_inning);
            TextView seat_tv = (TextView) convertView.findViewById(R.id.myticket_list_col_seat);
            Button cancel_btn = (Button) convertView.findViewById(R.id.myticket_list_col_cancel_btn);
            Button diary_btn = (Button) convertView.findViewById(R.id.myticket_list_col_diary_btn);

            HashMap<String, Object> movie = getMovie(tList.get(position).getT_screen());
            movietitle_tv.setText(getString((Integer) movie.get("movie_title")));
            movieposter_iv.setImageResource((Integer) movie.get("movie_poster"));
            b_number_tv.setText(tList.get(position).getT_booknumber());
            theater_tv.setText(tList.get(position).getT_theater());
            screen_tv.setText(tList.get(position).getT_screen());
            showdate_tv.setText(tList.get(position).getT_showdate());
            inning_tv.setText(getInningTime(tList.get(position).getT_inning())+" ("+tList.get(position).getT_inning()+")");
            seat_tv.setText(tList.get(position).getT_seatstr());

            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyTicketList.this);
                    builder.setTitle(getString(R.string.cancel_book))
                            .setMessage(getString(R.string.cancel_book_question))
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dbHelper.deleteBookedSeatAndTicket(tList.get(position));
                                    finish();
                                    startActivity(getIntent());
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                }
            });

            diary_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyTicketList.this, MyTicketDiary.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ticketVO", tList.get(position));
                    intent.putExtras(bundle);
                    finish();
                    startActivity(intent);
                }
            });


            return convertView;
        }
    }

    public String getInningTime(String inning) {
        String inningTime = "";
        switch (inning.charAt(0)) {
            case '1': inningTime = getString(R.string.book_time_inning_01); break;
            case '2': inningTime = getString(R.string.book_time_inning_02); break;
            case '3': inningTime = getString(R.string.book_time_inning_03); break;
            case '4': inningTime = getString(R.string.book_time_inning_04); break;
            case '5': inningTime = getString(R.string.book_time_inning_05); break;
        }
        return inningTime;
    }

    public HashMap<String, Object> getMovie(String screen) {
        ResourceData resourceData = new ResourceData();
        return resourceData.getMovieList().get(Integer.parseInt(String.valueOf(screen.substring(0, screen.length() - 1)))-1);
    }
}

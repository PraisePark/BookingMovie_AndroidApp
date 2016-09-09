package com.ajoumedia.bookingmovie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * 예매 액티비티
 * 상영시간 선택
 */
public class BookSelectTime extends ActionBarActivity {

    // Menu - ActionBar (actionbar_custom_b.xml)
    TextView menu_back_btn, menu_title_tv;

    ListView time_listview;

    ArrayList<HashMap<String, Object>> timeList;
    HashMap<String, Object> time;

    HashMap<String, Object> selected_movie, selected_theater;
    Calendar selected_date;
    int selected_movie_screen;

    String screen_str, time_str, remainder_str;

    // DB
    DBHelper dbHelper;
    BookedSeatVO bookedSeatVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_time);

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
        menu_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        menu_title_tv = (TextView) findViewById(R.id.menu_title);
        menu_title_tv.setText(getString(R.string.menu_bookselect));

        time_listview = (ListView) findViewById(R.id.bookselect_time_listview);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        selected_movie = (HashMap<String, Object>) bundle.getSerializable("bookedMovie");
        selected_theater = (HashMap<String, Object>) bundle.getSerializable("bookedTheater");
        selected_date = (Calendar) bundle.getSerializable("bookedDate");
        selected_movie_screen = bundle.getInt("bookedMovieIndex") + 1;
        bookedSeatVO = (BookedSeatVO) bundle.getSerializable("bookedSeatVO");

        // 상영시간별로, DB에 접근하여 잔여좌석 받아오기
        timeList = new ArrayList<>();
        for (int i=0; i<5; i++) {
            time = new HashMap<>();
            time.put("inning", String.valueOf(i+1));
            bookedSeatVO.setBs_inning(String.valueOf(i + 1) + getString(R.string.book_time_inning));
            int remainder = dbHelper.getCountOfRemainderSeat(bookedSeatVO);
            time.put("remainderseat", remainder);
            timeList.add(time);
        }

        // set listadapter
        SelectTimeListAdapter selectTimeListAdapter = new SelectTimeListAdapter(getApplicationContext(), timeList);
        time_listview.setAdapter(selectTimeListAdapter);

    }

    // 상영시간 ListView ListAdapter
    public class SelectTimeListAdapter extends BaseAdapter {

        Context context;
        ArrayList<HashMap<String, Object>> tList;
        int inning = 0;

        public SelectTimeListAdapter(Context context, ArrayList<HashMap<String, Object>> tList) {
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
                convertView = inflater.inflate(R.layout.book_time_listview_col, parent, false);
            }

            TextView screen_tv = (TextView) convertView.findViewById(R.id.book_time_listview_col_screen);
            screen_str = "["+selected_movie_screen+getString(R.string.book_time_screen)+"]";
            screen_tv.setText(screen_str);

            TextView time_tv = (TextView) convertView.findViewById(R.id.book_time_listview_col_time);
            inning = Integer.parseInt((String) tList.get(position).get("inning"));
            time_str = getInningTime(inning) + " (" + inning + getString(R.string.book_time_inning) + ")";
            time_tv.setText(time_str);

            TextView seat_tv = (TextView) convertView.findViewById(R.id.book_time_listview_col_seat);
            remainder_str = String.valueOf((Integer) tList.get(position).get("remainderseat"));
            seat_tv.setText(remainder_str);

            // 해당 상영시간 클릭 시
            // bookedSeatVO에 상영시간, 회차 set
            // intent, bundle로 전송
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BookSelectTime.this, BookSelectSeat.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("screen", "[" + selected_movie_screen + getString(R.string.book_time_screen) + "]");
                    bundle.putInt("inning", Integer.parseInt((String) tList.get(position).get("inning")));
                    bundle.putString("time", getInningTime(Integer.parseInt((String) tList.get(position).get("inning"))));
                    bundle.putString("remainder", String.valueOf((Integer) tList.get(position).get("remainderseat")));
                    bookedSeatVO.setBs_inning(Integer.parseInt((String) tList.get(position).get("inning")) + getString(R.string.book_time_inning));
                    bundle.putSerializable("bookedSeatVO", bookedSeatVO);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }

    public String getInningTime(int inning) {
        String inningTime = "";
        switch (inning) {
            case 1: inningTime = getString(R.string.book_time_inning_01); break;
            case 2: inningTime = getString(R.string.book_time_inning_02); break;
            case 3: inningTime = getString(R.string.book_time_inning_03); break;
            case 4: inningTime = getString(R.string.book_time_inning_04); break;
            case 5: inningTime = getString(R.string.book_time_inning_05); break;
        }
        return inningTime;
    }
}

package com.ajoumedia.bookingmovie;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * 예매 액티비티
 * 상영일자, 영화관, 영화 선택
 */
public class BookSelectList extends ActionBarActivity implements View.OnClickListener {

    // Menu - ActionBar (actionbar_custom_b.xml)
    TextView menu_back_btn, menu_title_tv;

    LinearLayout date_btn, theater_btn, movie_btn, othets_btn;
    TextView date_tv, theater_tv, movie_tv, others_tv,
            selected_date_tv, selected_theater_tv, selected_movie_tv, selected_others_tv,
            go_date, go_theater, go_movie, go_others;

    DatePickerDialog datePickerDialog;
    SimpleDateFormat sdf;
    AlertDialog.Builder theaterDialog, movieDialog;
    AlertDialog md;

    // 영화, 영화관 데이터
    ResourceData resourceData;
    ArrayList<HashMap<String, Object>> movieList, theaterList;
    HashMap<String, Object> selected_movie, selected_theater;

    // 예매 테이블 접근을 위한 VO 객체
    BookedSeatVO bookedSeatVO;

    Boolean isSelected_Date = true, isSelected_Theater = false, isSelected_Movie = false;
    Calendar booked_date;
    String booked_theater, booked_movie;
    int booked_movie_index;

    // Thread 이용하여 상영일자, 영화관, 영화가 모두 선택되었을 때,
    // 시간/인원/좌석 버튼을 활성화시켜 시간/인원/좌석 선택으로 넘어갈 수 있도록 구현
    Thread bThread;
    Boolean bThreadRunning = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_selectlist);

        resourceData = new ResourceData();
        selected_movie = new HashMap<>();
        movieList = new ArrayList<>();
        movieList = resourceData.getMovieList();
        theaterList = resourceData.getTheaterList();

        // Database
        bookedSeatVO = new BookedSeatVO();

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

        date_btn = (LinearLayout) findViewById(R.id.bookselectlist_date_btn);
        theater_btn = (LinearLayout) findViewById(R.id.bookselectlist_theater_btn);
        movie_btn = (LinearLayout) findViewById(R.id.bookselectlist_movie_btn);
        othets_btn = (LinearLayout) findViewById(R.id.bookselectlist_others_btn);

        date_tv = (TextView) findViewById(R.id.bookselectlist_date);
        theater_tv = (TextView) findViewById(R.id.bookselectlist_theater);
        movie_tv = (TextView) findViewById(R.id.bookselectlist_movie);
        others_tv = (TextView) findViewById(R.id.bookselectlist_others);

        selected_date_tv = (TextView) findViewById(R.id.bookselectlist_selected_date);
        selected_theater_tv = (TextView) findViewById(R.id.bookselectlist_selected_theater);
        selected_movie_tv = (TextView) findViewById(R.id.bookselectlist_selected_movie);
        selected_others_tv = (TextView) findViewById(R.id.bookselectlist_selected_others);

        go_date = (TextView) findViewById(R.id.bookselectlist_go_date);
        go_date.setText(">");
        go_theater = (TextView) findViewById(R.id.bookselectlist_go_theater);
        go_theater.setText(">");
        go_movie = (TextView) findViewById(R.id.bookselectlist_go_movie);
        go_movie.setText(">");
        go_others = (TextView) findViewById(R.id.bookselectlist_go_others);
        go_others.setText(">");

        // 영화관, 영화에서 바로 예매로 넘어왔을 때, 영화관, 영화 정보 처리
        if(getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            if(bundle.containsKey("selectedMovie")) {
                selected_movie = (HashMap<String, Object>) bundle.getSerializable("selectedMovie");
                isSelected_Movie = true;
                selected_movie_tv.setText(getString((Integer) selected_movie.get("movie_title")));
            }
            if(bundle.containsKey("selectedTheater")){
                selected_theater = (HashMap<String, Object>) bundle.getSerializable("selectedTheater");
                isSelected_Theater = true;
                selected_theater_tv.setText(getString((Integer) selected_theater.get("theater_name")));
            }
        }

        sdf = new SimpleDateFormat("yyyy-MM-dd (E)");
        booked_date = Calendar.getInstance();
        selected_date_tv.setText(sdf.format(booked_date.getTime()));

        menu_back_btn.setOnClickListener(this);
        date_btn.setOnClickListener(this);
        theater_btn.setOnClickListener(this);
        movie_btn.setOnClickListener(this);
        othets_btn.setOnClickListener(this);

        // Thread 이용하여 상영일자, 영화관, 영화가 모두 선택되었을 때,
        // 시간/인원/좌석 버튼을 활성화시켜 시간/인원/좌석 선택으로 넘어갈 수 있도록 구현
        bThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(bThreadRunning) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Log.d("THREAD ERROR", e.getMessage());
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isSelected_Date && isSelected_Theater && isSelected_Movie) {
                                bThreadRunning = false;
                                others_tv.setTextColor(Color.parseColor("#606060"));
                                selected_others_tv.setTextColor(Color.parseColor("#000000"));
                                go_others.setTextColor(Color.parseColor("#AA1212"));
                            }
                        }
                    });
                }
            }
        });

        bThread.start();
    }

    @Override
    public void onClick(View v) {
        if(v == menu_back_btn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(BookSelectList.this);
            builder.setTitle(getString(R.string.cancel_book))
                    .setMessage(getString(R.string.cancel_book_question))
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(BookSelectList.this, MovieList.class);
                            finish();
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
        } else if(v == date_btn) {
            // 상영일자 선택 - 오늘 포함 7일간만 날짜 범위 제한
            datePickerDialog = new DatePickerDialog(BookSelectList.this, mDateSetListener, booked_date.get(Calendar.YEAR), booked_date.get(Calendar.MONTH), booked_date.get(Calendar.DATE));
            Calendar date = Calendar.getInstance();
            datePickerDialog.getDatePicker().setMinDate(date.getTimeInMillis());
            date.add(Calendar.DATE, 6);
            datePickerDialog.getDatePicker().setMaxDate(date.getTimeInMillis());
            datePickerDialog.show();
        } else if(v == theater_btn) {
            // 영화관 선택 - Custom AlertDialog
            final String[] theaters = {getString(R.string.theater01_name), getString(R.string.theater02_name), getString(R.string.theater03_name), getString(R.string.theater04_name)};
            theaterDialog = new AlertDialog.Builder(BookSelectList.this);
            LayoutInflater inflater = getLayoutInflater();
            View customTitleView = inflater.inflate(R.layout.book_theater_dialog_customtitle, null);
            theaterDialog.setCustomTitle(customTitleView)
                        .setItems(theaters, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isSelected_Theater = true;
                                booked_theater = theaters[which];
                                selected_theater_tv.setText(theaters[which]);
                                selected_theater = theaterList.get(which);
                            }
                        }).show();
        } else if(v == movie_btn) {
            // 영화 선택 - Custom AlertDialog
            movieDialog = new AlertDialog.Builder(BookSelectList.this);
            LayoutInflater inflater = getLayoutInflater();
            View customView = inflater.inflate(R.layout.book_movie_dialog, null);
            ListView listView = (ListView) customView.findViewById(R.id.bookselect_movie_dialog_listview);
            SelectMovieListAdapter selectMovieListAdapter = new SelectMovieListAdapter(BookSelectList.this, movieList);
            listView.setAdapter(selectMovieListAdapter);
            movieDialog.setView(customView)
                    .setCancelable(true);
            md = movieDialog.create();
            md.show();
        } else if(v == othets_btn) {
            // 상영일자, 영화관, 영화 모두 선택되었을 때, 시간/인원/좌석 선택으로 넘어가기
            // intent, bundle로 예매에 필요한 데이터들을 bookedSeatVO에 저장하여 전송
            if (isSelected_Date && isSelected_Theater && isSelected_Movie) {
                Intent intent = new Intent(BookSelectList.this, BookSelectTime.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bookedMovie", selected_movie);
                bundle.putSerializable("bookedTheater", selected_theater);
                bundle.putSerializable("bookedDate", booked_date);
                bundle.putInt("bookedMovieIndex", booked_movie_index);
                bookedSeatVO.setBs_showdate(selected_date_tv.getText().toString());
                bookedSeatVO.setBs_theater(selected_theater_tv.getText().toString());
                bookedSeatVO.setBs_screen(movietitleToScreen(selected_movie));
                bundle.putSerializable("bookedSeatVO", bookedSeatVO);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }

    }

    // 상영일자 다이얼로그
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            booked_date.set(year, monthOfYear, dayOfMonth);
            selected_date_tv.setText(sdf.format(booked_date.getTime()));
            isSelected_Date = true;
        }
    };

    // 영화 선택 다이얼로그 ListView Adapter
    public class SelectMovieListAdapter extends BaseAdapter {

        Context context;
        ArrayList<HashMap<String, Object>> mList;

        public SelectMovieListAdapter(Context context, ArrayList<HashMap<String, Object>> mList) {
            this.context = context;
            this.mList = mList;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.book_movie_dialog_listview_col, parent, false);
            }

            ImageView movie_poster_iv = (ImageView) convertView.findViewById(R.id.select_dialog_mlist_column_poster);
            movie_poster_iv.setLayoutParams(new LinearLayout.LayoutParams(168, 253));
            movie_poster_iv.setScaleType(ImageView.ScaleType.FIT_XY);
            movie_poster_iv.setImageResource((Integer) mList.get(position).get("movie_poster"));

            TextView movie_title_tv = (TextView) convertView.findViewById(R.id.select_dialog_mlist_column_title);
            movie_title_tv.setText(getString((Integer) mList.get(position).get("movie_title")));

            TextView movie_level_tv = (TextView) convertView.findViewById(R.id.select_dialog_mlist_column_level);
            movie_level_tv.setText(getString((Integer) mList.get(position).get("movie_level")));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    booked_movie_index = position;
                    selected_movie = mList.get(position);
                    isSelected_Movie = true;
                    booked_movie = getString((Integer) mList.get(position).get("movie_title"));
                    selected_movie_tv.setText(getString((Integer) mList.get(position).get("movie_title")));
                    md.cancel();
                }
            });
            return convertView;
        }
    }

    // 영화 별로 상영관 반환해주는 메서드
    public String movietitleToScreen(HashMap<String, Object> movie) {
        ArrayList<HashMap<String, Object>> movieList = resourceData.getMovieList();
        return (movieList.indexOf(movie)+1) + getString(R.string.book_time_screen);
    }

}

package com.ajoumedia.bookingmovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

/**
 * 영화 다이어리 액티비티
 */
public class MyTicketDiary extends ActionBarActivity {

    // Menu - ActionBar (actionbar_custom_b.xml)
    TextView menu_back_btn, menu_title_tv;

    TextView movietitle_tv, theater_tv, showdate_tv;
    ImageView movieposter_iv;

    TextView diary_tv;
    EditText diary_et;
    Button diary_save_btn;

    ResourceData resourceData;
    HashMap<String, Object> movie;

    TicketVO ticketVO;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myticket_diary);

        // ActionBar Custom B
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
                // 뒤로 가기 - MovieList Activity로 이동
                finish();
                startActivity(new Intent(MyTicketDiary.this, MyTicketList.class));
            }
        });
        menu_title_tv = (TextView) findViewById(R.id.menu_title);
        menu_title_tv.setText(getString(R.string.menu_diary));

        movietitle_tv = (TextView) findViewById(R.id.myticket_diary_movietitle);
        theater_tv = (TextView) findViewById(R.id.myticket_diary_theater);
        showdate_tv = (TextView) findViewById(R.id.myticket_diary_showdate);
        movieposter_iv = (ImageView) findViewById(R.id.myticket_diary_poster);

        diary_tv = (TextView) findViewById(R.id.myticket_diary_textview);
        diary_et = (EditText) findViewById(R.id.myticket_diary_edittext);
        diary_save_btn = (Button) findViewById(R.id.myticket_diary_save_btn);

        resourceData = new ResourceData();
        movie = new HashMap<>();

        dbHelper = new DBHelper(this);

        ticketVO = (TicketVO) getIntent().getExtras().getSerializable("ticketVO");
        movie = resourceData.getMovieList().get(Integer.parseInt(String.valueOf(ticketVO.getT_screen().substring(0, ticketVO.getT_screen().length()-1)))-1);
        movietitle_tv.setText(getString((Integer) movie.get("movie_title")));
        movieposter_iv.setImageResource((Integer) movie.get("movie_poster"));
        theater_tv.setText(ticketVO.getT_theater());
        showdate_tv.setText(ticketVO.getT_showdate());

        // SQLite를 이용하여 관람한 영화에 대한 간단한 감상평을 기록할 수 있도록 구현
        // 기록된 감상평이 없으면, 좌측처럼 EditText, Button이 보여지고,
        // 기록된 감상평이 있으면 우측처럼 TextView를 통해서 감상평을 확인할 수 있다.
        // 수정을 원할 때에는 TextView를 클릭하면 좌측 화면처럼 돌아가게 된다. 이때 수정을 하고 저장한다.

        if (ticketVO.getT_review() != null) {
            diary_tv.setVisibility(View.VISIBLE);
            diary_tv.setText(ticketVO.getT_review());
            diary_et.setVisibility(View.GONE);
            diary_save_btn.setVisibility(View.GONE);
        } else {
            diary_tv.setVisibility(View.GONE);
            diary_et.setVisibility(View.VISIBLE);
            diary_save_btn.setVisibility(View.VISIBLE);
        }

        diary_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diary_tv.setVisibility(View.GONE);
                diary_et.setVisibility(View.VISIBLE);
                diary_et.setText(diary_tv.getText());
                diary_save_btn.setVisibility(View.VISIBLE);
            }
        });

        diary_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticketVO.setT_review(diary_et.getText().toString());
                diary_tv.setText(dbHelper.updateReview(ticketVO));
                diary_tv.setVisibility(View.VISIBLE);
                diary_et.setVisibility(View.GONE);
                diary_save_btn.setVisibility(View.GONE);
            }
        });
    }
}

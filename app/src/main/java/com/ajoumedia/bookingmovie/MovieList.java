package com.ajoumedia.bookingmovie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ��ȭ ����Ʈ ��Ƽ��Ƽ
 */
public class MovieList extends ActionBarActivity implements View.OnClickListener, View.OnTouchListener {

    // Menu - ActionBar (actionbar_custom_a.xml)
    TextView menu_home_btn, menu_theater_btn, menu_book_btn, menu_myticket_btn;

    // ��ȭ ��� - GridView
    GridView movielistGridView;
    MovieListGridViewAdpater movieListGridViewAdpater;

    // AsyncTask
    DataLoadTask dataLoadTask;

    // ��ȭ ������
    ResourceData resourceData;
    ArrayList<HashMap<String, Object>> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_list);

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
        menu_home_btn.setBackgroundColor(Color.parseColor("#930000"));
        menu_theater_btn = (TextView) findViewById(R.id.menu_theater_btn);
        menu_book_btn = (TextView) findViewById(R.id.menu_book_btn);
        menu_myticket_btn = (TextView) findViewById(R.id.menu_myticket_btn);
        movielistGridView = (GridView) findViewById(R.id.movielist_gridview);

        // AsyncTask Thread - ��ȭ ������ �ҷ�����, ���
        dataLoadTask = new DataLoadTask();
        dataLoadTask.execute(0);

        // actionbar textviewButton set OnClickListener, OnTouchListener
        menu_home_btn.setOnClickListener(this);
        menu_theater_btn.setOnClickListener(this);
        menu_book_btn.setOnClickListener(this);
        menu_myticket_btn.setOnClickListener(this);
        menu_home_btn.setOnTouchListener(this);
        menu_theater_btn.setOnTouchListener(this);
        menu_book_btn.setOnTouchListener(this);
        menu_myticket_btn.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = getIntent();

        if(v == menu_home_btn) {
            // HOME click - ���� ��ħ
            intent = getIntent();
        } else if(v == menu_theater_btn) {
            // ��ȭ�� click - TheaterList Activity�� �̵�
            intent = new Intent(MovieList.this, TheaterList.class);
        } else if(v == menu_book_btn) {
            // ���� click - BookSelectList Activity�� �̵�
            intent = new Intent(MovieList.this, BookSelectList.class);
        } else if(v == menu_myticket_btn) {
            // MYƼ�� click - MyTicketList Activity�� �̵�
            intent = new Intent(MovieList.this, MyTicketList.class);
        }

        finish();
        startActivity(intent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // ActionBar Menu ��ư ��ġ �� Color ��ȭ ȿ��
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if((v == menu_home_btn) || (v == menu_book_btn) || (v == menu_myticket_btn)) {
                v.setBackgroundColor(Color.parseColor("#930000"));
            }
        } else if(event.getAction() == MotionEvent.ACTION_UP) {
            if ((v == menu_home_btn) || (v == menu_book_btn) || (v == menu_myticket_btn)) {
                v.setBackgroundColor(Color.parseColor("#AA1212"));
            }
        }
        return false;
    }

    // ��ȭ ����Ʈ GridView Adapter
    public class MovieListGridViewAdpater extends BaseAdapter {

        private Context context;
        private ArrayList<HashMap<String, Object>> mList;

        public MovieListGridViewAdpater(Context c, ArrayList<HashMap<String, Object>> movieList) {
            this.context = c;
            this.mList = movieList;
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
                convertView = inflater.inflate(R.layout.movie_list_gridview_custom, parent, false);
            }

            // ��ȭ ������ ImageView
            ImageView movie_poster_iv = (ImageView) convertView.findViewById(R.id.movielist_gridview_poster);
            movie_poster_iv.setImageResource((Integer) mList.get(position).get("movie_poster"));

            // ��ȭ Ÿ��Ʋ TextView
            TextView movie_title_tv = (TextView) convertView.findViewById(R.id.movielist_gridview_title);
            movie_title_tv.setText(getString((Integer) mList.get(position).get("movie_title")));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // GridView ��ȭ Ŭ�� �� ��ȭ ������(MovieDetail Activity)�� �̵�
                    //  - ���õ� ��ȭ ������ �Բ� ����(Bundle)
                    Intent intent = new Intent(MovieList.this, MovieDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("movie", mList.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }

    // ��ȭ ������, ������ �̹��� ���ҽ��� �ҷ����� �� OutOfMemoryError������ �߻��Ͽ� Thread ���
    // GridView UI ��� ���ÿ� �ϱ� ���ؼ� AsyncTask�� ���
    class DataLoadTask extends AsyncTask<Integer, Integer, Integer> {

        public DataLoadTask() {
            resourceData = new ResourceData();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            // ��ȭ ����Ʈ ������ ��������
            movieList = resourceData.getMovieList();
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            // ��ȭ ����Ʈ ������ GridView Set Adapter
            movieListGridViewAdpater = new MovieListGridViewAdpater(getApplicationContext(), movieList);
            movielistGridView.setAdapter(movieListGridViewAdpater);
        }
    }


}

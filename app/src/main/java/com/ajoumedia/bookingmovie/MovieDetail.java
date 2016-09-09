package com.ajoumedia.bookingmovie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashMap;

/**
 * 영화 상세정보 액티비티
 */
public class MovieDetail extends ActionBarActivity {

    // Menu - ActionBar (actionbar_custom_b.xml)
    TextView menu_back_btn, menu_title_tv;

    // 영화 상세정보 view
    LinearLayout backgroundLayout;
    ScrollView moviedeital_sv;
    TextView title_tv, title_e_tv, genre_tv, outline_tv, director_tv,
             actors_tv, level_tv, sysnopsis_tv, grade_tv;
    RatingBar grade_rb;
    Button synopsis_showmore_btn;
    HorizontalScrollView still_sv;
    ImageView still_iv;
    LinearLayout still_ly;
    Button book_thismovie_btn;

    // 영화 데이터
    HashMap<String, Object> movie;
    Integer[] still_cuts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail);

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
                startActivity(new Intent(MovieDetail.this, MovieList.class));
            }
        });
        menu_title_tv = (TextView) findViewById(R.id.menu_title);
        menu_title_tv.setText(getString(R.string.menu_moviedetial));

        backgroundLayout = (LinearLayout) findViewById(R.id.moviedetail_background);
        moviedeital_sv = (ScrollView) findViewById(R.id.moviedetail_scrollview);
        title_tv = (TextView) findViewById(R.id.moviedetail_title);
        title_e_tv = (TextView) findViewById(R.id.moviedetail_title_e);
        genre_tv = (TextView) findViewById(R.id.moviedetail_genre);
        outline_tv = (TextView) findViewById(R.id.moviedetail_outline);
        director_tv = (TextView) findViewById(R.id.moviedetail_director);
        actors_tv = (TextView) findViewById(R.id.moviedetail_actors);
        level_tv = (TextView) findViewById(R.id.moviedetail_level);
        sysnopsis_tv = (TextView) findViewById(R.id.moviedetail_synopsis);
        grade_tv = (TextView) findViewById(R.id.moviedetail_audiencegrade);
        grade_rb = (RatingBar) findViewById(R.id.moviedetail_audiencegrade_rating);
        // 평점 RatingBar Setting
        LayerDrawable stars = (LayerDrawable) grade_rb.getProgressDrawable(); // Color ����
        stars.getDrawable(0).setColorFilter(Color.parseColor("#A6A6A6"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.parseColor("#A6A6A6"), PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(2).setColorFilter(Color.parseColor("#AA1212"), PorterDuff.Mode.SRC_ATOP);
        grade_rb.setNumStars(5);
        grade_rb.setStepSize(0.1f);
        synopsis_showmore_btn = (Button) findViewById(R.id.moviedetail_synopsis_more);
        still_sv = (HorizontalScrollView) findViewById(R.id.moviedetail_photonvidio_scrollview);
        still_ly = (LinearLayout) findViewById(R.id.moviedetail_photonvidio_scrollview_layout);
        book_thismovie_btn = (Button) findViewById(R.id.moviedetail_bookthismovie_btn);

        // Intent 를 통해서 클릭한 영화 데이터 받아오기
        movie = (HashMap<String, Object>) getIntent().getExtras().getSerializable("movie");

        // 받아온 영화 데이터 위젯에 뿌려주기
        backgroundLayout.setBackgroundResource((Integer) movie.get("movie_poster"));
        title_tv.setText(getString((Integer) movie.get("movie_title")));
        title_e_tv.setText(getString((Integer) movie.get("movie_title_e")));
        genre_tv.setText(getString((Integer) movie.get("movie_genre")));
        outline_tv.setText(getString((Integer) movie.get("movie_time")) + " | " + getString((Integer) movie.get("movie_country")) + " | " + getString((Integer) movie.get("movie_releasedate")));
        director_tv.setText(getString((Integer) movie.get("movie_director")));
        actors_tv.setText(getString((Integer) movie.get("movie_actors")));
        actors_tv.setSelected(true);
        level_tv.setText(getString((Integer) movie.get("movie_level")));
        grade_tv.setText(getString((Integer) movie.get("movie_audiencegrade")));
        grade_rb.setRating(Float.parseFloat(getString((Integer) movie.get("movie_audiencegrade"))) / 2.0f); // ���� -> 5�� �������� ��ȯ
        sysnopsis_tv.setText(getString((Integer) movie.get("movie_synopsis")));

        // 영화 스틸컷 이미지 OutOfMemoryError 해결을 위해 BitmapFactory.Options의 SampleSize값을 변경
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        Bitmap bitmap;
        still_cuts = getStillCuts(getString((Integer) movie.get("movie_title")));
        for(int i = 0; i < still_cuts.length; i++) {
            still_iv = new ImageView(getApplicationContext());
            bitmap = BitmapFactory.decodeResource(getResources(), still_cuts[i], options);
            still_iv.setLayoutParams(new ViewGroup.LayoutParams(300, 200));
            still_iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            still_iv.setImageBitmap(bitmap);
            if(i == 0) {
                still_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 유투브 이미지 클릭 시 예고편 재생을 위해 YouTube로 이동
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString((Integer) movie.get("movie_youtube")))));
                    }
                });
            }
            still_ly.addView(still_iv);
        }

        // 시놉시스 더보기/접기 버튼
        synopsis_showmore_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (synopsis_showmore_btn.getText().equals(getString(R.string.showmore))) {
                    sysnopsis_tv.setMaxLines(50);
                    synopsis_showmore_btn.setText(getString(R.string.showless));
                } else if (synopsis_showmore_btn.getText().equals(getString(R.string.showless))) {
                    sysnopsis_tv.setMaxLines(5);
                    synopsis_showmore_btn.setText(getString(R.string.showmore));
                }
            }
        });

        // 해당 영화 예매 바로가기
        book_thismovie_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDetail.this, BookSelectList.class);
                Bundle bundle = new Bundle();
                // intent 통해서 해당 영화 데이터 전송
                bundle.putSerializable("selectedMovie", movie);
                intent.putExtras(bundle);
                finish();
                startActivity(intent);
            }
        });
    }

    // 영화 스틸컷 이미지 리소스를 가져오는 메서드
    public Integer[] getStillCuts(String movie_title) {
        Integer[] still_cuts = null;
        if(movie_title.equals(getString(R.string.avengers_title))) {
            still_cuts = ResourceData.avengers_still;
        } else if(movie_title.equals(getString(R.string.home_title))) {
            still_cuts = ResourceData.home_still;
        } else if(movie_title.equals(getString(R.string.jangsoo_title))) {
            still_cuts = ResourceData.jangsoo_still;
        } else if(movie_title.equals(getString(R.string.chinatown_title))) {
            still_cuts = ResourceData.chinatown_still;
        } else if(movie_title.equals(getString(R.string.kingsman_title))) {
            still_cuts = ResourceData.kingsman_still;
        } else if(movie_title.equals(getString(R.string.dinotime_title))) {
            still_cuts = ResourceData.dinotime_still;
        } else if(movie_title.equals(getString(R.string.twenty_title))) {
            still_cuts = ResourceData.twenty_still;
        } else if(movie_title.equals(getString(R.string.whiplash_title))) {
            still_cuts = ResourceData.whiplash_still;
        } else if(movie_title.equals(getString(R.string.spy_title))) {
            still_cuts = ResourceData.spy_still;
        } else if(movie_title.equals(getString(R.string.lovetaste_title))) {
            still_cuts = ResourceData.lovetaste_still;
        }
        return still_cuts;
    }
}

package com.ajoumedia.bookingmovie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 영화관 액티비티
 */
public class TheaterList extends ActionBarActivity implements View.OnClickListener, View.OnTouchListener {

    // Menu - ActionBar (actionbar_custom_a.xml)
    TextView menu_home_btn, menu_theater_btn, menu_book_btn, menu_myticket_btn;

    // 영화관 위치 Google Map
    GoogleMap mMap; // Might be null if Google Play services APK is not available.

    // 영화관 선택 Spinner
    Spinner theater_spinner;

    // 영화관 정보 widget
    TableLayout theaterInfo_tablely;
    TextView theater_name_tv, theater_address_tv, theater_tel_tv;
    Button go_book_btn;

    // 영화관 데이터
    ResourceData resourceData;
    HashMap<String, Object> theater;
    ArrayList<HashMap<String, Object>> theaterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theater_list);
        setUpMapIfNeeded();

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
        menu_theater_btn.setBackgroundColor(Color.parseColor("#930000"));
        menu_book_btn = (TextView) findViewById(R.id.menu_book_btn);
        menu_myticket_btn = (TextView) findViewById(R.id.menu_myticket_btn);

        theater_spinner = (Spinner) findViewById(R.id.theaterlist_spinner);
        theaterInfo_tablely = (TableLayout) findViewById(R.id.theaterlist_theaterinfo_tablelayout);
        theaterInfo_tablely.setVisibility(View.GONE);
        theater_name_tv = (TextView) findViewById(R.id.theaterlist_theater_name);
        theater_address_tv = (TextView) findViewById(R.id.theaterlist_theater_address);
        theater_tel_tv = (TextView) findViewById(R.id.theaterlist_theater_tel);
        go_book_btn = (Button) findViewById(R.id.theaterlist_gobook);

        // 영화관 리스트 데이터 지도에 Mark 띄우기
        resourceData = new ResourceData();
        theaterList = resourceData.getTheaterList();
        for (HashMap<String, Object> t : theaterList) {
            setUpMap(new LatLng(Double.parseDouble(getString((Integer) t.get("theater_lat"))),
                                Double.parseDouble(getString((Integer) t.get("theater_lon")))),
                                getString((Integer) t.get("theater_name")));
        }

        // 영화관 Spinner Set Adapter
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.theater_name_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        theater_spinner.setAdapter(spinnerAdapter);

        // 영화관 Spinner Item 선택 시 지도 위치 변경, 영화관 상세정보 출력
        theater_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    theaterInfo_tablely.setVisibility(View.GONE); // 영화관 정보 보여주는 Layout 보이지 않도록 설정
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.276708, 127.030405), 12));
                } else {
                    // Spinner Item 클릭 -> GoogleMap 카메라 해당 <영화관 위치>로 이동
                    theaterInfo_tablely.setVisibility(View.VISIBLE); // 영화관 정보 보여주는 Layout 보이도록 설정

                    // 영화관 데이터 가져와서 뿌려주기
                    theater = theaterList.get(position - 1);
                    setUpMap(new LatLng(Double.parseDouble(getString((Integer) theater.get("theater_lat"))),
                            Double.parseDouble(getString((Integer) theater.get("theater_lon")))), "");
                    theater_name_tv.setText(getString((Integer) theater.get("theater_name")));
                    theater_address_tv.setText(getString((Integer) theater.get("theater_address")));
                    SpannableString tel_str = new SpannableString("Tel. " + getString((Integer) theater.get("theater_tel")));
                    tel_str.setSpan(new UnderlineSpan(), 0, tel_str.length(), 0); // 전화번호 underline
                    theater_tel_tv.setText(tel_str);

                    // 영화관 전화번호 클릭 시 전화 이동
                    theater_tel_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + getString((Integer) theater.get("theater_tel"))));
                            startActivity(intent);
                        }
                    });

                    // 해당 영화관 예매 바로가기
                    go_book_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(TheaterList.this, BookSelectList.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("selectedTheater", theater);
                            intent.putExtras(bundle);
                            finish();
                            startActivity(intent);
                        }
                    });
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

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
            // HOME click - MovieList Activity로 이동
            intent = new Intent(TheaterList.this, MovieList.class);
        } else if(v == menu_theater_btn) {
            // 영화관 click - 새로고침
            intent = getIntent();
        } else if(v == menu_book_btn) {
            // 예매 click - BookSelectList Activity로 이동
            intent = new Intent(TheaterList.this, BookSelectList.class);
        } else if(v == menu_myticket_btn) {
            // MY티켓 click - MyTicketList Activity로 이동
            intent = new Intent(TheaterList.this, MyTicketList.class);
        }

        finish();
        startActivity(intent);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // ActionBar Menu 버튼 터치 시 Color 변화 효과
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

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.theaterlist_googlemap)).getMap();
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.276708, 127.030405), 10));
            }
        }
    }

    private void setUpMap(LatLng latLng, String title) {
        if(!title.equals("")) {
            MarkerOptions markerOption = new MarkerOptions();
            Bitmap resized_bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.mark1), 80, 80, true);
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(resized_bitmap));
            markerOption.position(latLng);
            markerOption.title(title);
            mMap.addMarker(markerOption);
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        }

    }
}
